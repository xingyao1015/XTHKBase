package com.bestbrand.lib_xtsize

import android.graphics.drawable.Drawable
/**
 * Author:XingWei
 * Time:2020/3/30 17:29
 * Desc:
 */
interface IXtTextView {
    fun setXtTextSize(textSize: Int)

    fun setXtMaxWidth(maxWidth: Int)

    fun setXtMaxHeight(maxHeight: Int)

    fun setXtDrawableLeft(
        drawable: Drawable?,
        padding: Int,
        width: Int,
        height: Int
    )

    fun setXtDrawableTop(
        drawable: Drawable?,
        padding: Int,
        width: Int,
        height: Int
    )

    fun setXtDrawableRight(
        drawable: Drawable?,
        padding: Int,
        width: Int,
        height: Int
    )

    fun setXtDrawableBottom(
        drawable: Drawable?,
        padding: Int,
        width: Int,
        height: Int
    )
}