package com.blogspot.soyamr.domain.usecases

import com.blogspot.soyamr.domain.models.Weather

interface GetCityWeatherByName {
    suspend operator fun invoke(cityName: String): Weather
}