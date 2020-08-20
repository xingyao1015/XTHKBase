package com.bestbrand.lib_hotfix.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.bestbrand.lib_hotfix.BuildConfig
import com.bestbrand.lib_hotfix.R
import com.bestbrand.lib_hotfix.TinkerManager
import com.bestbrand.lib_hotfix.module.BasePath
import com.bestbrand.lib_hotfix.task.DownloadTask
import com.bestbrand.lib_hotfix.task.FileCallback
import com.bestbrand.lib_hotfix.task.NetCallback
import com.bestbrand.lib_hotfix.task.NetworkTask
import com.bestbrand.lib_hotfix.utils.TaskDispatchManager
import com.bestbrand.lib_hotfix.utils.Utils
import com.google.gson.Gson
import com.tencent.tinker.lib.util.TinkerLog
import java.io.File
import java.lang.ref.WeakReference

/**
 * @author XingWei
 * @time 2020/7/29
 * @function 应用程序Tinker更新服务：
 * 1.从服务器下载patch文件
 * 2.使用TinkerManager完成patch文件加载
 * 3.patch文件会在下次进程启动时生效
 */
class TinkerService : Service() {
    private var mPatchFileDir //patch要保存的文件夹
            : String? = null
    private var mFilePtch //patch文件保存路径
            : String? = null
    private var mPatchInfo //服务器patch信息
            : BasePath? = null

    private val mHandler by lazy { TinkerServiceHandler(this) }

    private var mCurPushId = ""

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //检查是否有patch更新
        mCurPushId = intent?.getStringExtra(TAG) ?: ""
        TinkerLog.d(
            TAG,
            "是否是开发设备 ${applicationContext.resources.getStringArray(R.array.pushId)
                .contains(mCurPushId)}"
        )
        mHandler.sendEmptyMessage(UPDATE_PATCH)
        return START_NOT_STICKY //被系统回收不再重启
    }

    //初始化变量
    private fun init() {
        mPatchFileDir = getExternalFilesDir(null)?.absolutePath + "/tpatch/"
        val patchFileDir = File(mPatchFileDir)
        try {
            if (!patchFileDir.exists()) {
                patchFileDir.mkdir() //文件夹不存在则创建
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf() //无法正常创建文件，则终止服务
        }
    }

    /**
     * 检查是否有补丁
     */
    private fun checkPatchInfo() {
        val host =
            if (BuildConfig.DEBUG) "http://edu3.api.test.xthktech.cn/api/" else "https://app-edu.xthktech.cn/api/"
        val issueType = if (BuildConfig.DEBUG) 1 else 2
        val url =
            "${host}app_hot_update/get_new?app_version_name=${Utils.getAppVersionName(baseContext)}&issue_type=${issueType}&status=1"
        TaskDispatchManager.getInstance().execute(NetworkTask(url, "GET", object : NetCallback {
            override fun onSuccess(content: String) {
                //判断是否需要安装
                mPatchInfo = Gson().fromJson<BasePath>(content, BasePath::class.java)
                if (mPatchInfo?.needLoadPatch(applicationContext, mCurPushId) == true) {
                    mHandler.sendEmptyMessage(DOWNLOAD_PATCH)
                } else {
                    TinkerLog.d(TAG, "无需安装  ${mPatchInfo?.toString()}")
                    stopSelf()
                }
            }

            override fun onFail(e: Exception?) {
                TinkerLog.e(TAG, e?.message)
                stopSelf()
            }
        }))
    }

    /**
     * 下载补丁
     */
    private fun downloadPatch() {
        mFilePtch = mPatchFileDir + System.currentTimeMillis().toString() + FILE_END
        TaskDispatchManager.getInstance()
            .execute(DownloadTask(mPatchInfo!!.getPatchUrl(), mFilePtch!!, object : FileCallback {
                override fun onSuccess(file: File?) {
                    if (file != null) {
                        TinkerManager.loadPatch(mFilePtch, mPatchInfo!!.getPatchFileMd5())
                    } else {
                        TinkerLog.e(TAG, "download patch error")
                        stopSelf()
                    }
                }

                override fun onProgress(progress: Int) {
                    TinkerLog.d(TAG, "download patch progress $progress")
                }

                override fun onFail(e: Exception?) {
                    TinkerLog.e(TAG, e?.message)
                    stopSelf()
                }

            }))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    companion object {
        private const val TAG = "Tinker.TinkerService"
        private const val FILE_END = ".apk" //文件后缀名
        private const val DOWNLOAD_PATCH = 0x01 //下载patch文件信息
        private const val UPDATE_PATCH = 0x02 //检查是否有patch更新

        //对外提供启动servcie方法
        fun runTinkerService(context: Context, pushId: String) {
            try {
                val intent = Intent(context, TinkerService::class.java)
                intent.putExtra(TAG, pushId)
                context.startService(intent)
            } catch (e: Exception) {
                TinkerLog.e(TAG, e.message)
            }
        }

        private class TinkerServiceHandler(tinkerService: TinkerService) : Handler() {
            private val mServiceRef: WeakReference<TinkerService> = WeakReference(tinkerService)
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    DOWNLOAD_PATCH -> {
                        mServiceRef.get()?.downloadPatch()
                    }
                    UPDATE_PATCH -> {
                        mServiceRef.get()?.checkPatchInfo()
                    }
                }
            }
        }
    }
}