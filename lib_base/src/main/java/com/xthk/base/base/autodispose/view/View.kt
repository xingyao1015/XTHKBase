package com.xthk.base.base.autodispose.view

import android.view.View
import com.xthk.base.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

private val TAG = R.id.autodispose_view_tag
/**
 * @author: xingwei
 * @email: 654206017@qq.com
 *
 * Desc: [CoroutineScope] tied to this [View]
 * This scope will be canceled when View is detached
 */

val View.autoDisposeScope: CoroutineScope
    get() {
        val exist = getTag(TAG) as? CoroutineScope
        if (exist != null) {
            return exist
        }
        val newScope = ViewCoroutineScope(
            SupervisorJob() +
                    Dispatchers.Main +
                    autoDisposeInterceptor()
        )
        setTag(TAG, newScope)
        return newScope
    }

internal class ViewCoroutineScope(
    override val coroutineContext: CoroutineContext
) : CoroutineScope