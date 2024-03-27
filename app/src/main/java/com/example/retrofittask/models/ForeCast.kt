package com.example.retrofittask.models

data class ForeCast(
    val city: City1,
    val cnt: Int,
    val cod: String,
    val list: List<Base>,
    val message: Int
)