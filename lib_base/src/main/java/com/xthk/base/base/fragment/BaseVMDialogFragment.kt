package com.xthk.base.base.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xthk.base.base.viewmodel.BaseViewModel

abstract class BaseVMDialogFragment<VB : ViewDataBinding, VM : BaseViewModel> :
    BaseVBDialogFragment<VB>() {

    abstract val viewModel: VM

    //标记是否第一次初始化数据
    var mIsFirstInitData = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        startObserve()
    }

    override fun onResume() {
        super.onResume()
        if (mIsFirstInitData) {
            mIsFirstInitData = false
            onFirstInit()
        }
    }


    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    protected open fun startObserve() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            showOrHideLoadingView = it

        })
        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            showOrHideEmptyView = it
        })
    }

    /**
     * 当首次初始化会调用的方法 （常用在多个fragment切换）
     */
    protected open fun onFirstInit() {

    }

    /**
     * 初始化数据
     */
    protected open fun initData() {
    }

}