package com.blogspot.soyamr.weathermap.app

import android.app.Application
import com.blogspot.soyamr.weathermap.app.di.domain.dataSourceModule
import com.blogspot.soyamr.weathermap.app.di.domain.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherMapApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherMapApp)

            modules(
                    listOf(
                            dataSourceModule,
                            useCasesModule
                    )
            )
        }
    }
}