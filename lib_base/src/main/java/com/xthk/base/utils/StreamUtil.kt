package com.xthk.base.utils

import android.text.TextUtils
import android.webkit.URLUtil
import java.io.*

/**
 * @author XingWei
 * @time 2020/5/27
 * 对文件流的操作工具类
 */
object StreamUtil {
    /**
     * Copy 文件
     *
     * @param in           文件
     * @param outputStream 输出流
     * @return 是否copy成功
     */
    fun copy(`in`: File, outputStream: OutputStream): Boolean {
        if (!`in`.exists()) return false
        val stream: InputStream
        stream = try {
            FileInputStream(`in`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }
        return copy(stream, outputStream)
    }

    /**
     * 把一个文件copy到另外一个文件
     *
     * @param in  输入文件
     * @param out 输出文件
     * @return 是否copy成功
     */
    fun copy(`in`: File, out: File): Boolean {
        if (!`in`.exists()) return false
        val stream: InputStream
        stream = try {
            FileInputStream(`in`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }
        return copy(stream, out)
    }

    /**
     * 把输入流输出到文件
     *
     * @param inputStream 输入流
     * @param out         输出文件
     * @return 是否copy成功
     */
    fun copy(inputStream: InputStream, out: File): Boolean {
        if (!out.exists()) {
            val fileParentDir = out.parentFile
            if (!fileParentDir.exists()) {
                if (!fileParentDir.mkdirs()) return false
            }
            try {
                if (!out.createNewFile()) return false
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
        }
        val outputStream: OutputStream
        outputStream = try {
            FileOutputStream(out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }
        return copy(inputStream, outputStream)
    }

    /**
     * 把一个输入流定向到输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 是否输出成功
     */
    fun copy(inputStream: InputStream, outputStream: OutputStream): Boolean {
        return try {
            val buffer = ByteArray(1024)
            var realLength: Int
            while (inputStream.read(buffer).also { realLength = it } > 0) {
                outputStream.write(buffer, 0, realLength)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(inputStream)
            close(outputStream)
        }
    }

    /**
     * 对流进行close操作
     *
     * @param closeables Closeable
     */
    fun close(vararg closeables: Closeable?) {
        if (closeables == null) return
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 删除某路径的文件
     *
     * @param path 文件路径
     * @return 删除是否成功
     */
    fun delete(path: String?): Boolean {
        if (TextUtils.isEmpty(path) || URLUtil.isNetworkUrl(path)) return false
        val file = File(path)
        return file.exists() && file.delete()
    }
}