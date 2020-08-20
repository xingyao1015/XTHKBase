package com.xthk.base.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.SeekBar
import androidx.databinding.BindingAdapter

/**
 * [View]设置seekBar监听
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_seekChangeListener")
fun setOnLongClickEvent(view: SeekBar, seekChangeListener: SeekBar.OnSeekBarChangeListener) {
    view.setOnSeekBarChangeListener(seekChangeListener)
}


/**
 * [View]设置seekBar最大值
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_max_seekbar")
fun setSeekbarMax(view: SeekBar, max: Int?) {
    if (max == null) {
        return
    }
    view.max = max
    view.invalidate()
}


/**
 * [View]设置seekBar的进度
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_progress_seekbar")
fun setSeekbarProgress(view: SeekBar, progress: Int?) {
    if (progress == null) {
        return
    }
    view.progress = progress
    view.invalidate()
}

/**
 * [View]设置seekBar第二进度
 *
 */
@SuppressLint("CheckResult")
@BindingAdapter("bind_second_progress_seekbar")
fun setSeekbarSecondProgress(view: SeekBar, secondProgress: Int?) {
    if (secondProgress == null) {
        return
    }
    view.secondaryProgress = secondProgress
}