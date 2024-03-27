package com.example.retrofittask.models

data class Base(
    val clouds: Clouds2,
    val dt: Int,
    val dt_txt: String,
    val main: Main1,
    val pop: Double,
    val rain: Rain1,
    val sys: Sys1,
    val visibility: Int,
    val weather: List<Weather1>,
    val wind: Wind1
)