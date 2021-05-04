package com.blogspot.soyamr.data.converters

import com.blogspot.soyamr.data.net.models.CityWeatherResponse
import com.blogspot.soyamr.domain.enums.Direction
import com.blogspot.soyamr.domain.models.Weather

fun CityWeatherResponse.toDomain() =
    Weather(
        name,
        main.humidity.toInt(),
        main.temp.toInt(),
        main.pressure,
        wind.speed,
        Direction.getDirection(wind.deg),
        weather[0].description,
        weather[0].icon,
    )