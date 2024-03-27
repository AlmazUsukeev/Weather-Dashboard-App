package com.example.retrofittask.retrofit


import com.example.retrofittask.models.ForeCast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

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