package com.example.weather_ken.roomDB

import android.app.Application
import androidx.lifecycle.LiveData

class WeatherRepository(application: Application) {
    //obtain the instance of the database and notesDAO
    private var db : AppDB? = null
    private var weatherDAO = AppDB.getDB(application)?.weatherDAO()

    var weatherHistory : LiveData<List<WeatherHistory>>? = weatherDAO?.getWeatherHistory()

    init {
        this.db = AppDB.getDB(application)
    }

    fun insertWeatherHistory(weatherHistory: WeatherHistory){
        AppDB.databaseQueryExecutor.execute {
            this.weatherDAO?.insertWeatherHistory(weatherHistory)
        }
    }
    
    fun clearWeatherHistory(){
        AppDB.databaseQueryExecutor.execute {
            this.weatherDAO?.clearWeatherHistory()
        }
    }
}