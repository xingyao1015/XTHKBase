package com.bestbrand.lib_hotfix.module

import android.content.Context
import com.bestbrand.lib_hotfix.BuildConfig
import com.bestbrand.lib_hotfix.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author XingWei
 * @time 2020/7/29
 * 补丁信息
 */

data class BasePath(val status_code: Int, val message: String, val data: PatchInfo?) :
    Serializable {

    fun needLoadPatch(context: Context, curPushId: String): Boolean {
        return isSuc() &&
                data != null &&
                getPatchUrl().isNotEmpty() &&
                getPatchFileMd5().isNotEmpty() &&
                data.status == 1 &&
                getCurIssueType(context, curPushId)
    }

    fun getPatchUrl(): String {
        return data?.downloadUrl ?: ""
    }

    fun getPatchFileMd5(): String {
        return data?.md5 ?: ""
    }

    private fun isSuc(): Boolean {
        return status_code == 200
    }

    private fun getCurIssueType(context: Context, curPushId: String): Boolean {
        return if (BuildConfig.DEBUG) {
            //安装包是测试包 不分开发和全量
            true
        } else {
            //安装包是正式包
            if (data?.issue_type == 1) {
                //开发设备
                context.resources.getStringArray(R.array.pushId).contains(curPushId)
            } else {
                //全量设备
                true
            }
        }
    }

    override fun toString(): String {
        return "BasePath(status_code=$status_code, message='$message', data=$data)"
    }
}

data class PatchInfo(
    @SerializedName("patch_url")
    val downloadUrl: String,
    @SerializedName("patch_md5")
    val md5: String,
    @SerializedName("patch_remark")
    val remark: String,
    val issue_type: Int, //1开发设备 2全量设备
    val status: Int, //0已停止 1已发布
    val patchMessage: String
) : Serializable {
    override fun toString(): String {
        return "PatchInfo(downloadUrl='$downloadUrl', md5='$md5', remark='$remark', issue_type=$issue_type, status=$status, patchMessage='$patchMessage')"
    }
}