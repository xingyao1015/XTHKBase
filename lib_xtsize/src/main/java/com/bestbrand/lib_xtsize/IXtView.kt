package com.bestbrand.lib_xtsize

/**
 * Author:XingWei
 * Time:2020/3/30 17:31
 * Desc:
 */
interface IXtView {
    fun setXtSize(width: Int, height: Int)

    fun getXtWidth(): Int

    fun setXtWidth(width: Int)

    fun getXtHeight(): Int

    fun setXtHeight(height: Int)

    fun setXtPadding(padding: Int)

    fun setXtPadding(left: Int, top: Int, right: Int, bottom: Int)

    fun getXtPaddingLeft(): Int

    fun setXtPaddingLeft(paddingLeft: Int)

    fun getXtPaddingTop(): Int

    fun setXtPaddingTop(paddingTop: Int)

    fun getXtPaddingRight(): Int

    fun setXtPaddingRight(paddingRight: Int)

    fun getXtPaddingBottom(): Int

    fun setXtPaddingBottom(paddingBottom: Int)

    fun setXtMargin(margin: Int)

    fun setXtMargin(left: Int, top: Int, right: Int, bottom: Int)

    fun getXtMarginLeft(): Int

    fun setXtMarginLeft(marginLeft: Int)

    fun getXtMarginTop(): Int

    fun setXtMarginTop(marginTop: Int)

    fun getXtMarginRight(): Int

    fun setXtMarginRight(marginRight: Int)

    fun getXtMarginBottom(): Int

    fun setXtMarginBottom(marginBottom: Int)
}