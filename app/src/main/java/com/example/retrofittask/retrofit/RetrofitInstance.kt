package com.example.retrofittask.retrofit

import com.example.retrofittask.Units
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Units.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}