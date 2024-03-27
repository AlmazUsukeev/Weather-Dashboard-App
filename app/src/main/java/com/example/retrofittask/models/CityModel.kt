package com.example.retrofittask.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("city")
data class CityModel (
    @PrimaryKey(autoGenerate = true)
    val id:Int? =null ,
    @ColumnInfo("name")
    val name:String,
    @ColumnInfo("isFavorite")
    var isFavorite:Boolean = false,
)