package com.blogspot.soyamr.data.converters

import com.blogspot.soyamr.data.net.models.CityWeatherResponse
import com.blogspot.soyamr.domain.enums.Direction
import com.blogspot.soyamr.domain.models.Weather

fun CityWeatherResponse.toDomain() =
    Weather(
        name ?: "city name placeholder",
        main?.humidity ?: 0,
        main?.temp?.toInt()?:0,
        main?.pressure?.toDouble() ?: 0.toDouble(),
        wind?.speed ?: 0.toDouble(),
        Direction.getDirection(wind?.deg?:0),
        weather?.get(0)?.description ?:"description placeholder",
        weather?.get(0)?.icon ?:"icon placeholder",
    )