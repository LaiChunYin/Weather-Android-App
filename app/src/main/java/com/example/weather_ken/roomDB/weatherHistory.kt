package com.example.weather_ken.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity
@Entity(tableName = "weather_history")
class WeatherHistory(
    val address: String,
    val temperature: Double,
    val humidity: Double,
    val weather_condition: String,
    val time: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    override fun toString(): String {
        return "Weather(id=${id}, temp=${temperature}, humid=${humidity}, cond=${weather_condition}, time=${time})"
    }
}