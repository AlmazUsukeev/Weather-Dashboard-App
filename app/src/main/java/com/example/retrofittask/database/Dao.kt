package com.example.retrofittask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.retrofittask.models.CityModel

@Dao
interface Dao {
    @Query("select *from city where isFavorite = 0")
    fun getAllCity():LiveData<List<CityModel>>
    @Query("select *from city where isFavorite = 1 ")
    fun getFavoriteCity():LiveData<List<CityModel>>
    @Delete
    fun deleteCity(city: CityModel)
    @Insert
    fun insertCity(city: CityModel)
    @Insert
    fun insertCityList(cityList:List<CityModel>)
    @Query("update city set isFavorite = 0 where LOWER(name) = LOWER(:cityName)")
    fun updateIsFavorite(cityName:String)
    @Update
    fun updateCity(city: CityModel)
    @Query("select * from city where LOWER(name) = LOWER(:cityName)")
    fun findCityByName (cityName:String): CityModel

}