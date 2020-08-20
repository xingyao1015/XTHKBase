package com.xthk.core.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.databinding.BindingAdapter

/**
 * [View]设置选择监听器
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_onCheckListener_checkbox")
fun setOncheckListenerByCheckBox(
    view: CheckBox,
    checkedChangeListener: CompoundButton.OnCheckedChangeListener?
) {
    checkedChangeListener?.apply {
        view.setOnCheckedChangeListener(this)
    }
}
