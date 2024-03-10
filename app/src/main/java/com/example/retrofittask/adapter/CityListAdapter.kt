package com.example.retrofittask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.retrofittask.R
import com.example.retrofittask.model.CityModel

class CityListAdapter(val itemOnClick: ItemOnClick):ListAdapter<CityModel,CityListAdapter.CityListViewHolder>(Comparator()) {
    class CityListViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val title:TextView = view.findViewById(R.id.tv_title)
        val ibSave:ImageButton = view.findViewById(R.id.ib_save)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item_list,parent,false)
        return CityListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
       val city = getItem(position)
        holder.title.text = city.name
        holder.ibSave.setOnClickListener {
            itemOnClick.onClick(city)
        }
    }

    class Comparator():DiffUtil.ItemCallback<CityModel>(){
        override fun areItemsTheSame(oldItem: CityModel, newItem: CityModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CityModel, newItem: CityModel): Boolean {
            return oldItem == newItem
        }

    }
    interface ItemOnClick{
        fun onClick(city:CityModel)
    }

}