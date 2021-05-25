package com.blogspot.soyamr.weathermap.presentation.converters

import com.blogspot.soyamr.weathermap.presentation.models.Weather
import com.blogspot.soyamr.weathermap.presentation.models.enums.WeatherDescription
import com.blogspot.soyamr.domain.models.Weather as DomainWeather

fun DomainWeather.toPresenter() =
    Weather(
        cityName,
        humidity.toString(),
        temp.toString(),
        pressure.toString(),
        windSpeed.toString(),
        direction.symbol,
        WeatherDescription.getStringId(generalDescription),
        WeatherDescription.getImageSrcId(generalDescription),
        iconCode
    )