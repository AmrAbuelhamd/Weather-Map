package com.blogspot.soyamr.weathermap.app.di.presentation

import android.location.Geocoder
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.city_weather.CityWeatherDetailsViewModel
import com.blogspot.soyamr.weathermap.presentation.map.MapsViewModel
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        Places.initialize(androidContext(), androidContext().getString(R.string.google_maps_key))
        MapsViewModel(Geocoder(androidContext()), Places.createClient(androidContext()))
    }
    viewModel { CityWeatherDetailsViewModel(get()) }
}