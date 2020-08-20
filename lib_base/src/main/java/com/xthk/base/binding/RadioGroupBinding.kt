package com.xthk.base.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter

/**
 * [View]设置选择监听器
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_onCheckListener_rg")
fun setOnLongClickEvent(view: RadioGroup, checkedChangeListener: RadioGroup.OnCheckedChangeListener) {
    view.setOnCheckedChangeListener(checkedChangeListener)
}