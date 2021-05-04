package com.blogspot.soyamr.data.net

import com.blogspot.soyamr.data.net.models.CityWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCityWeather(@Query("q") cityName: String): CityWeatherResponse
}