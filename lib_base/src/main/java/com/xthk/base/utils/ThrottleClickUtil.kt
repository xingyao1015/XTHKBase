package com.xthk.base.utils

import android.view.View
import com.xthk.base.binding.ViewClickConsumer

object ThrottleClickUtil {
    private var lastClickTime = 0L

    fun clickWithThrottleFirst(view: View?, consumer: ViewClickConsumer?) {
        view?.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime > 2000L) {
                consumer?.accept(it)
            }
            lastClickTime = clickTime
        }
    }

}