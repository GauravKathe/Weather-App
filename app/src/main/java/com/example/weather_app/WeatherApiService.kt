package com.example.weather_app

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "52d6e2c9c7f5ac427e1797324b80da5a",
        @Query("units") units: String = "metric"
    ): WeatherResponse
}