package com.example.weather_ken.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_ken.R
import com.example.weather_ken.roomDB.WeatherHistory

class WeatherAdapter(private var weatherHistory: MutableList<WeatherHistory>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>()
{
    private val tag = "weatherAdapter"

    inner class WeatherViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder  {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return WeatherViewHolder (view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder , position: Int) {
        val weather = weatherHistory[position]
        Log.d(tag, "weather is ${weather}")

        val cityNameView = holder.itemView.findViewById<TextView>(R.id.city_name)
        val temperatureView = holder.itemView.findViewById<TextView>(R.id.temperature)
        val humidityView = holder.itemView.findViewById<TextView>(R.id.humidity)
        val weatherConditionView = holder.itemView.findViewById<TextView>(R.id.weather_condition)
        val timeView = holder.itemView.findViewById<TextView>(R.id.time)

//        cityNameView.text = weather.address.toString()
//        temperatureView.text = weather.temperature.toString()
//        humidityView.text = weather.humidity.toString()
//        weatherConditionView.text = weather.weather_condition
//        timeView.text = weather.time
        cityNameView.text = weather.address.toString()
        temperatureView.text = "Temperature: ${weather.temperature.toString()}°C"
        humidityView.text = "Humidity: ${weather.humidity.toString()}%"
        weatherConditionView.text = "Weather Condition: ${weather.weather_condition}"
        timeView.text = "Time: ${weather.time}"
    }

    override fun getItemCount(): Int {
        return weatherHistory.size
    }

}