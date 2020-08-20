package com.bestbrand.lib_hotfix.task

import java.io.File


/**
 * @author: xingwei
 * @email: 654206017@qq.com
 *
 * Desc:
 */
interface FileCallback {
    fun onSuccess(file: File?)
    fun onProgress(progress:Int)
    fun onFail(e: Exception?)
}