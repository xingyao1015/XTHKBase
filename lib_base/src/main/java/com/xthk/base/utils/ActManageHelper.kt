package com.xthk.base.utils

import android.app.Activity
import java.util.*


/**
 * @author XingWei
 * @time 2020/5/6
 * Activity统一管理的辅助类
 */
object ActManageHelper {
    /**
     * 维护Activity 的list
     */
    val mActivityList= Collections.synchronizedList(LinkedList<Activity>())


    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    fun pushActivity(activity: Activity?) {
        mActivityList.add(activity)
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    fun popActivity(activity: Activity?) {
        mActivityList.remove(activity)
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return if (mActivityList == null || mActivityList.isEmpty()) {
            null
        } else mActivityList[mActivityList.size - 1]
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        if (mActivityList == null || mActivityList.isEmpty()) {
            return
        }
        val activity = mActivityList[mActivityList.size - 1]
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (mActivityList == null || mActivityList.isEmpty()) {
            return
        }
        if (activity != null) {
            mActivityList.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        if (mActivityList == null || mActivityList.isEmpty()) {
            return
        }
        for (activity in mActivityList) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    fun findActivity(cls: Class<*>): Activity? {
        var targetActivity: Activity? = null
        if (mActivityList != null) {
            for (activity in mActivityList) {
                if (activity.javaClass == cls) {
                    targetActivity = activity
                    break
                }
            }
        }
        return targetActivity
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    fun getTopActivity(): Activity? {
        var mBaseActivity: Activity?
        synchronized(mActivityList) {
            val size = mActivityList.size - 1
            if (size < 0) {
                return null
            }
            mBaseActivity = mActivityList[size]
        }
        return mBaseActivity
    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    fun getTopActivityName(): String? {
        var mBaseActivity: Activity
        synchronized(mActivityList) {
            val size = mActivityList.size - 1
            if (size < 0) {
                return null
            }
            mBaseActivity = mActivityList[size]
        }
        return mBaseActivity::class.java.simpleName
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (mActivityList == null) {
            return
        }
        for (activity in mActivityList) {
            activity.finish()
        }
        mActivityList.clear()
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
        }
    }
}