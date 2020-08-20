package com.xthk.base

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide

class BaseApp : Application() {

    lateinit var app:Application

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        fix()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this.app).clearMemory()
        }
        Glide.get(this.app).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this.app).clearMemory()
    }


    /**
     *
     * java.util.concurrent.TimeoutException: android.os.BinderProxy.finalize()
     * timed out after 120 seconds
     * 解决部分手机出现的该问题
     */
    private fun fix() {
        try {
            val cls = Class.forName("java.lang.Daemons\$FinalizerWatchdogDaemon")
            val method = cls.superclass?.getDeclaredMethod("stop")
            method?.isAccessible = true

            val field = cls.getDeclaredField("INSTANCE")
            field.isAccessible = true

            method?.invoke(field.get(null))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 获取进程名
     */
    private fun getProcessName(cxt: Context?): String? {
        if (cxt == null) return ""
        val pid = Process.myPid()
        val am =
            cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

    companion object {
        lateinit var CONTEXT: Context
    }


}