package com.xthk.base.base.fragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.xthk.base.base.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class AutoDisposeDialogFragment : DialogFragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewLifecycleOwner.autoDisposeInterceptor()
}