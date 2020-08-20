package com.bestbrand.lib_xtsize.delegate

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.bestbrand.lib_xtsize.IXtView
import com.bestbrand.lib_xtsize.R
import com.bestbrand.lib_xtsize.XtScreenAdapter


/**
 * Author:XingWei
 * Time:2020/3/30 17:34
 * Desc: view属性代理
 */
open class XtViewDelegate constructor(protected val view: View) : IXtView {
    companion object {
        const val XT_NO_VALUE = Int.MIN_VALUE
    }

    var adapter: XtScreenAdapter = XtScreenAdapter.instance
    private var xtWidth = XT_NO_VALUE
    private var xtHeight = XT_NO_VALUE
    private var xtPaddingLeft = XT_NO_VALUE
    private var xtPaddingTop = XT_NO_VALUE
    private var xtPaddingRight = XT_NO_VALUE
    private var xtPaddingBottom = XT_NO_VALUE
    private var xtMarginLeft = XT_NO_VALUE
    private var xtMarginTop = XT_NO_VALUE
    private var xtMarginRight = XT_NO_VALUE
    private var xtMarginBottom = XT_NO_VALUE

    open fun initAttributes(context: Context, attrs: AttributeSet?) {
        XtScreenAdapter.instance.init(context.resources.displayMetrics)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.XtView)
        try {
            xtWidth = typedArray.getInt(R.styleable.XtView_xt_layout_width, XT_NO_VALUE)
            xtHeight = typedArray.getInt(R.styleable.XtView_xt_layout_height, XT_NO_VALUE)

            val padding: Int = typedArray.getInt(R.styleable.XtView_xt_padding, XT_NO_VALUE)

            xtPaddingLeft = typedArray.getInt(R.styleable.XtView_xt_paddingLeft, padding)
            xtPaddingTop = typedArray.getInt(R.styleable.XtView_xt_paddingTop, padding)
            xtPaddingRight = typedArray.getInt(R.styleable.XtView_xt_paddingRight, padding)
            xtPaddingBottom = typedArray.getInt(R.styleable.XtView_xt_paddingBottom, padding)

            val margin: Int = typedArray.getInt(R.styleable.XtView_xt_layout_margin, XT_NO_VALUE)

            xtMarginLeft = typedArray.getInt(R.styleable.XtView_xt_layout_marginLeft, margin)
            xtMarginTop = typedArray.getInt(R.styleable.XtView_xt_layout_marginTop, margin)
            xtMarginRight = typedArray.getInt(R.styleable.XtView_xt_layout_marginRight, margin)
            xtMarginBottom = typedArray.getInt(R.styleable.XtView_xt_layout_marginBottom, margin)
        } finally {
            typedArray.recycle()
        }
    }

    open fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if (params == null)
            throw NullPointerException("Layout parameters cannot be null")
        setXtWidth(params, xtWidth)
        setXtHeight(params, xtHeight)
        setXtMarginLeft(params, xtMarginLeft)
        setXtMarginTop(params, xtMarginTop)
        setXtMarginRight(params, xtMarginRight)
        setXtMarginBottom(params, xtMarginBottom)
        setXtPadding(xtPaddingLeft, xtPaddingTop, xtPaddingRight, xtPaddingBottom)
    }

    private fun setXtMarginBottom(params: ViewGroup.LayoutParams, marginBottom: Int) {
        if (marginBottom == XT_NO_VALUE) return
        if (params is ViewGroup.MarginLayoutParams) {
            params.bottomMargin = adapter.scaleY(marginBottom)
        }
    }

    private fun setXtMarginRight(params: ViewGroup.LayoutParams, marginRight: Int) {
        if (marginRight == XT_NO_VALUE) return
        if (params is ViewGroup.MarginLayoutParams) {
            params.rightMargin = adapter.scaleX(marginRight)
        }
    }

    private fun setXtMarginTop(params: ViewGroup.LayoutParams, marginTop: Int) {
        if (marginTop == XT_NO_VALUE) return
        if (params is ViewGroup.MarginLayoutParams) {
            params.topMargin = adapter.scaleY(marginTop)
        }
    }

    private fun setXtMarginLeft(params: ViewGroup.LayoutParams, marginLeft: Int) {
        if (marginLeft == XT_NO_VALUE) return
        if (params is ViewGroup.MarginLayoutParams) {
            params.leftMargin = adapter.scaleX(marginLeft)
        }
    }

    private fun setXtHeight(params: ViewGroup.LayoutParams, height: Int) {
        if (height == XT_NO_VALUE) {
            return
        }
        if (XtScreenAdapter.WRAP != height && XtScreenAdapter.MATCH != height) {
            params.height = adapter.scaleY(height)
        }
    }

    private fun setXtWidth(params: ViewGroup.LayoutParams, width: Int) {
        if (width == XT_NO_VALUE) {
            return
        }
        if (XtScreenAdapter.WRAP != width && XtScreenAdapter.MATCH != width) {
            params.width = adapter.scaleX(width)
        }
    }

    override fun setXtSize(width: Int, height: Int) {
        setXtWidth(width)
        setXtHeight(height)
    }

    override fun getXtWidth(): Int {
        return xtWidth
    }

    override fun setXtWidth(width: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtWidth = width
        } else {
            setXtWidth(layoutParams, width)
        }
    }

    override fun getXtHeight(): Int {
        return xtHeight
    }

    override fun setXtHeight(height: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtHeight = height
        } else {
            setXtHeight(layoutParams, height)
        }
    }

    override fun setXtPadding(padding: Int) {
        setXtPadding(padding, padding, padding, padding)
    }

    override fun setXtPadding(left: Int, top: Int, right: Int, bottom: Int) {
        setXtPaddingLeft(left)
        setXtPaddingTop(top)
        setXtPaddingRight(right)
        setXtPaddingBottom(bottom)
    }

    override fun getXtPaddingLeft(): Int {
        return xtPaddingLeft
    }

    override fun setXtPaddingLeft(paddingLeft: Int) {
        if (paddingLeft == XT_NO_VALUE) return
        view.setPadding(
            adapter.scaleX(paddingLeft),
            view.paddingTop,
            view.paddingRight,
            view.paddingBottom
        )
    }

    override fun getXtPaddingTop(): Int {
        return xtPaddingTop
    }

    override fun setXtPaddingTop(paddingTop: Int) {
        if (paddingTop == XT_NO_VALUE) return
        view.setPadding(
            view.paddingLeft,
            adapter.scaleY(paddingTop),
            view.paddingRight,
            view.paddingBottom
        )
    }

    override fun getXtPaddingRight(): Int {
        return xtPaddingRight
    }

    override fun setXtPaddingRight(paddingRight: Int) {
        if (paddingRight == XT_NO_VALUE) return
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            adapter.scaleX(paddingRight),
            view.paddingBottom
        )
    }

    override fun getXtPaddingBottom(): Int {
        return xtPaddingBottom
    }

    override fun setXtPaddingBottom(paddingBottom: Int) {
        if (paddingBottom == XT_NO_VALUE) return
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            adapter.scaleY(paddingBottom)
        )
    }

    override fun setXtMargin(margin: Int) {
        setXtMargin(margin, margin, margin, margin)
    }

    override fun setXtMargin(left: Int, top: Int, right: Int, bottom: Int) {
        setXtMarginLeft(left)
        setXtMarginTop(top)
        setXtMarginRight(right)
        setXtMarginBottom(bottom)
    }

    override fun getXtMarginLeft(): Int {
        return xtMarginLeft
    }

    override fun setXtMarginLeft(marginLeft: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtMarginLeft = marginLeft
        } else {
            setXtMarginLeft(layoutParams, marginLeft)
        }
    }

    override fun getXtMarginTop(): Int {
        return xtMarginTop
    }

    override fun setXtMarginTop(marginTop: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtMarginTop = marginTop
        } else {
            setXtMarginTop(layoutParams, marginTop)
        }
    }

    override fun getXtMarginRight(): Int {
        return xtMarginRight
    }

    override fun setXtMarginRight(marginRight: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtMarginRight = marginRight
        } else {
            setXtMarginRight(layoutParams, marginRight)
        }
    }

    override fun getXtMarginBottom(): Int {
        return xtMarginBottom
    }

    override fun setXtMarginBottom(marginBottom: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams == null) {
            this.xtMarginBottom = marginBottom
        } else {
            setXtMarginBottom(layoutParams, marginBottom)
        }

    }
}