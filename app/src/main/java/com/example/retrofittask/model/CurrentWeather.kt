package com.example.retrofittask.model

import java.util.SimpleTimeZone

data class CurrentWeather(
    val base: String?,
    val clouds: Clouds?,
    val cod: Int?,
    val coord: Coord?,
    val dt: Int?,
    val id: Int?,
    val main: Main?,
    val name: String?,
    val sys: Sys?,
    val visibility: Int?,
    val timeZone:Int,
    val weather: List<Weather>?,
    val wind: Wind?
)