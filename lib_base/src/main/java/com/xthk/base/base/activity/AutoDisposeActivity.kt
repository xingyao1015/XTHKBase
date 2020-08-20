package com.xthk.base.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.xthk.base.base.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Author:XingWei
 * Time:2020/4/1 10:58
 * Desc:
 */
open class AutoDisposeActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + autoDisposeInterceptor()
}