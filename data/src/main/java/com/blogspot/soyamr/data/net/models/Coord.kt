package com.blogspot.soyamr.data.net.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lon: Double?,
    val lat: Double?
)