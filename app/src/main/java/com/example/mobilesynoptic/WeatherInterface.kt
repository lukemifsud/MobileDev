package com.example.mobilesynoptic

import retrofit2.Call
import retrofit2.http.GET

interface WeatherInterface {

    @GET("api/v1/weather/valletta")
    fun getWeatherValletta(): Call<Valletta>

    @GET("api/v1/weather/rome")
    fun getWeatherRome(): Call<Rome>

    @GET("api/v1/weather/paris")
    fun getWeatherParis(): Call<Paris>


}