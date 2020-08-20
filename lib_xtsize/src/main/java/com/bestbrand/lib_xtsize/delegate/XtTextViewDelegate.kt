package com.bestbrand.lib_xtsize.delegate

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bestbrand.lib_xtsize.IXtTextView
import com.bestbrand.lib_xtsize.R


/**
 * Author:XingWei
 * Time:2020/3/30 17:33
 * Desc: textView代理
 */
class XtTextViewDelegate constructor(view: View) : XtViewDelegate(view), IXtTextView {
    private var xtTextSize = 0
    private var drawableWidth = 0
    private var drawableHeight = 0
    private var drawablePadding = 0
    private var drawableLeft: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableRight: Drawable? = null
    private var drawableBottom: Drawable? = null
    private var xtMaxWidth = 0
    private var xtMaxHeight = 0

    override fun initAttributes(context: Context, attrs: AttributeSet?) {
        super.initAttributes(context, attrs)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.XtView)
        try {
            xtTextSize = typedArray.getInt(R.styleable.XtView_xt_textSize, XT_NO_VALUE)
            drawableWidth = typedArray.getInt(R.styleable.XtView_xt_drawableWidth, XT_NO_VALUE)
            drawableHeight = typedArray.getInt(R.styleable.XtView_xt_drawableHeight, XT_NO_VALUE)
            drawablePadding = typedArray.getInt(R.styleable.XtView_xt_drawablePadding, XT_NO_VALUE)
            drawableLeft = typedArray.getDrawable(R.styleable.XtView_android_drawableLeft)
            drawableTop = typedArray.getDrawable(R.styleable.XtView_android_drawableTop)
            drawableRight = typedArray.getDrawable(R.styleable.XtView_android_drawableRight)
            drawableBottom = typedArray.getDrawable(R.styleable.XtView_android_drawableBottom)
            xtMaxWidth = typedArray.getInt(R.styleable.XtView_xt_layout_max_width, XT_NO_VALUE)
            xtMaxHeight = typedArray.getInt(R.styleable.XtView_xt_layout_max_height, XT_NO_VALUE)
        } finally {
            typedArray.recycle()
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        if (view is TextView) {
            setXtTextSize(xtTextSize)
            if (drawableLeft != null) {
                setXtDrawableLeft(drawableLeft, drawablePadding, drawableWidth, drawableHeight)
            }
            if (drawableTop != null) {
                setXtDrawableTop(drawableTop, drawablePadding, drawableWidth, drawableHeight)
            }
            if (drawableRight != null) {
                setXtDrawableRight(drawableRight, drawablePadding, drawableWidth, drawableHeight)
            }
            if (drawableBottom != null) {
                setXtDrawableBottom(drawableBottom, drawablePadding, drawableWidth, drawableHeight)
            }
            setXtMaxWidth(xtMaxWidth)
            setXtMaxHeight(xtMaxHeight)
        }
    }


    override fun setXtTextSize(textSize: Int) {
        if (textSize == XT_NO_VALUE) return
        if (view is TextView) {
            adapter.scaleTextSize(view, textSize.toFloat())
        }
    }

    override fun setXtMaxWidth(maxWidth: Int) {
        if (maxWidth == XT_NO_VALUE) return
        if (view is TextView) {
            view.maxWidth = adapter.scaleX(maxWidth)
        }
    }

    override fun setXtMaxHeight(maxHeight: Int) {
        if (maxHeight == XT_NO_VALUE) return
        if (view is TextView) {
            view.maxHeight = adapter.scaleY(maxHeight)
        }
    }

    override fun setXtDrawableLeft(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        if (padding == XT_NO_VALUE || width == XT_NO_VALUE || height == XT_NO_VALUE) return
        drawable?.apply {
            setBounds(0, 0, adapter.scaleX(width), adapter.scaleY(height))
        }
        if (view is TextView) {
            view.apply {
                compoundDrawablePadding = adapter.scaleX(padding)
                setCompoundDrawables(drawable, null, null, null)
            }
        }
    }

    override fun setXtDrawableTop(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        if (padding == XT_NO_VALUE || width == XT_NO_VALUE || height == XT_NO_VALUE) return
        drawable?.apply {
            setBounds(0, 0, adapter.scaleX(width), adapter.scaleY(height))
        }
        if (view is TextView) {
            view.apply {
                compoundDrawablePadding = adapter.scaleY(padding)
                setCompoundDrawables(null, drawable, null, null)
            }
        }
    }

    override fun setXtDrawableRight(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        if (padding == XT_NO_VALUE || width == XT_NO_VALUE || height == XT_NO_VALUE) return
        drawable?.apply {
            setBounds(0, 0, adapter.scaleX(width), adapter.scaleY(height))
        }
        if (view is TextView) {
            view.apply {
                compoundDrawablePadding = adapter.scaleX(padding)
                setCompoundDrawables(null, null, drawable, null)
            }
        }
    }

    override fun setXtDrawableBottom(drawable: Drawable?, padding: Int, width: Int, height: Int) {
        if (padding == XT_NO_VALUE || width == XT_NO_VALUE || height == XT_NO_VALUE) return
        drawable?.apply {
            setBounds(0, 0, adapter.scaleX(width), adapter.scaleY(height))
        }
        if (view is TextView) {
            view.apply {
                compoundDrawablePadding = adapter.scaleY(padding)
                setCompoundDrawables(null, null, null, drawable)
            }
        }
    }
}