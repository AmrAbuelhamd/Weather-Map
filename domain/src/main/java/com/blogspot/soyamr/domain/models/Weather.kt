package com.blogspot.soyamr.domain.models

import com.blogspot.soyamr.domain.enums.Direction

data class Weather(
    val cityName: String,
    val humidity: Int,
    val temp: Int,
    val pressure: Double,
    val windSpeed: Double,
    val direction: Direction,
    val generalDescription: String
)
