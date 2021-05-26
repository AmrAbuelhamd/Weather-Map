package com.blogspot.soyamr.data.net.interceptors

import com.blogspot.soyamr.data.utils.NoInternetException
import com.blogspot.soyamr.domain.utils.Connectivity
import okhttp3.Interceptor
import okhttp3.Response

class InternetExceptionInterceptor(
    private val connectivity: Connectivity
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!connectivity.isOnline()) {
            throw NoInternetException()
        }
        return chain.proceed(request)
    }
}