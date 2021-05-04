package com.blogspot.soyamr.data.net.models


import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)