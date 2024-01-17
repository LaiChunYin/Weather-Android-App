package com.example.weather_ken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_ken.adapters.WeatherAdapter
import com.example.weather_ken.databinding.ActivityHistoryBinding
import com.example.weather_ken.roomDB.WeatherHistory
import com.example.weather_ken.roomDB.WeatherRepository
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherRepository: WeatherRepository
    private var weatherHistory = mutableListOf<WeatherHistory>()
    val tag = "History"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Search History"

        weatherAdapter = WeatherAdapter(weatherHistory)
        binding.weatherHistory.adapter = weatherAdapter
        binding.weatherHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
        binding.weatherHistory.addItemDecoration(
            DividerItemDecoration(
                this@HistoryActivity,
                LinearLayoutManager.VERTICAL
            )
        )

        weatherRepository = WeatherRepository(application)
        val savedWeather = weatherRepository.weatherHistory
        savedWeather?.observe(this) { weatherHistory ->
            this.weatherHistory.clear()
            this.weatherHistory.addAll(weatherHistory)
            weatherAdapter.notifyDataSetChanged()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        if(menu != null) {
            menu.findItem(R.id.current_weather).setVisible(true)
            menu.findItem(R.id.search_history).setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.current_weather -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.delete_all -> {
                weatherRepository.clearWeatherHistory()
                weatherAdapter.notifyDataSetChanged()
                Snackbar.make(binding.root, "cleared data", Snackbar.LENGTH_LONG).show()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}