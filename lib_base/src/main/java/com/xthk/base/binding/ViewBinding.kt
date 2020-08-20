package com.xthk.base.binding

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.util.Consumer
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.xthk.base.utils.GlideApp
import com.xthk.base.utils.ThrottleClickUtil

interface ViewClickConsumer : Consumer<View>

const val DEFAULT_THROTTLE_TIME = 1500L

/**
 * [View]是否可见
 *
 * @param visible 值为true时可见
 */
@BindingAdapter("bind_visibility")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * [View]是否可见
 *
 * @param visible 值为true时可见 否则不显示
 */
@BindingAdapter("bind_visibility_invisible")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}


/**
 * [View]防抖动点击事件
 *
 * @param consumer 点击事件消费者
 * @param time 防抖动时间间隔，单位ms
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_onClick")
fun setOnClickEvent(view: View, consumer: ViewClickConsumer) {
    ThrottleClickUtil.clickWithThrottleFirst(view, consumer)
}


/**
 * [View]被点击时,关闭输入框
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_onClick_closeSoftInput")
fun closeSoftInputWhenClick(view: View, activity: FragmentActivity? = null) {
    if (activity == null) {
        return
    }
    view.setOnClickListener {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}




