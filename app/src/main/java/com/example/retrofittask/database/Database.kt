package com.example.retrofittask.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.retrofittask.model.CityModel

@Database(entities = [CityModel::class], version = 1, exportSchema = false)
abstract class Database:RoomDatabase() {

    abstract fun cityDao():com.example.retrofittask.database.Dao

    companion object{
        @Volatile
        private var INSTANCE: com.example.retrofittask.database.Database? = null

        fun getDatabase(context: Context): com.example.retrofittask.database.Database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.example.retrofittask.database.Database::class.java,
                    "city_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }



}