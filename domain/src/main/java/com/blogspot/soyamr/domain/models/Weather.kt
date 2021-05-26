package com.blogspot.soyamr.domain.models

data class Weather(
    val cityName: String,
    val humidity: Int,
    val temp: Int,
    val pressure: Double,
    val windSpeed: Double,
    val deg: Int,
    val generalDescription: String,
    val iconCode: String
)
