package com.example.retrofittask.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.retrofittask.Units
import com.example.retrofittask.adapter.VpAdapter
import com.example.retrofittask.database.Database
import com.example.retrofittask.databinding.ActivityDetailsWeatherBinding
import com.example.retrofittask.models.CityModel
import com.example.retrofittask.models.ForeCast

import com.example.retrofittask.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class DetailsWeather : AppCompatActivity() {
    lateinit var binding:ActivityDetailsWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val adapter = VpAdapter()
        val db = Database.getDatabase(this)
        binding.vpList.adapter = adapter

        db.cityDao().getFavoriteCity().observe(this, Observer {
            lifecycleScope.launch {
                val dataList = getWeather(it)
                adapter.submitList(dataList)
            }
        })
        binding.vpIndicator.attachTo(binding.vpList)

        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.imageButton2.setOnClickListener {
            val intent = Intent(this,AddCity::class.java)
            startActivity(intent)
        }
    }

    suspend fun getWeather(it:List<CityModel>):List<ForeCast>{
        val weatherData = mutableListOf<ForeCast>()
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


}