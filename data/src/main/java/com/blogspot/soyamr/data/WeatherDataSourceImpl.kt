package com.blogspot.soyamr.data

import com.blogspot.soyamr.data.converters.toDomain
import com.blogspot.soyamr.data.net.WeatherApi
import com.blogspot.soyamr.domain.WeatherDataSource
import com.blogspot.soyamr.domain.models.Weather

class WeatherDataSourceImpl(private val weatherApi: WeatherApi) : WeatherDataSource {
    override suspend fun getWeatherByCityName(cityName: String): Weather =
        weatherApi.getCityWeather(cityName).toDomain()
}