package com.blogspot.soyamr.data.net.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double? = 0.0
)