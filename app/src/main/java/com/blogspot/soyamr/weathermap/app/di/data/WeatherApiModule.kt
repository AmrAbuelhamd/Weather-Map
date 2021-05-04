package com.blogspot.soyamr.weathermap.app.di.data


import com.blogspot.soyamr.weathermap.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit


val weatherApiModule = module {
    single {
        GsonBuilder()
            .setLenient()
            .create()
    }



    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.weather_base_url))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>().create(UserApi::class.java) }
    single { get<Retrofit>().create(MechanicApi::class.java) }
    single { get<Retrofit>().create(TMRApi::class.java) }
    single { get<Retrofit>().create(WarehouseApi::class.java) }
    single { get<Retrofit>().create(MiscApi::class.java) }
    single { TokenApiImpl(get()) as TokenApi }
}