package com.bestbrand.lib_xtsize.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import com.bestbrand.lib_xtsize.IXtTextView
import com.bestbrand.lib_xtsize.IXtView
import com.bestbrand.lib_xtsize.delegate.XtTextViewDelegate

/**
 * XingWei
 * 2020/3/31
 */
class XtRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr), IXtView, IXtTextView {
    private val delegate = XtTextViewDelegate(this)

    init {
        delegate.initAttributes(context, attrs)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        delegate.setLayoutParams(params)
        super.setLayoutParams(params)
    }

    override fun setXtSize(width: Int, height: Int) {
        delegate.setXtSize(width, height)
    }

    override fun getXtWidth(): Int {
        return delegate.getXtWidth()
    }

    override fun setXtWidth(width: Int) {
        delegate.setXtWidth(width)
    }

    override fun getXtHeight(): Int {
        return delegate.getXtHeight()
    }

    override fun setXtHeight(height: Int) {
        delegate.setXtHeight(height)
    }

    override fun setXtPadding(padding: Int) {
        delegate.setXtPadding(padding)
    }

    override fun setXtPadding(left: Int, top: Int, right: Int, bottom: Int) {
        delegate.setXtPadding(left, top, right, bottom)
    }

    override fun getXtPaddingLeft(): Int {
        return delegate.getXtPaddingLeft()
    }

    override fun setXtPaddingLeft(paddingLeft: Int) {
        delegate.setXtPaddingLeft(paddingLeft)
    }

    override fun getXtPaddingTop(): Int {
        return delegate.getXtPaddingTop()
    }

    override fun setXtPaddingTop(paddingTop: Int) {
        delegate.setXtPaddingTop(paddingTop)
    }

    override fun getXtPaddingRight(): Int {
        return delegate.getXtPaddingRight()
    }

    override fun setXtPaddingRight(paddingRight: Int) {
        delegate.setXtPaddingRight(paddingRight)
    }

    override fun getXtPaddingBottom(): Int {
        return delegate.getXtPaddingBottom()
    }

    override fun setXtPaddingBottom(paddingBottom: Int) {
        delegate.setXtPaddingBottom(paddingBottom)
    }

    override fun setXtMargin(margin: Int) {
        delegate.setXtMargin(margin)
    }

    override fun setXtMargin(left: Int, top: Int, right: Int, bottom: Int) {
        delegate.setXtMargin(left, top, right, bottom)
    }

    override fun getXtMarginLeft(): Int {
        return delegate.getXtMarginLeft()
    }

    override fun setXtMarginLeft(marginLeft: Int) {
        delegate.setXtMarginLeft(marginLeft)
    }

    override fun getXtMarginTop(): Int {
        return delegate.getXtMarginTop()
    }

    override fun setXtMarginTop(marginTop: Int) {
        delegate.setXtMarginTop(marginTop)
    }

    override fun getXtMarginRight(): Int {
        return delegate.getXtMarginRight()
    }

    override fun setXtMarginRight(marginRight: Int) {
        delegate.setXtMarginRight(marginRight)
    }

    override fun getXtMarginBottom(): Int {
        return delegate.getXtMarginBottom()
    }

    override fun setXtMarginBottom(marginBottom: Int) {
        delegate.setXtMarginBottom(marginBottom)
    }

    override fun setXtTextSize(textSize: Int) {
        delegate.setXtTextSize(textSize)
    }

    override fun setXtMaxWidth(maxWidth: Int) {
        delegate.setXtMaxWidth(maxWidth)
    }

    override fun setXtMaxHeight(maxHeight: Int) {
        delegate.setXtMaxHeight(maxHeight)
    }

    override fun setXtDrawableLeft(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        delegate.setXtDrawableLeft(drawable, padding, width, height)
    }

    override fun setXtDrawableTop(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        delegate.setXtDrawableTop(drawable, padding, width, height)
    }

    override fun setXtDrawableRight(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        delegate.setXtDrawableRight(drawable, padding, width, height)
    }

    override fun setXtDrawableBottom(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        delegate.setXtDrawableBottom(drawable, padding, width, height)
    }
}