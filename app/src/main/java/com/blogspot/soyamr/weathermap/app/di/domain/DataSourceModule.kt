package com.blogspot.soyamr.weathermap.app.di.domain

import com.blogspot.soyamr.data.WeatherDataSourceImpl
import com.blogspot.soyamr.domain.WeatherDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single { WeatherDataSourceImpl() as WeatherDataSource }
}