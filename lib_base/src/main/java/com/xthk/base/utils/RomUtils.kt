package com.xthk.base.utils

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.util.*


/**
 * Author:XingWei
 * Time:2020/4/1 15:27
 * Desc:
 */
object RomUtils {
     const val MIUI = 1
     const val FLYME = 2
     const val ANDROID_NATIVE = 3
     const val NA = 4

    fun getLightStatusBarAvailableRomType(): Int {
        if (isFlymeV4OrAbove()) {
            return FLYME
        }
        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错
        if (isMiUIV7OrAbove()) {
            return ANDROID_NATIVE
        }
        if (isMiUIV6OrAbove()) {
            return MIUI
        }
        return if (isAndroidMOrAbove()) {
            ANDROID_NATIVE
        } else NA
    }

    //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
    //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
    private fun isFlymeV4OrAbove(): Boolean {
        val displayId = Build.DISPLAY
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            val displayIdArray = displayId.split(" ").toTypedArray()
            for (temp in displayIdArray) { //版本号4以上，形如4.x.
                if (temp.matches(Regex("^[4-9]\\.(\\d+\\.)+\\S*"))) {
                    return true
                }
            }
        }
        return false
    }

    //Android Api 23以上
    private fun isAndroidMOrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"

    private fun isMiUIV6OrAbove(): Boolean {
        return try {
            val properties = Properties()
            properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
            val uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null)
            if (uiCode != null) {
                val code = uiCode.toInt()
                code >= 4
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun isMiUIV7OrAbove(): Boolean {
        return try {
            val properties = Properties()
            properties.load(
                FileInputStream(
                    File(
                        Environment.getRootDirectory(),
                        "build.prop"
                    )
                )
            )
            val uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null)
            if (uiCode != null) {
                val code = uiCode.toInt()
                code >= 5
            } else {
                false
            }
        } catch (e: java.lang.Exception) {
            false
        }
    }
}