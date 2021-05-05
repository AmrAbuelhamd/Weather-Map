package com.blogspot.soyamr.weathermap.app.di.data


import com.blogspot.soyamr.data.net.WeatherApi
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.app.utils.Connectivity
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
import java.io.IOException

const val INTERNET_EXCEPTION_HANDLER_INTERCEPTOR = "internet_exception_handler_interceptor"
const val FIXED_QUERY_INTERCEPTOR = "fixed_query_interceptor"
const val LOGGER_INTERCEPTOR = "logger_interceptor"

val weatherApiModule = module {
    single(named(INTERNET_EXCEPTION_HANDLER_INTERCEPTOR)) {
        Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            if (!get<Connectivity>().isOnline()) {
                throw IOException(androidContext().getString(R.string.no_internet))
            }
            chain.proceed(request)
        }
    }
    single(named(FIXED_QUERY_INTERCEPTOR)) {
        Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()
            val url = request.url
            request = request.newBuilder()
                .url(
                    url.newBuilder()
                        .addQueryParameter(
                            "units",
                            "metrics"
                        ).addQueryParameter(
                            "appid",
                            androidContext().getString(R.string.weather_key)
                        )
                        .build()
                )
                .build()
            chain.proceed(request)
        }
    }
    single(named(LOGGER_INTERCEPTOR)) {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
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