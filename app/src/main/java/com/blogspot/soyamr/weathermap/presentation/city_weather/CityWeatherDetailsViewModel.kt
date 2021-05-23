package com.blogspot.soyamr.weathermap.presentation.city_weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName

class CityWeatherDetailsViewModel(
    private val getCityWeatherByName: GetCityWeatherByName
) : ViewModel() {

    val cityName = MutableLiveData("")

    fun setCityWeatherInfo(cityName: String?) {
        this.cityName.value = cityName
    }
}