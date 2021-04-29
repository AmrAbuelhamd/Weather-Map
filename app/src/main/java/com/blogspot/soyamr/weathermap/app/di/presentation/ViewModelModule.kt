package com.blogspot.soyamr.weathermap.app.di.presentation

import com.blogspot.soyamr.weathermap.presentation.city_weather.CityWeatherDetailsViewModel
import com.blogspot.soyamr.weathermap.presentation.map.MapsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MapsViewModel() }
    viewModel { CityWeatherDetailsViewModel(get()) }
}