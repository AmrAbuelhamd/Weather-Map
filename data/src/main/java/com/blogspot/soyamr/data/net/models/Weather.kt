package com.blogspot.soyamr.data.net.models


import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)