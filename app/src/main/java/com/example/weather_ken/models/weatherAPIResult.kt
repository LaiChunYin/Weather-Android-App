package com.example.weather_ken.models

import com.example.weather_ken.roomDB.WeatherHistory

data class WeatherAPIResult(
    val address: String,
    val currentConditions: CurrentConditions
){
    fun convertToWeatherHistory(): WeatherHistory{
        return WeatherHistory(this.address,
                              currentConditions.temp,
                              currentConditions.humidity,
                              currentConditions.conditions,
                              currentConditions.datetime)
    }
}

data class CurrentConditions(
    val datetime: String,
    val temp: Double,
    val humidity: Double,
    val conditions: String,
)