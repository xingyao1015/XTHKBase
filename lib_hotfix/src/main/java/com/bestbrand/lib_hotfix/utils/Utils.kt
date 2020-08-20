package com.bestbrand.lib_hotfix.utils

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Environment
import android.os.StatFs
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareConstants
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


/**
 * @author XingWei
 * @time 2020/7/27
 */
object Utils {
    private const val TAG = "Tinker.Utils"
    private const val ERROR_PATCH_ROM_SPACE = -21
    private const val ERROR_PATCH_MEMORY_LIMIT = -22
    const val ERROR_PATCH_CRASH_LIMIT = -23

    //App 退到后台
    const val APP_TO_BACKGROUND = "APP_TO_BACKGROUND"

    //最小需要的堆大小
    private const val MIN_MEMORY_HEAP_SIZE = 45


    fun isBackground(context: Context): Boolean {
        val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
                    TinkerLog.i(TAG, String.format("Background App: %s", appProcess.processName))
                    true
                } else {
                    TinkerLog.i(TAG, String.format("Foreground App: %s", appProcess.processName))
                    false
                }
            }
        }
        return false
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp) {
                    continue
                }
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        val isIPv4 = hostAddress.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return hostAddress
                        } else {
                            if (!isIPv4) {
                                val index = hostAddress.indexOf('%')
                                return if (index < 0) hostAddress.toUpperCase(Locale.getDefault()) else hostAddress.substring(
                                    0,
                                    index
                                ).toUpperCase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return null
    }

    fun isXposedExists(thr: Throwable): Boolean {
        val stackTraces = thr.stackTrace
        for (stackTrace in stackTraces) {
            val clazzName = stackTrace.className
            if (clazzName != null && clazzName.contains("de.robv.android.xposed.XposedBridge")) {
                return true
            }
        }
        return false
    }

    fun checkForPatchRecover(roomSize: Long, maxMemory: Int): Int {
        if (maxMemory < MIN_MEMORY_HEAP_SIZE) {
            return ERROR_PATCH_MEMORY_LIMIT
        }
        return if (!checkRomSpaceEnough(roomSize)) {
            // 内存不足 这里可以提示用户清理空间
            ERROR_PATCH_ROM_SPACE
        } else {
            ShareConstants.ERROR_PATCH_OK
        }
    }

    private fun checkRomSpaceEnough(limitSize: Long): Boolean {
        var allSize: Long
        var availableSize: Long = 0
        try {
            val data = Environment.getDataDirectory()
            val sf = StatFs(data.path)
            availableSize =
                sf.availableBlocksLong * sf.blockSizeLong
            allSize = sf.blockCountLong * sf.blockSizeLong
        } catch (e: Exception) {
            allSize = 0
        }
        return allSize != 0L && availableSize > limitSize
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    fun getAppVersionName(context: Context): String? {
        return getAppVersionName(context, context.packageName)
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    private fun getAppVersionName(
        context: Context,
        packageName: String?
    ): String? {
        return if (isSpace(packageName)) null else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    class ScreenState(
        context: Context,
        onScreenOffInterface: IOnScreenOff?
    ) {
        interface IOnScreenOff {
            fun onScreenOff()
        }

        init {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(APP_TO_BACKGROUND)
            filter.addAction(ACTION_CLOSE_SYSTEM_DIALOGS)
            context.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = if (intent == null) "" else intent.action
                    TinkerLog.i(TAG, "ScreenReceiver action [%s] ", action)
                    if (Intent.ACTION_SCREEN_OFF == action || APP_TO_BACKGROUND == action) {
                        onScreenOffInterface?.onScreenOff()
                    }
                    context?.unregisterReceiver(this)
                }
            }, filter)
        }
    }
}