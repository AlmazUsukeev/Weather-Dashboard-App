package com.example.retrofittask.retrofit

import com.example.retrofittask.model.CurrentWeather
import com.example.retrofittask.models.ForeCast
import retrofit2.Call

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
@GET("weather?")
 suspend fun getCurrentWeather(
    @Query("q")
    city:String,
    @Query("units")
    units:String,
    @Query("appid")
    apiKey:String
):Response<CurrentWeather>
 @GET("forecast")
 suspend fun getWeatherForeCast(
    @Query("q")
    city:String,
    @Query("units")
    metric:String,
    @Query("appid")
    apiKey:String,
 ):Response<ForeCast>
}