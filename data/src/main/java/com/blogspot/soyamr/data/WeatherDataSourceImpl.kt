package com.blogspot.soyamr.data

import com.blogspot.soyamr.domain.WeatherDataSource
import com.blogspot.soyamr.domain.models.Weather

class WeatherDataSourceImpl : WeatherDataSource {
    override suspend fun getWeatherByCityName(cityName: String): Weather {
        TODO("Not yet implemented")
    }
}