package com.xthk.base.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout

@BindingAdapter("bind_offsetChangedListener")
fun bindOnOffsetChangedListener(
    appBarLayout: AppBarLayout,
    listener: AppBarLayout.OnOffsetChangedListener?
) {
    listener?.apply {
        appBarLayout.addOnOffsetChangedListener(this)
    }
}

@BindingAdapter("bind_enable_scroll")
fun bindOnEnableScroll(appBarLayout: AppBarLayout, enable: Boolean?) {
    if (enable == null) {
        return
    }
    appBarLayout.enableScroll(enable)
}


/**
 * 确定AppBarLayout是否可滑动
 */
fun AppBarLayout.enableScroll(enable: Boolean) {
    if (childCount == 0) {
        return
    }
    val mAppBarChildAt = getChildAt(0)
    val mAppBarParams = mAppBarChildAt.layoutParams as AppBarLayout.LayoutParams
    if (enable) {
        mAppBarParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        mAppBarChildAt.layoutParams = mAppBarParams
    } else {
        mAppBarParams.scrollFlags = 0
    }
}