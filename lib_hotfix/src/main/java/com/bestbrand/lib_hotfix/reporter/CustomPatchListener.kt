package com.bestbrand.lib_hotfix.reporter

import android.app.ActivityManager
import android.content.Context
import com.bestbrand.lib_hotfix.crash.SampleUncaughtExceptionHandler
import com.bestbrand.lib_hotfix.utils.Utils
import com.tencent.tinker.lib.listener.DefaultPatchListener
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil
import java.io.File

/**
 * @author XingWei
 * @time 2020/7/27
 * 自定义补丁的校验
 */
class CustomPatchListener(context: Context) : DefaultPatchListener(context) {

    companion object {
        private const val TAG = "Tinker.CustomPatchListener"

        //限制补丁大小60MB
        private const val NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = 60 * 1024 * 1024.toLong()
    }

    private var maxMemory = 0 //最大内存

    private var currentMD5: String? = null


    init {
        maxMemory =
            (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
        TinkerLog.i(TAG, "application maxMemory:$maxMemory")
    }

    fun setCurrentMD5(md5Value: String?) {
        currentMD5 = md5Value
    }


    override fun patchCheck(path: String?, patchMd5: String?): Int {
        val patchFile = File(path)
        TinkerLog.i(
            TAG,
            "receive a patch file: %s, file size:%d",
            path,
            SharePatchFileUtil.getFileOrDirectorySize(patchFile)
        )
        //校验MD5 防止篡改和指向错误
        if (currentMD5 != patchMd5) {
            //签名文件不一致
            return ShareConstants.ERROR_PATCH_DISABLE
        }
        var returnCode = super.patchCheck(path, patchMd5)
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            returnCode = Utils.checkForPatchRecover(
                NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN,
                maxMemory
            )
        }
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            val sp = context.getSharedPreferences(
                ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG,
                Context.MODE_MULTI_PROCESS
            )
            //判断这个补丁安装异常次数 不超过MAX_CRASH_COUNT
            val fastCrashCount = sp.getInt(patchMd5, 0)
            if (fastCrashCount >= SampleUncaughtExceptionHandler.MAX_CRASH_COUNT) {
                returnCode = Utils.ERROR_PATCH_CRASH_LIMIT
            }
        }
        return returnCode

    }
}