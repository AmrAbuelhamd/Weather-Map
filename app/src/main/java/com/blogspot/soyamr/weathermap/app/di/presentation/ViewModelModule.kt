package com.blogspot.soyamr.weathermap.app.di.presentation

import android.location.Geocoder
import com.blogspot.soyamr.weathermap.presentation.city_weather.CityWeatherDetailsViewModel
import com.blogspot.soyamr.weathermap.presentation.map.MapsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MapsViewModel(Geocoder(androidContext())) }
    viewModel { CityWeatherDetailsViewModel(get()) }
}