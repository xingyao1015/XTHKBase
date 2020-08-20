package com.bestbrand.lib_hotfix.task

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author: xingwei
 * @email: 654206017@qq.com
 *
 * Desc: 下载task
 */
class DownloadTask(
    private val mUrl: String,
    private val mFilePath: String,
    private val mFileCallback: FileCallback
) : Runnable {

    override fun run() {
        try {
            val url = URL(mUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                connectTimeout = 5000
                requestMethod = "GET"
                connect()
            }
            if (connection.responseCode == 200) {
                mFileCallback.onSuccess(handelConnection(connection))
            } else {
                mFileCallback.onFail(null)
            }
        } catch (e: Exception) {
            mFileCallback.onFail(e)
        }

    }

    private fun handelConnection(connection: HttpURLConnection): File? {
        var inputStream: InputStream? = null
        var file: File?
        var fos: FileOutputStream? = null
        val buffer = ByteArray(2048)
        var length: Int
        var currentLength = 0
        val sumLength: Double
        try {
            checkLocalFilePath(mFilePath)
            file = File(mFilePath)
            fos = FileOutputStream(file)
            inputStream = connection.inputStream
            sumLength = connection.contentLength.toDouble()
            while (inputStream.read(buffer).also { length = it } != -1) {
                fos.write(buffer, 0, length)
                currentLength += length
                mFileCallback.onProgress((currentLength / sumLength * 100).toInt())
            }
            fos.flush()
        } catch (e: Exception) {
            file = null
        } finally {
            try {
                fos?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return file
    }

    private fun checkLocalFilePath(localFilePath: String) {
        val path = File(localFilePath.substring(0, localFilePath.lastIndexOf("/") + 1))
        val file = File(localFilePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}