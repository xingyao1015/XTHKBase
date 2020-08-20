package com.xthk.base.base.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Author:XingWei
 * Time:2020/4/1 16:04
 * Desc:
 */
data class BaseEntity<out T>(
    @SerializedName("status_code")
    val statusCode: Int = 0,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null
) {
    fun isSuccess(): Boolean {
        return statusCode in 200..299
    }
}