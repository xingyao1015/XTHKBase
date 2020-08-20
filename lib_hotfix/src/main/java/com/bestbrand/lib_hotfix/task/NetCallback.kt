package com.bestbrand.lib_hotfix.task

/**
 * @author: xingwei
 * @email: 654206017@qq.com
 *
 * Desc:
 */
interface NetCallback {
    fun onSuccess(content:String)

    fun onFail(e:Exception?)
}