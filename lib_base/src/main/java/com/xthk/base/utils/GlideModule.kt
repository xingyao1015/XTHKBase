package com.xthk.base.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //设置缓存大小为20mb
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb
        //设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        //根据SD卡是否可用选择是在内部缓存还是SD卡缓存
        builder.setDiskCache(
            ExternalPreferredCacheDiskCacheFactory(
                context,
                "picture",
                memoryCacheSizeBytes.toLong()
            )
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()

        builder.sslSocketFactory(SSLSocketClient.sSLSocketFactory, SSLSocketClient.trustManager[0])
            .hostnameVerifier(SSLSocketClient.hostnameVerifier)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)

        val client = builder.build()
        client.sslSocketFactory()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}

object SSLSocketClient {
    //获取这个SSLSocketFactory
    val sSLSocketFactory: SSLSocketFactory
        get() = try {
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager, SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    //获取TrustManager
    val trustManager: Array<X509TrustManager>
        get() {
            return arrayOf(
                object : X509TrustManager {

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
        }

    //获取HostnameVerifier
    val hostnameVerifier: HostnameVerifier
        get() {
            return HostnameVerifier { hostname, session -> true }
        }
}