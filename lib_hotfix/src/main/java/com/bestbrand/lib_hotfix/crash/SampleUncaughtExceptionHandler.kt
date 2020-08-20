package com.bestbrand.lib_hotfix.crash

import android.content.Context
import android.os.SystemClock
import com.bestbrand.lib_hotfix.TinkerManager
import com.bestbrand.lib_hotfix.utils.Utils
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals

/**
 * @author XingWei
 * @time 2020/7/29
 *
 */
class SampleUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
    private val ueh: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        TinkerLog.e(TAG, "uncaughtException:" + ex?.message)
        tinkerFastCrashProtect()
        tinkerPreVerifiedCrashHandler(ex)
        ueh.uncaughtException(thread, ex)
    }

    /**
     * 官方写法，主要是处理Xposed带来的异常
     */
    private fun tinkerPreVerifiedCrashHandler(ex: Throwable?) {
        val applicationLike = TinkerManager.getTinkerApplicationLike()
        if (applicationLike == null || applicationLike.application == null) {
            TinkerLog.w(TAG, "applicationlike is null")
            return
        }
        if (!TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
            TinkerLog.w(TAG, "tinker is not loaded")
            return
        }
        var throwable = ex
        var isXposed = false
        while (throwable != null) {
            if (!isXposed) {
                isXposed = Utils.isXposedExists(throwable)
            }

            // xposed?
            if (isXposed) {
                var isCausedByXposed = false
                //for art, we can't know the actually crash type
                //just ignore art
                if (throwable is IllegalAccessError && throwable.message!!.contains(
                        DALVIK_XPOSED_CRASH
                    )
                ) {
                    //for dalvik, we know the actual crash type
                    isCausedByXposed = true
                }
                if (isCausedByXposed) {
                    TinkerLog.e(TAG, "have xposed: just clean tinker")
                    //kill all other process to ensure that all process's code is the same.
                    ShareTinkerInternals.killAllOtherProcess(applicationLike.application)
                    TinkerApplicationHelper.cleanPatch(applicationLike)
                    ShareTinkerInternals.setTinkerDisableWithSharedPreferences(applicationLike.application)
                    return
                }
            }
            throwable = throwable.cause!!
        }
    }

    /**
     * 如果修补程序已加载，并且崩溃超过MAX_CRASH_COUNT个，则清理补丁
     */
    private fun tinkerFastCrashProtect(): Boolean {
        val applicationLike = TinkerManager.getTinkerApplicationLike()
        if (applicationLike == null || applicationLike.application == null) {
            return false
        }
        if (!TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
            return false
        }
        val elapsedTime =
            SystemClock.elapsedRealtime() - applicationLike.applicationStartElapsedTime
        //此过程无法加载补丁
        if (elapsedTime < QUICK_CRASH_ELAPSE) {
            val currentVersion =
                TinkerApplicationHelper.getCurrentVersion(applicationLike)
            if (ShareTinkerInternals.isNullOrNil(currentVersion)) {
                return false
            }
            val sp = applicationLike.application
                .getSharedPreferences(
                    ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG,
                    Context.MODE_MULTI_PROCESS
                )
            val fastCrashCount = sp.getInt(currentVersion, 0) + 1
            if (fastCrashCount >= MAX_CRASH_COUNT) {
                TinkerApplicationHelper.cleanPatch(applicationLike)
                TinkerLog.e(
                    TAG,
                    "tinker has fast crash more than %d, we just clean patch!",
                    fastCrashCount
                )
                return true
            } else {
                sp.edit().putInt(currentVersion, fastCrashCount).commit()
                TinkerLog.e(TAG, "tinker has fast crash %d times", fastCrashCount)
            }
        }
        return false
    }

    companion object {
        private const val TAG = "Tinker.SampleUncaughtExHandler"
        private const val QUICK_CRASH_ELAPSE = 10 * 1000.toLong()
        const val MAX_CRASH_COUNT = 3
        private const val DALVIK_XPOSED_CRASH =
            "Class ref in pre-verified class resolved to unexpected implementation"
    }
}