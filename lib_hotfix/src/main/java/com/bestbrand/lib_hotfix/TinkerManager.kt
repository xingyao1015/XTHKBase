package com.bestbrand.lib_hotfix

import android.content.Context
import com.bestbrand.lib_hotfix.crash.SampleUncaughtExceptionHandler
import com.bestbrand.lib_hotfix.reporter.CustomPatchListener
import com.bestbrand.lib_hotfix.service.CustomResultService
import com.tencent.tinker.entry.ApplicationLike
import com.tencent.tinker.lib.patch.AbstractPatch
import com.tencent.tinker.lib.patch.UpgradePatch
import com.tencent.tinker.lib.reporter.DefaultLoadReporter
import com.tencent.tinker.lib.reporter.DefaultPatchReporter
import com.tencent.tinker.lib.reporter.LoadReporter
import com.tencent.tinker.lib.reporter.PatchReporter
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tencent.tinker.lib.util.UpgradePatchRetry

/**
 * @author XingWei
 * @time 2020/7/27
 * 对Tinker的所有api做一层封装
 */
object TinkerManager {
    private var mAppLike: ApplicationLike? = null
    private var mPatchListener: CustomPatchListener? = null
    private var isInstalled = false
    private var uncaughtExceptionHandler: SampleUncaughtExceptionHandler? = null

    fun setTinkerApplicationLike(appLike: ApplicationLike) {
        mAppLike = appLike
    }
    /**
     * 安装Tinker
     */
    fun installTinker(applicationLike: ApplicationLike) {

        if (isInstalled) {
            return
        }
        mPatchListener = CustomPatchListener(applicationLike.application)
        val loadReporter: LoadReporter = DefaultLoadReporter(applicationLike.application)
        val patchReporter: PatchReporter = DefaultPatchReporter(applicationLike.application)
        val upgradePatchProcessor: AbstractPatch = UpgradePatch()
        TinkerInstaller.install(
            mAppLike,
            loadReporter, patchReporter, mPatchListener,
            CustomResultService::class.java, upgradePatchProcessor
        )
        isInstalled = true
    }

    /**
     * 补丁合成失败，是否重试
     */
    fun setUpgradeRetryEnable(enable: Boolean) {
        UpgradePatchRetry.getInstance(mAppLike?.application)
            .setRetryEnable(enable)
    }

    /**
     * 初始化补丁加载过程中的异常处理
     */
    fun initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = SampleUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)
        }
    }

    /**
     * 加载Patch文件
     */
    fun loadPatch(path: String?, md5Value: String?) {
        if (Tinker.isTinkerInstalled()) {
            mPatchListener?.setCurrentMD5(md5Value)
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path)
        }
    }

    /**
     * 通过ApplicationLike获取Context
     */
    private fun getApplicationContext(): Context? {
        return if (mAppLike != null) {
            mAppLike!!.application?.applicationContext
        } else null
    }

    fun getTinkerApplicationLike(): ApplicationLike? {
        return mAppLike
    }
}