package com.blogspot.soyamr.domain

import com.blogspot.soyamr.domain.models.Weather

interface WeatherDataSource {
    suspend fun getWeatherByCityName(cityName:String):Weather
}