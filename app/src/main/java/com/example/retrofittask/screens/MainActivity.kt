package com.example.retrofittask.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.retrofittask.Units
import com.example.retrofittask.adapter.WeatherAdapter
import com.example.retrofittask.database.Database

import com.example.retrofittask.databinding.ActivityMainBinding
import com.example.retrofittask.models.CityModel
import com.example.retrofittask.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),WeatherAdapter.ItemOnClickDelete {
    val cityList: List<CityModel> = listOf(
        CityModel(id = null, name = "Tokyo", isFavorite = false),
        CityModel(id = null, name = "New York", isFavorite = false),
        CityModel(id = null, name = "London", isFavorite = true),
        CityModel(id = null, name = "Beijing", isFavorite = false),
        CityModel(id = null, name = "Paris", isFavorite = false),
        CityModel(id = null, name = "Delhi", isFavorite = false),
        CityModel(id = null, name = "Moscow", isFavorite = false),
        CityModel(id = null, name = "Istanbul", isFavorite = false),
        CityModel(id = null, name = "Dubai", isFavorite = true),
        CityModel(id = null, name = "Seoul", isFavorite = false),
        CityModel(id = null, name = "Mexico City", isFavorite = false),
        CityModel(id = null, name = "Cairo", isFavorite = false),
        CityModel(id = null, name = "Rome", isFavorite = false),
        CityModel(id = null, name = "Sydney", isFavorite = false),
        CityModel(id = null, name = "Tehran", isFavorite = true)
    )
    lateinit var binding:ActivityMainBinding
    lateinit var adapter:WeatherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Database.getDatabase(this)
        window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        adapter = WeatherAdapter(this)
        binding.rvFavoriteCity.adapter = adapter

        db.cityDao().getFavoriteCity().observe(this, Observer {
            lifecycleScope.launch {
               val weatherList =  getWeather(it)
                adapter.submitList(weatherList)
            }
        })
        binding.floatingActionButton.setOnClickListener {
           val intent = Intent(this,DetailsWeather::class.java)
            startActivity(intent)
        }

        db.cityDao().getAllCity().observe(this, Observer {city->
            if(city.isEmpty()){
                lifecycleScope.launch(Dispatchers.IO) {
                    db.cityDao().insertCityList(cityList)
                }
            }
        })

        binding.fbAddActivity.setOnClickListener {
            val intent = Intent(this,AddCity::class.java)
            startActivity(intent)
        }

    }

    suspend fun getWeather(it:List<CityModel>):List<com.example.retrofittask.models.ForeCast>{
       val weatherData = mutableListOf<com.example.retrofittask.models.ForeCast>()
        for(city in it){
            val response  = RetrofitInstance.api.getWeatherForeCast(city.name,"metric",Units.API_KEY)
            if (response.isSuccessful){
                val weatherResponse = response.body()
                weatherResponse.let {
                    if (it != null) {
                        weatherData.add(it)
                    }
                }
            }
        }
        return weatherData
    }

    override fun deleteFromFavorite(city: String) {
        val db = Database.getDatabase(this)
        Toast.makeText(this, city, Toast.LENGTH_SHORT).show()
        lifecycleScope.launch(Dispatchers.IO){
            val upCity = db.cityDao().findCityByName(city)
            if(upCity != null){
                db.cityDao().updateIsFavorite(upCity.name)
            }

        }
    }

}