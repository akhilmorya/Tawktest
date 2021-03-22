package com.tawktest.data.remote.interceptor

import android.content.Context
import com.tawktest.app.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

class NetworkInterceptor(private val mContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (NetworkUtils.isConnected(mContext)) {
            request.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build()

        } else {
            request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .build()
        }

        return chain.proceed(request)
    }
}