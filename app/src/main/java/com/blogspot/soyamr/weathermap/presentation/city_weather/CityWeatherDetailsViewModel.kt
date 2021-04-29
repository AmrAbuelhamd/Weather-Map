package com.blogspot.soyamr.weathermap.presentation.city_weather

import androidx.lifecycle.ViewModel
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName

class CityWeatherDetailsViewModel(
    val getCityWeatherByName: GetCityWeatherByName
) : ViewModel() {
}