package com.blogspot.soyamr.weathermap.presentation.models

data class Weather(
    val cityName: String,
    val humidity: String,
    val temp: String,
    val pressure: String,
    val windSpeed: String,
    val generalDescriptionId: Int,
    val generalDescriptionImgId: Int,
    val iconUrl: String
)
