package com.xthk.core.binding

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * 设置中划线
 */
@BindingAdapter("bind_textView_mediumLine")
fun setTextViewMediumLine(view: TextView, render: Boolean = false) {
    when (render) {
        true -> view.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        false -> view.paint.flags = 0
    }
    view.paint.isAntiAlias = true
}


/**
 * 设置是否加粗
 */
@BindingAdapter("bind_textView_style_with_bold")
fun setTextViewStyleWithBold(view: TextView, isBold: Boolean) {
    view.paint.isFakeBoldText = isBold
    view.paint.isAntiAlias = true
    view.invalidate()
}


fun TextView.isFakeBoldText(isBold: Boolean){
    paint.isFakeBoldText = isBold
    paint.isAntiAlias = true
    invalidate()
}