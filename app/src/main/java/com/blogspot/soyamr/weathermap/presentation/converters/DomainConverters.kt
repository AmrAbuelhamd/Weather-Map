package com.blogspot.soyamr.weathermap.presentation.converters

import com.blogspot.soyamr.weathermap.presentation.models.Weather
import com.blogspot.soyamr.weathermap.presentation.models.enums.Direction
import com.blogspot.soyamr.weathermap.presentation.models.enums.WeatherDescription
import com.blogspot.soyamr.weathermap.presentation.utils.Utils
import com.blogspot.soyamr.domain.models.Weather as DomainWeather

fun DomainWeather.toPresenter() =
    Weather(
        cityName,
        humidity.toString(),
        temp.toString(),
        "%.2f".format(Utils.convertTOmmHg(pressure)) + " mm Hg",
        windSpeed.toInt().toString(),
        WeatherDescription.getStringId(generalDescription),
        WeatherDescription.getImageSrcId(generalDescription),
        Utils.getUrl(iconCode)
    )