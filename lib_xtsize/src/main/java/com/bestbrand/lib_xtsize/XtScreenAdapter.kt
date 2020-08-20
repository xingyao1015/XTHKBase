package com.bestbrand.lib_xtsize

import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import kotlin.math.roundToInt

/**
 * Author:XingWei
 * Time:2020/3/30 16:40
 * Desc:大小适配器
 */
class XtScreenAdapter private constructor() {

    private var defaultWidth = 0
    private var defaultHeight = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private var isInit = false

    fun init(displayMetrics: DisplayMetrics) {
        if (isInit.not()) {
            isInit = true
            reset(displayMetrics)
        }
    }

    private fun reset(displayMetrics: DisplayMetrics) {
        if (screenWidth == 0) {
            screenWidth = displayMetrics.widthPixels
        }
        if (screenHeight == 0) {
            screenHeight = when (displayMetrics.heightPixels) {
                672 -> {
                    720
                }
                1008 -> {
                    1080
                }
                else -> {
                    displayMetrics.heightPixels
                }
            }
        }
        if (defaultWidth == 0) {
            defaultWidth = if (screenWidth > screenHeight) 1920 else 1080
        }
        if (defaultHeight == 0) {
            defaultHeight = if (screenWidth > screenHeight) 1080 else 1920
        }
    }

    /**
     * 字体适配（只适配TextView或其子类）
     */
    fun scaleTextSize(view: View, size: Float) {
        if (view is TextView) {
            val txtSize = (size / defaultHeight * screenHeight)
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize)
        }
    }

    /**
     * @return !=0
     */
    fun scaleX(x: Int): Int {
        var scaleX = (x * screenWidth / defaultWidth.toFloat()).roundToInt()
        if (scaleX == 0 && x != 0) {
            scaleX = if (x < 0) -1 else 1
        }
        return scaleX
    }


    /**
     * @return !=0
     */
    fun scaleY(y: Int): Int {
        var scaleY = (y * screenWidth / defaultWidth.toFloat()).roundToInt()
        if (scaleY == 0 && y != 0) {
            scaleY = if (y < 0) -1 else 1
        }
        return scaleY
    }

    /**
     * 适配控件（动态添加的控件先要自己添加layoutParams）
     */
    fun scaleView(view: View, w: Int, h: Int, l: Int, t: Int, r: Int, b: Int) {
        var mWidth = w
        if (WRAP != mWidth && MATCH != mWidth) {
            mWidth = scaleX(mWidth)
        }
        var mHeight = h
        if (WRAP != mHeight && MATCH != mHeight) {
            mHeight = scaleY(mHeight)
        }
        val ll = scaleX(l)
        val tt = scaleY(t)
        val rr = scaleX(r)
        val bb = scaleY(b)
        val layoutParams = view.layoutParams
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            width = mWidth
            height = mHeight
            setMargins(scaleX(ll), scaleY(tt), scaleX(rr), scaleY(bb))
        }
    }


    private object Holder {
        val instance = XtScreenAdapter()
    }

    companion object {
        const val WRAP = RelativeLayout.LayoutParams.WRAP_CONTENT
        const val MATCH = RelativeLayout.LayoutParams.MATCH_PARENT
        val instance = Holder.instance
    }
}