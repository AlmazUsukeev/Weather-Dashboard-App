package com.example.retrofittask.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofittask.R
import com.example.retrofittask.databinding.VpItemListBinding
import com.example.retrofittask.models.ForeCast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class VpAdapter: ListAdapter<ForeCast, VpAdapter.VpViewHolder>(Comparator()) {
    class VpViewHolder(private val binding:VpItemListBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind (data:ForeCast) {
            binding.vpCityNameItem.text = data.city.name
            binding.vpCityTempItem.text = data.list[getWeatherTempNow(data.city.timezone,data.city.name)].main.temp.toInt().toString()
            binding.vpDateItem.text = data.list[0].dt_txt.take(10)
            binding.vpTvWind.text = "${data.list[0].wind.speed.toInt()}m/s"
            binding.vpTvHumidity.text = "${data.list[0].main.humidity}%"
            binding.vpTvTime12.text = "${data.list[2].main.temp.toInt().toString()}°"
            binding.vpTvTime14.text = "${data.list[3].main.temp.toInt().toString()}°"
            binding.vpTvTime16.text = "${data.list[4].main.temp.toInt().toString()}°"
            binding.vpTvTime18.text = "${data.list[5].main.temp.toInt().toString()}°"
            binding.vpTvTepmNow.text =data.list[getWeatherTempNow(data.city.timezone,data.city.name)].main.temp.toInt().toString()+"°"
            binding.ivIcon12.setImageResource(getIcon(data.list[2].weather[0].main))
            binding.ivIcon14.setImageResource(getIcon(data.list[3].weather[0].main))
            binding.ivIcon16.setImageResource(getIcon(data.list[4].weather[0].main))
            binding.ivIcon18.setImageResource(getIcon(data.list[5].weather[0].main))
            binding.ivIconNow.setImageResource(getIcon(data.list[getWeatherTempNow(data.city.timezone,data.city.name)].weather[0].main))
            when(data.city.name){
                "London" -> {binding.ivBack.setImageResource(R.drawable.vp_2)
                    binding.constraint1.setBackgroundColor(Color.parseColor("#5FCD7E"))
                    binding.constraint2.setBackgroundColor(Color.parseColor("#5FCD7E"))}
                "Dubai"  -> {binding.ivBack.setImageResource(R.drawable.vp_3)
                    binding.constraint1.setBackgroundColor(Color.parseColor("#F3733C"))
                    binding.constraint2.setBackgroundColor(Color.parseColor("#F3733C"))}
                    else ->{binding.ivBack.setImageResource(R.drawable.vp_backraound_1)}
            }
        }

        fun getIcon(main:String):Int{
           val icon = when(main){
                "Rain"-> R.drawable.vector__7_
                "Clouds"-> R.drawable.vector_cloud_sun
                "Clear"-> R.drawable.vector_sun
                else ->R.drawable.vector_cloud_sun
            }
            return icon
        }

        private fun getWeatherTempNow(timeZone: Int, cityName: String): Int {
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
            val timeFormat = SimpleDateFormat("HH")
            val time  = timeFormat.format(finalCityDate)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpViewHolder {
        val binding = VpItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VpViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class Comparator():DiffUtil.ItemCallback<ForeCast>(){
        override fun areItemsTheSame(oldItem: ForeCast, newItem: ForeCast): Boolean {
            return oldItem.city.id == newItem.city.id
        }

        override fun areContentsTheSame(oldItem: ForeCast, newItem: ForeCast): Boolean {
            return  oldItem == newItem
        }


    }
}