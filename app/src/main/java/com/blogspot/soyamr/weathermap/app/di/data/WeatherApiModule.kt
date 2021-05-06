package com.blogspot.soyamr.weathermap.app.di.data


import com.blogspot.soyamr.data.net.WeatherApi
import com.blogspot.soyamr.data.net.interceptors.FixedQueryInterceptor
import com.blogspot.soyamr.data.net.interceptors.InternetExceptionInterceptor
import com.blogspot.soyamr.weathermap.R
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val INTERNET_EXCEPTION_HANDLER_INTERCEPTOR = "internet_exception_handler_interceptor"
const val FIXED_QUERY_INTERCEPTOR = "fixed_query_interceptor"
const val LOGGER_INTERCEPTOR = "logger_interceptor"

val weatherApiModule = module {
    factory<Interceptor>(named(INTERNET_EXCEPTION_HANDLER_INTERCEPTOR)) {
        InternetExceptionInterceptor(get())
    }
    factory<Interceptor>(named(FIXED_QUERY_INTERCEPTOR)) {
        FixedQueryInterceptor(androidContext().getString(R.string.weather_key))
    }
    factory(named(LOGGER_INTERCEPTOR)) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(named(INTERNET_EXCEPTION_HANDLER_INTERCEPTOR)))
            .addInterceptor(get<Interceptor>(named(FIXED_QUERY_INTERCEPTOR)))
            .addInterceptor(get<HttpLoggingInterceptor>(named(LOGGER_INTERCEPTOR)))
            .build()
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())
    }

    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.weather_base_url))
            .client(get<OkHttpClient>())
            .addConverterFactory(get())
            .build()
    }

    single { get<Retrofit>().create(WeatherApi::class.java) }
}