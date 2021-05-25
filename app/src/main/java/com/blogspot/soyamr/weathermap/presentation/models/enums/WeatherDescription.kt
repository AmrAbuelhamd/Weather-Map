package com.blogspot.soyamr.weathermap.presentation.models.enums

import com.blogspot.soyamr.weathermap.R

enum class WeatherDescription(val valueFromServer: String, val strId: Int, val imgSrcId: Int) {
    BrokenClouds("broken clouds", R.string.broken_clouds, R.drawable.broken_clouds),
    ClearSky("clear sky", R.string.clear_sky, R.drawable.clear_sky),
    FewClouds("few clouds", R.string.few_clouds, R.drawable.few_clouds),
    Mist("mist", R.string.mist, R.drawable.mist),
    Rain("rain", R.string.rain, R.drawable.rain),
    ScatteredClouds("scattered clouds", R.string.scattered_clouds, R.drawable.scattered_clouds),
    ShowerRain("shower Rain", R.string.shower_rain, R.drawable.shower_rain),
    Snow("Snow", R.string.snow, R.drawable.snow),
    Thunderstorm("Thunderstorm", R.string.thunderstorm, R.drawable.thunderstorm),
    PlaceHolder("Nothing", R.string.place_holder, R.drawable.thunderstorm);

    companion object {
        fun getImageSrcId(strReturnedFromServer: String) =
            values().find { it.valueFromServer == strReturnedFromServer }?.imgSrcId
                ?: PlaceHolder.imgSrcId

        fun getStringId(strReturnedFromServer: String) =
            values().find { it.valueFromServer == strReturnedFromServer }?.strId
                ?: PlaceHolder.strId
    }
}