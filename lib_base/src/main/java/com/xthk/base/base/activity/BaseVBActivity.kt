package com.xthk.base.base.activity

import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xthk.base.R
import com.xthk.base.databinding.ActivityBaseBinding
import com.xthk.base.ext.fromL
import com.xthk.base.utils.ActManageHelper
import com.xthk.base.utils.StatusBarUtil
import com.xthk.base.utils.StatusBarUtil.setStatusBarColor
import com.xthk.base.utils.StatusBarUtil.transparencyBar

/**
 * ViewBinding基础类
 */
abstract class BaseVBActivity<VB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VB

    abstract val layoutId: Int

    private var loadingView: View? = null

    private var emptyView: View? = null

    private var mEyeView: View? = null

    protected var isResume = false


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActManageHelper.pushActivity(this)
        setStatusBar()
        setContentView(layoutId)
        initView()
    }

    override fun setContentView(layoutResID: Int) {
        val mBaseBinding = DataBindingUtil.inflate<ActivityBaseBinding>(
            LayoutInflater.from(this),
            R.layout.activity_base,
            null,
            false
        )
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        binding.lifecycleOwner = this

        // content
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.root.layoutParams = params
        val mContainer = mBaseBinding.root.findViewById(R.id.container) as RelativeLayout
        mContainer.addView(binding.root)

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
        super.setContentView(mBaseBinding.root)
        initEye()
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return
        }
        super.setRequestedOrientation(requestedOrientation)
    }

    abstract fun initView()


    open fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                transparencyBar(this)
            } else {
                setStatusBarColor(this, getStatusBarColor())
            }
            if (isUseBlackFontWithStatusBar()) {
                StatusBarUtil.setLightStatusBar(this, true, isUseFullScreenMode())
            }
        }
    }


    /**
     * 修改底部导航栏颜色
     */
    protected fun setNavigationBarColor(color: Int = Color.WHITE) {
        if (fromL()) {
            window.navigationBarColor = color
        }
    }

    /**
     * 是否设置成透明状态栏，即就是全屏模式
     */
    protected open fun isUseFullScreenMode(): Boolean {
        return true
    }

    /**
     * 更改状态栏颜色，只有非全屏模式下有效
     */
    protected open fun getStatusBarColor(): Int {
        return R.color.color_ffffff
    }

    /**
     * 是否改变状态栏文字颜色为黑色，默认为黑色
     */
    protected open fun isUseBlackFontWithStatusBar(): Boolean {
        return true
    }


    /**
     * 初始化加载动画控件
     */
    open fun initLoadingView(): View? {
        return null
    }

    /**
     * 初始化空页面控件
     */
    open fun initEmptyView(): View? {
        return null
    }

    /**
     * 添加护眼模式浮层
     */
    private fun initEye() {
        val content = findViewById<ViewGroup>(android.R.id.content)
        mEyeView = View(this).apply {
            setBackgroundColor(Color.TRANSPARENT)
        }
        val params = WindowManager.LayoutParams()
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL and
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE and
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        content.addView(mEyeView, params)
    }

    /**
     * 开启护眼模式
     */
    fun openEye() {
        mEyeView?.setBackgroundColor(getFilterColor())
    }

    /**
     * 关闭护眼模式
     */
    fun closeEye() {
        mEyeView?.setBackgroundColor(Color.TRANSPARENT)
    }

    /**
     * 过滤蓝光
     *
     * * @param blueFilterPercent 蓝光过滤比例[10-30-80]
     */
    private fun getFilterColor(blueFilterPercent: Int = 30): Int {
        var realFilter = blueFilterPercent
        if (realFilter < 10) {
            realFilter = 10
        } else if (realFilter > 80) {
            realFilter = 80
        }
        val a = (realFilter / 80f * 180).toInt()
        val r = (200 - realFilter / 80f * 190).toInt()
        val g = (180 - realFilter / 80f * 170).toInt()
        val b = (60 - realFilter / 80f * 60).toInt()
        return Color.argb(a, r, g, b)
    }


    /**
     * 判断是否透明背景
     */
    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable")
                .getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m = ActivityInfo::class.java.getMethod(
                "isTranslucentOrFloating",
                TypedArray::class.java
            )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }


}