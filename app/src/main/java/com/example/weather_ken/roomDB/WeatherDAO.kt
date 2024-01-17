package com.example.weather_ken.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDAO {
    @Insert
    fun insertWeatherHistory(weatherHistory: WeatherHistory)

    @Query("SELECT * FROM weather_history ORDER BY id")
    fun getWeatherHistory():  LiveData<List<WeatherHistory>>

    @Query("DELETE FROM weather_history")
    fun clearWeatherHistory()
}