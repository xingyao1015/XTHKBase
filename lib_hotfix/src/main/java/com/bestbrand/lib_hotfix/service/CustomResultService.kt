package com.bestbrand.lib_hotfix.service

import android.os.Handler
import android.os.Looper
import android.os.Process
import android.widget.Toast
import com.bestbrand.lib_hotfix.BuildConfig
import com.bestbrand.lib_hotfix.utils.Utils
import com.tencent.tinker.lib.service.DefaultTinkerResultService
import com.tencent.tinker.lib.service.PatchResult
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.lib.util.TinkerServiceInternals
import java.io.File
import kotlin.system.exitProcess

/**
 * @author XingWei
 * @time 2020/7/27
 * 决定在patch安装完以后的后续操作，默认实现是杀进程
 */
class CustomResultService : DefaultTinkerResultService() {
    companion object {
        private const val TAG = "Tinker.CustomResultService"
    }

    override fun onPatchResult(result: PatchResult?) {
        if (result == null) {
            TinkerLog.e(TAG, "DefaultTinkerResultService received null result!!!!")
            return
        }
        TinkerLog.i(TAG, "DefaultTinkerResultService received a result:%s ", result.toString())

        //first, 杀死修复进程
        TinkerServiceInternals.killTinkerPatchServiceProcess(applicationContext)

        //之后可以在这里处理补丁加载完成之后的逻辑，弹窗之类的
        if (BuildConfig.DEBUG) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                if (result.isSuccess) {
                    Toast.makeText(applicationContext, "补丁加载成功", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "补丁加载失败", Toast.LENGTH_LONG).show()
                }
            }
        }
        if (result.isSuccess) {
            deleteRawPatchFile(File(result.rawPatchFilePath))
            if (checkIfNeedKill(result)) {
                //如果APP在后台，杀死
                if (Utils.isBackground(applicationContext)) {
                    TinkerLog.i(TAG, "it is in background, just restart process")
                    restartProcess()
                } else {
                    //注册一个广播，当锁定屏幕之后或者App切换到后台后 杀死APP
                    TinkerLog.i(TAG, "tinker wait screen to restart process")
                    Utils.ScreenState(applicationContext, object : Utils.ScreenState.IOnScreenOff {
                        override fun onScreenOff() {
                            restartProcess()
                        }
                    })
                }
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!")
            }
        }
    }

    /**
     * 可以通过服务或广播重新启动流程
     */
    private fun restartProcess() {
        try {
            TinkerLog.i(TAG, "app is background now, i can kill quietly")
            Process.killProcess(Process.myPid())
            exitProcess(0)
        } catch (e: Exception) {

        }

    }

}