package com.xthk.base.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.xthk.base.ext.getColorFromRes
import com.xthk.base.utils.RomUtils.getLightStatusBarAvailableRomType
import java.lang.reflect.Field

/**
 * Author:XingWei
 * Time:2020/4/1 15:26
 * Desc:
 */
object StatusBarUtil {
    private var DEVICE_FIRM = -1

    /**
     * 修改状态栏为全透明
     */
    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window: Window = activity.window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    /**
     * 修改状态栏颜色
     */
    fun setStatusBarColor(activity: Activity, colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getColorFromRes(colorRes)
        }
    }

    /**
     * @param dark       true 字体颜色为黑色，false为白色
     * @param isFullMode 是否在全屏模式下
     */
    fun setLightStatusBar(
        activity: Activity,
        dark: Boolean,
        isFullMode: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (DEVICE_FIRM == -1) {
                DEVICE_FIRM = getLightStatusBarAvailableRomType()
            }
            when (DEVICE_FIRM) {
                RomUtils.MIUI -> MIUISetStatusBarLightMode(activity, dark)
                RomUtils.FLYME -> if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    setAndroidNativeLightStatusBar(activity, dark, isFullMode)
                } else {
                    setFlymeLightStatusBar(activity, dark)
                }
                RomUtils.ANDROID_NATIVE -> setAndroidNativeLightStatusBar(
                    activity,
                    dark,
                    isFullMode
                )
                RomUtils.NA -> {
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setLightStatusBar(
        window: Window,
        dark: Boolean,
        isFullMode: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when (getLightStatusBarAvailableRomType()) {
                RomUtils.MIUI -> MIUISetStatusBarLightMode(window, dark)
                RomUtils.FLYME -> if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    setAndroidNativeLightStatusBar(window, dark, isFullMode)
                } else {
                    setFlymeLightStatusBar(window, dark)
                }
                RomUtils.ANDROID_NATIVE -> setAndroidNativeLightStatusBar(
                    window,
                    dark,
                    isFullMode
                )
                RomUtils.NA -> {
                }
            }
        }
    }

    private fun setFlymeLightStatusBar(obj: Any, dark: Boolean): Boolean {
        var result = false
        var window: Window? = null
        if (obj is Activity) {
            window = obj.window
        } else if (obj is Window) {
            window = obj
        }
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit: Int = darkFlag.getInt(null)
                var value: Int = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (ignore: java.lang.Exception) {
            }
        }
        return result
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setAndroidNativeLightStatusBar(
        obj: Any,
        dark: Boolean,
        isFullMode: Boolean
    ) {
        var decor: View? = null
        if (obj is Activity) {
            decor = obj.window.decorView
        } else if (obj is Window) {
            decor = obj.decorView
        }
        if (decor == null) {
            return
        }
        if (dark) {
            if (isFullMode) {
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            if (isFullMode) {
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            } else {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    /**
     * 获得通知栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>?
        val obj: Any?
        val field: Field?
        val x :Int
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field.get(obj).toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    /**
     * 需要MIUIV6以上
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun MIUISetStatusBarLightMode(
        `object`: Any,
        dark: Boolean
    ) {
        var window: Window? = null
        if (`object` is Activity) {
            window = `object`.window
        } else if (`object` is Window) {
            window = `object`
        }
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove()) { //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    }
                }
            } catch (ignore: Exception) {
            }
        }
    }

}