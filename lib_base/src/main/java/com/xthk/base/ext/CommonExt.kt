package com.xthk.base.ext

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebStorage
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xthk.base.BaseApp

fun getColorFromRes(@ColorRes int: Int): Int {
    return ContextCompat.getColor(BaseApp.CONTEXT, int)
}

fun getDrawableFromRes(@DrawableRes int: Int): Drawable? {
    return ContextCompat.getDrawable(BaseApp.CONTEXT, int)
}

fun getStringFromRes(@StringRes id: Int): String {
    return BaseApp.CONTEXT.getString(id)
}


fun fromM() = fromSpecificVersion(Build.VERSION_CODES.M)
fun beforeM() = beforeSpecificVersion(Build.VERSION_CODES.M)
fun fromN() = fromSpecificVersion(Build.VERSION_CODES.N)
fun beforeN() = beforeSpecificVersion(Build.VERSION_CODES.N)
fun fromO() = fromSpecificVersion(Build.VERSION_CODES.O)
fun beforeO() = beforeSpecificVersion(Build.VERSION_CODES.O)

fun fromL() = fromSpecificVersion(Build.VERSION_CODES.LOLLIPOP)
fun beforeL() = beforeSpecificVersion(Build.VERSION_CODES.LOLLIPOP)

fun fromK() = fromSpecificVersion(Build.VERSION_CODES.KITKAT)
fun beforeK() = beforeSpecificVersion(Build.VERSION_CODES.KITKAT)

fun fromSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT >= version
fun beforeSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT < version
