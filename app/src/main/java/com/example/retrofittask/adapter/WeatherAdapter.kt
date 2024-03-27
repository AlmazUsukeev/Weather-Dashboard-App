package com.example.retrofittask.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.retrofittask.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone


class WeatherAdapter(val itemOnClickDelete: ItemOnClickDelete): ListAdapter<com.example.retrofittask.models.ForeCast,WeatherAdapter.WeatherViewHolder>(Comparator()) {
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


    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvTemp.text = item.list[getWeatherTempNow(item.city.timezone,item.city.name)].main.temp.toInt().toString()
        holder.tvTitle.text = item.city.name
        val timeFormat = SimpleDateFormat("hh:mm a")
        val time  = timeFormat.format(getTimeCity(item.city.timezone,item.city.name))
        holder.tvTime.text = time
        val weather = item.list[getWeatherTempNow(item.city.timezone,item.city.name)].weather[0].main
            holder.ibDelete.setOnClickListener {
                itemOnClickDelete.deleteFromFavorite(item.city.name)
            }
            holder.tvDescription.text = weather
        when (weather) {
            "Clouds" -> holder.icon.setImageResource(R.drawable.sunc)
            "Clear" -> holder.icon.setImageResource(R.drawable.sun)
            "Rain" -> holder.icon.setImageResource(R.drawable.frame)
            else->holder.icon.setImageResource(R.drawable.sunc)
        }
    }
    class Comparator():DiffUtil.ItemCallback<com.example.retrofittask.models.ForeCast>(){
        override fun areItemsTheSame(oldItem: com.example.retrofittask.models.ForeCast, newItem: com.example.retrofittask.models.ForeCast): Boolean {
            return oldItem.city.id == newItem.city.id
        }
        override fun areContentsTheSame(oldItem: com.example.retrofittask.models.ForeCast, newItem: com.example.retrofittask.models.ForeCast): Boolean {
            return  oldItem == newItem
        }
    }
    interface ItemOnClickDelete{
        fun deleteFromFavorite(city:String)
    }

    private fun getWeatherTempNow(timeZone: Int, cityName: String): Int {
        val timeFormat = SimpleDateFormat("HH")
        val time  = timeFormat.format(getTimeCity(timeZone,cityName))
        val index = when(time.toInt()){
            in 0..6 -> 0
            in 6..9 ->1
            in 12..14->2
            in 15..17->3
            in 18..20->4
            else -> 5
        }
        return index
    }
    fun getTimeCity(timeZone:Int,cityName:String):Date{
        val timezoneOffset = timeZone
        val utcTime = System.currentTimeMillis() / 1000
        val cityDate = Date(utcTime * 1000)
        val isDst = TimeZone.getTimeZone(cityName).inDaylightTime(cityDate)
        val cityTime = if (isDst) {
            utcTime + timezoneOffset + 3600
        } else {
            utcTime + timezoneOffset
        }
        val finalCityDate = Date(cityTime * 1000)
        return finalCityDate
    }

}
