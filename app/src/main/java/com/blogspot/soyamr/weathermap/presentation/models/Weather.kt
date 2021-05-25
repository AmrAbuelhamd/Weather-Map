package com.blogspot.soyamr.weathermap.presentation.models

data class Weather(
    val cityName: String,
    val humidity: String,
    val temp: String,
    val pressure: String,
    val windSpeed: String,
    val direction: String,
    val generalDescription: Int,
    val generalDescriptionImgId: Int,
    val iconCode: String
)
