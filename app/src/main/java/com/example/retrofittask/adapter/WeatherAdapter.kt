package com.example.retrofittask.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofittask.R
import com.example.retrofittask.model.CurrentWeather
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class WeatherAdapter(val itemOnClickDelete: ItemOnClickDelete): ListAdapter<CurrentWeather,WeatherAdapter.WeatherViewHolder>(Comparator()) {
    class WeatherViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvTemp:TextView = view.findViewById(R.id.tv_temp)
        val tvTitle:TextView = view.findViewById(R.id.tv_name)
        val metric:TextView = view.findViewById(R.id.tv_metric)
        val tvTime:TextView = view.findViewById(R.id.tv_time)
        val ibDelete:ImageButton = view.findViewById(R.id.ib_delete)
        val tvDescription:TextView = view.findViewById(R.id.tv_description)
        val icon:ImageView = view.findViewById(R.id.iv_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_list_item,parent,false)
        return WeatherViewHolder(view)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
            val cityWeather = getItem(position)
            val time = cityWeather.dt!!.toLong()
            val localTimeString = convertUnixTimeToTimeAMPM(time)
            val i = cityWeather.weather?.get(0)!!.icon
            val localDateString = convertUnixTimeToDate(time)
            val weather = cityWeather.weather?.get(0)!!.main
                holder.tvTemp.text = "${cityWeather.main!!.temp.toInt()}"
            holder.tvTitle.text = cityWeather.name
            holder.metric.text = "°c"
            holder.tvTime.text = localTimeString
            holder.ibDelete.setOnClickListener {
                itemOnClickDelete.deleteFromFavorite(cityWeather.name.toString())
            }
            holder.tvDescription.text = weather
        when (weather) {
            "Clouds" -> holder.icon.setImageResource(R.drawable.sunc)
            "Clear" -> holder.icon.setImageResource(R.drawable.sun)
            "Rain" -> holder.icon.setImageResource(R.drawable.frame)
            else->holder.icon.setImageResource(R.drawable.frame)
        }

    }

    class Comparator():DiffUtil.ItemCallback<CurrentWeather>(){
        override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
            return  oldItem == newItem
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUnixTimeToTimeAMPM(unixTime:Long):String{
        val instant =Instant.ofEpochSecond(unixTime)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return localDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUnixTimeToDate(unixTime: Long): String {
        val instant = Instant.ofEpochSecond(unixTime)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return localDateTime.format(formatter)
    }

    interface ItemOnClickDelete{
        fun deleteFromFavorite(city:String)
    }
}
