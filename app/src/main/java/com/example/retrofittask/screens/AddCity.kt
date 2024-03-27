package com.example.retrofittask.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.retrofittask.Units
import com.example.retrofittask.adapter.CityListAdapter
import com.example.retrofittask.database.Database
import com.example.retrofittask.databinding.ActivityAddCityBinding
import com.example.retrofittask.models.CityModel
import com.example.retrofittask.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCity : AppCompatActivity(),CityListAdapter.ItemOnClick {
    lateinit var binding:ActivityAddCityBinding
    lateinit var adapter:CityListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        adapter = CityListAdapter(this)
        binding.rvCity.adapter = adapter
        val db = Database.getDatabase(this)
        db.cityDao().getAllCity().observe(this, Observer {list->
            adapter.submitList(list)
        })
        binding.searchView.isIconified =false
        binding.searchView.clearFocus()
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    lifecycleScope.launch {
                        searchCity(newText)
                    }
                }
                db.cityDao().getAllCity().observe(this@AddCity, Observer {
                    adapter.submitList(it)
                })
                return true
            }


        })

    }

    private suspend fun searchCity(city:String) {
        val response  = RetrofitInstance.api.getWeatherForeCast(city,"metric", Units.API_KEY)
        if (response.isSuccessful&&response!=null){
            val weatherResponse = response.body()
            val list = listOf<CityModel>(CityModel(null,weatherResponse!!.city.name,false))
            weatherResponse.let {
                   adapter.submitList(list)
            }

        }
    }

    override fun onClick(city: CityModel) {
        val db = Database.getDatabase(this)
        Toast.makeText(this, "город добавлен!", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO){

            val checkCity = db.cityDao().findCityByName(city.name)
            if(checkCity != null){
                checkCity.isFavorite = true
                db.cityDao().updateCity(checkCity)
            }
            else{
                city.isFavorite = true
                db.cityDao().insertCity(city)

            }
        }
    }
}