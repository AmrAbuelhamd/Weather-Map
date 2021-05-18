package com.blogspot.soyamr.weathermap.app

import android.app.Application
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.app.di.data.weatherApiModule
import com.blogspot.soyamr.weathermap.app.di.domain.dataSourceModule
import com.blogspot.soyamr.weathermap.app.di.domain.useCasesModule
import com.blogspot.soyamr.weathermap.app.di.presentation.viewModelModule
import com.blogspot.soyamr.weathermap.app.di.utils.utilsModule
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherMapApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Places.initialize(this, this.getString(R.string.google_maps_key))

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WeatherMapApp)

            modules(
                listOf(
                    viewModelModule,
                    dataSourceModule,
                    useCasesModule,
                    utilsModule,
                    weatherApiModule,

                    )
            )
        }
    }
}