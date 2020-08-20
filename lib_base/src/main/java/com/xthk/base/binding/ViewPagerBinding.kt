package com.xthk.base.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


@BindingAdapter("bind_viewPager_adapter")
fun setViewPagerAdapter(
    viewPager: ViewPager2,
    adapter: FragmentStateAdapter?
) {
    adapter?.apply {
        viewPager.adapter = this
    }
}

@BindingAdapter("bind_viewPager_currentItem")
fun setViewPagerCurrentItem(
    viewPager: ViewPager2,
    currentItem: Int?
) {
    currentItem?.apply {
        viewPager.currentItem = this
    }
}
