package com.blogspot.soyamr.domain.usecases

import com.blogspot.soyamr.domain.WeatherDataSource
import com.blogspot.soyamr.domain.models.Weather

class GetCityWeatherByNameImpl(private val weatherDataSource: WeatherDataSource) :
    GetCityWeatherByName {
    override suspend operator fun invoke(cityName: String): Weather =
        weatherDataSource.getWeatherByCityName(cityName)
}