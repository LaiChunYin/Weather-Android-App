package com.example.weather_ken.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [WeatherHistory::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun weatherDAO() : WeatherDAO

    companion object{

        private var db : AppDB? = null

        private const val NUMBER_OF_THREADS = 4
        val databaseQueryExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDB(context: Context) : AppDB?{
            if (db == null){
                db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "com.example.weather_ken.roomDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return db
        }

    }

}