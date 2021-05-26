package com.blogspot.soyamr.weathermap.app.di.presentation

import android.location.Geocoder
import com.blogspot.soyamr.weathermap.presentation.fragments.city_weather.CityWeatherDetailsViewModel
import com.blogspot.soyamr.weathermap.presentation.fragments.map.MapsViewModel
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MapsViewModel(Geocoder(androidContext()), Places.createClient(androidContext()))
    }
    viewModel { CityWeatherDetailsViewModel(get()) }
}