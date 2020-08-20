package com.xthk.base.binding

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind_image_play_anim")
fun playAnim(imageView: ImageView, isStart: Boolean) {
    val drawable = imageView.drawable ?: return
    val anim = drawable as AnimationDrawable
    if (isStart) {
        anim.start()
    } else {
        anim.stop()
    }
}