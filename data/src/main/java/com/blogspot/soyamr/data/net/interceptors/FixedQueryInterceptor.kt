package com.blogspot.soyamr.data.net.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class FixedQueryInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url
        request = request.newBuilder()
            .url(
                url.newBuilder()
                    .addQueryParameter(
                        "units",
                        "metric"
                    ).addQueryParameter(
                        "appid",
                        apiKey
                    )
                    .build()
            )
            .build()
        return chain.proceed(request)
    }
}