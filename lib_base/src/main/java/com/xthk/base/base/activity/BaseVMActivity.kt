package com.xthk.base.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xthk.base.base.viewmodel.BaseViewModel

abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewDataBinding> : BaseVBActivity<VB>() {
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    open fun startObserver() {
        viewModel.isLoading
            .observe(this, Observer {
                showOrHideLoadingView = it
            })

        viewModel.isEmpty
            .observe(this, Observer {
                showOrHideEmptyView = it
            })
    }
}