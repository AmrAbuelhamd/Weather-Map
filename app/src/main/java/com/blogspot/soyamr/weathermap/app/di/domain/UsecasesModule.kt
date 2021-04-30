package com.blogspot.soyamr.weathermap.app.di.domain

import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByNameImpl
import org.koin.dsl.module

val useCasesModule = module {
    factory { GetCityWeatherByNameImpl(get()) as GetCityWeatherByName }
}