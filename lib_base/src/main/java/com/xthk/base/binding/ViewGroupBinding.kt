package com.xthk.core.binding

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

/**
 * 添加view进viewgroup
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_addViews")
fun addViews(viewGroup: ViewGroup, views: List<View>) {
    viewGroup.removeAllViews()
    for (view in views) {
        viewGroup.addView(view)
    }
}


/**
 * 设置paddingleft
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_paddingleft")
fun setPaddingLeft(view: View, px: Int) {
    if (px == null || px == 0) {
        return
    }
    view.setPadding(px, view.paddingTop, view.paddingRight, view.paddingBottom)
}

/**
 * 设置paddingleft
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_paddingright")
fun setPaddingRight(view: View, px: Int) {
    if (px == null || px == 0) {
        return
    }
    view.setPadding(view.paddingLeft, view.paddingTop, px, view.paddingBottom)
}

/**
 * 设置paddingleft
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_paddingtop")
fun setPaddingTop(view: View, px: Int) {
    if (px == null || px == 0) {
        return
    }
    view.setPadding(view.paddingLeft, px, view.paddingRight, view.paddingBottom)
}

/**
 * 设置paddingleft
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_paddingbottom")
fun setPaddingBottom(view: View, px: Int) {
    if (px == null || px == 0) {
        return
    }
    view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, px)
}

