package com.blogspot.soyamr.data.net.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val temp: Double?,
    @SerialName("feels_like")
    val feelsLike: Double?,
    @SerialName("temp_min")
    val tempMin: Double?,
    @SerialName("temp_max")
    val tempMax: Double?,
    val pressure: Int?,
    val humidity: Int?,
    @SerialName("sea_level")
    val seaLevel: Int? = 0,
    @SerialName("grnd_level")
    val grndLevel: Int? = 0
)