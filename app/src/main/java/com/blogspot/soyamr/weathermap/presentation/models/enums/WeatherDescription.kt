package com.blogspot.soyamr.weathermap.presentation.models.enums

import com.blogspot.soyamr.weathermap.R

enum class WeatherDescription(val valueFromServer: String, val strId: Int, val imgSrcId: Int) {
    BROKEN_CLOUDS("broken clouds", R.string.broken_clouds, R.drawable.broken_clouds),
    CLEAR_SKY("clear sky", R.string.clear_sky, R.drawable.clear_sky),
    FEW_CLOUDS("few clouds", R.string.few_clouds, R.drawable.few_clouds),
    MIST("mist", R.string.mist, R.drawable.mist),
    RAIN("rain", R.string.rain, R.drawable.rain),
    SCATTERED_CLOUDS("scattered clouds", R.string.scattered_clouds, R.drawable.scattered_clouds),
    SHOWER_RAIN("shower Rain", R.string.shower_rain, R.drawable.shower_rain),
    SNOW("Snow", R.string.snow, R.drawable.snow),
    THUNDERSTORM("Thunderstorm", R.string.thunderstorm, R.drawable.thunderstorm),
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