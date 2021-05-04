package com.blogspot.soyamr.weathermap.app.di.data


import com.blogspot.soyamr.data.net.WeatherApi
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.app.utils.Connectivity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.IOException


val weatherApiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.weather_base_url))
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                ).addInterceptor { chain ->
                    val request = chain.request()
                    if (!get<Connectivity>().isOnline()) {
                        throw IOException(androidContext().getString(R.string.no_internet))
                    }
                    chain.proceed(request)
                }.build()
            )
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single { get<Retrofit>().create(WeatherApi::class.java) }
}