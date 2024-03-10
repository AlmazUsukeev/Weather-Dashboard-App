package com.example.retrofittask.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.retrofittask.Units
import com.example.retrofittask.adapter.WeatherAdapter
import com.example.retrofittask.database.Database
import com.example.retrofittask.databinding.ActivityMainBinding
import com.example.retrofittask.model.CityModel
import com.example.retrofittask.model.CurrentWeather
import com.example.retrofittask.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),WeatherAdapter.ItemOnClickDelete {

    val cityList: List<CityModel> = listOf(
        CityModel(id = null, name = "Tokyo", isFavorite = false),
        CityModel(id = null, name = "New York", isFavorite = true),
        CityModel(id = null, name = "London", isFavorite = false),
        CityModel(id = null, name = "Beijing", isFavorite = false),
        CityModel(id = null, name = "Paris", isFavorite = false),
        CityModel(id = null, name = "Delhi", isFavorite = false),
        CityModel(id = null, name = "Moscow", isFavorite = true),
        CityModel(id = null, name = "Istanbul", isFavorite = false),
        CityModel(id = null, name = "Dubai", isFavorite = true),
        CityModel(id = null, name = "Seoul", isFavorite = false),
        CityModel(id = null, name = "Mexico City", isFavorite = false),
        CityModel(id = null, name = "Cairo", isFavorite = false),
        CityModel(id = null, name = "Rome", isFavorite = false),
        CityModel(id = null, name = "Sydney", isFavorite = false)
    )

    lateinit var binding:ActivityMainBinding
    lateinit var adapter:WeatherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val db = Database.getDatabase(this)
        adapter = WeatherAdapter(this)
        binding.rvFavoriteCity.adapter = adapter

        db.cityDao().getFavoriteCity().observe(this, Observer {
            lifecycleScope.launch {
               val weatherList =  getWeather(it)
                adapter.submitList(weatherList)
            }
        })
        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(this, "не успел!", Toast.LENGTH_SHORT).show()
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

    suspend fun getWeather(it:List<CityModel>):List<CurrentWeather>{
       val weatherData = mutableListOf<CurrentWeather>()
        for(city in it){
            val response  = RetrofitInstance.api.getCurrentWeather(city.name,"metric",Units.API_KEY)
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


        lifecycleScope.launch(Dispatchers.IO){

            val upCity = db.cityDao().findCityByName(city)
            if(upCity != null){
                upCity.isFavorite = false
                db.cityDao().updateCity(upCity)
            }

        }
    }

}