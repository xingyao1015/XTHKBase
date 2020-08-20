package com.bestbrand.lib_hotfix.task

import com.tencent.tinker.lib.util.TinkerLog
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * @author: xingwei
 * @email: 654206017@qq.com
 *
 * Desc:
 */
class NetworkTask(
    private val mUrl: String,
    private val method: String,
    private val mNetCallback: NetCallback
) : Runnable {

    override fun run() {
        try {
            TinkerLog.d("Tinker", mUrl)
            val url = URL(mUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                connectTimeout = 10000
                readTimeout = 10000
                requestMethod = method
                connect()
            }
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val content = outputStream.toByteArray().toString(Charset.forName("UTF-8"))
                mNetCallback.onSuccess(content)
                inputStream.close()
                outputStream.close()
            } else {
                mNetCallback.onFail(null)
            }
        } catch (e: Exception) {
            mNetCallback.onFail(e)
        }

    }
}