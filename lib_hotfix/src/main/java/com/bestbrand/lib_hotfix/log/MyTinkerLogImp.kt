package com.bestbrand.lib_hotfix.log

import android.util.Log
import com.tencent.tinker.lib.util.TinkerLog

/**
 * @author XingWei
 * @time 2020/7/28
 */
class MyTinkerLogImp : TinkerLog.TinkerLogImp {
    companion object {
        private const val LEVEL_VERBOSE = 0
        private const val LEVEL_DEBUG = 1
        private const val LEVEL_INFO = 2
        private const val LEVEL_WARNING = 3
        private const val LEVEL_ERROR = 4
        private const val LEVEL_NONE = 5
        private var level = LEVEL_VERBOSE

        fun getLogLevel(): Int {
            return level
        }

        fun setLevel(level: Int) {
            this.level = level
        }
    }

    override fun printErrStackTrace(
        tag: String?,
        tr: Throwable?,
        format: String?,
        vararg obj: Any?
    ) {
        var log = if (obj == null) format else String.format(format ?: "", *obj)
        if (log == null) {
            log = ""
        }
        log = log + "  " + Log.getStackTraceString(tr)
        android.util.Log.e(tag, log)
    }

    override fun i(tag: String?, s1: String?, vararg objects: Any?) {
        if (level <= LEVEL_INFO && s1.isNullOrEmpty().not()) {
            val log = if (objects == null) s1 else String.format(s1!!, *objects)
            android.util.Log.v(tag, log)
        }
    }

    override fun w(tag: String?, s1: String?, vararg objects: Any?) {
        if (level <= LEVEL_WARNING && s1.isNullOrEmpty().not()) {
            val log = if (objects == null) s1 else String.format(s1!!, *objects)
            android.util.Log.v(tag, log)
        }
    }

    override fun v(tag: String?, s1: String?, vararg objects: Any?) {
        if (level <= LEVEL_VERBOSE && s1.isNullOrEmpty().not()) {
            val log = if (objects == null) s1 else String.format(s1!!, *objects)
            android.util.Log.v(tag, log)
        }
    }

    override fun e(tag: String?, s1: String?, vararg objects: Any?) {
        if (level <= LEVEL_ERROR && s1.isNullOrEmpty().not()) {
            val log = if (objects == null) s1 else String.format(s1!!, *objects)
            android.util.Log.v(tag, log)
        }
    }

    override fun d(tag: String?, s1: String?, vararg objects: Any?) {
        if (level <= LEVEL_DEBUG && s1.isNullOrEmpty().not()) {
            val log = if (objects == null) s1 else String.format(s1!!, *objects)
            android.util.Log.v(tag, log)
        }
    }
}