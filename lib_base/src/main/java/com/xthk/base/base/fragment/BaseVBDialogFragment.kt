package com.xthk.base.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xthk.base.R

abstract class BaseVBDialogFragment<VB : ViewDataBinding> : AutoDisposeDialogFragment() {
    protected lateinit var binding: VB

    abstract val layoutId: Int

    protected var mRoot: View? = null

    private var emptyView: View? = null

    private var loadingView: View? = null

    /**
     * 展示或隐藏空页面
     */
    var showOrHideEmptyView: Boolean
        set(value) {
            emptyView?.visibility = if (value) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        get() {
            return emptyView?.visibility == View.VISIBLE
        }

    /**
     * 展示或隐藏加载中控件
     */
    var showOrHideLoadingView: Boolean
        set(value) {
            loadingView?.visibility = if (value) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        get() {
            return loadingView?.visibility == View.VISIBLE
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initArgs(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_base, null)
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        binding.lifecycleOwner = this
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.root.layoutParams = params
        val mContainer = mRoot!!.findViewById<RelativeLayout>(R.id.container)
        mContainer.addView(binding.root, 0)

        emptyView = initEmptyView()
        if (emptyView != null) {
            emptyView!!.layoutParams = params
            mContainer.addView(emptyView!!)
        }

        loadingView = initLoadingView()
        if (loadingView != null) {
            loadingView!!.layoutParams = params
            mContainer.addView(loadingView!!)
        }

        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 初始化空页面控件
     */
    open fun initEmptyView(): View? {
        return null
    }

    /**
     * 初始化加载动画控件
     */
    open fun initLoadingView(): View? {
        return null
    }

    /**
     * 初始化参数
     */
    protected open fun initArgs(arguments: Bundle?) {
    }

    /**
     * 初始化View
     */
    protected open fun initView() {
    }

}