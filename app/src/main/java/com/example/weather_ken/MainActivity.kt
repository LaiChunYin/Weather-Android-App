package com.example.weather_ken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.location.Address
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.weather_ken.api.MyInterface
import com.example.weather_ken.api.RetrofitInstance
import com.example.weather_ken.databinding.ActivityMainBinding
import com.example.weather_ken.models.WeatherAPIResult
import com.example.weather_ken.roomDB.WeatherHistory
import com.example.weather_ken.roomDB.WeatherRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherRepository: WeatherRepository
    val tag = "Main"
    val API_KEY = ""
    private var location: Address? = null
    private lateinit var locationHelper: LocationHelper
    private lateinit var api: MyInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        setSupportActionBar(this.binding.menuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Current Weather"

        this.weatherRepository = WeatherRepository(application)
        this.api = RetrofitInstance.retrofitService

        locationHelper = object: LocationHelper(this, this){}

        // showing the current location's weather by default
        locationHelper.getCurrentLocation()

        locationHelper.currentLocation.observe(this) { location ->
            val lat = location.latitude
            val long = location.longitude
            binding.etCity.setText(locationHelper.coordinatesToAddress(lat, long))
        }

        binding.getWeatherBtn.setOnClickListener {
            val locationFromUI : String = binding.etCity.text.toString()
            location = locationHelper.addressToCoordinates(locationFromUI)
            var api: MyInterface = RetrofitInstance.retrofitService

            if(location == null){
                clearWeatherReport()
                Snackbar.make(binding.root, "Cannot find the location", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val weatherFromApi: WeatherAPIResult = api.getWeather(location!!.latitude, location!!.longitude, API_KEY)
                Log.d(tag, "weather from api ${weatherFromApi}")
                showWeatherReport(weatherFromApi.convertToWeatherHistory())
            }
        }

        binding.saveWeatherBtn.setOnClickListener {
//            val temperature = binding.temperature.text.toString().toDoubleOrNull()
//            val humidity = binding.humidity.text.toString().toDoubleOrNull()
//            val condition = binding.weatherCondition.text.toString()
//            val time = binding.time.text.toString()
            val weatherInfoFromUI = getWeatherInfoFromUI()
            val temperature = weatherInfoFromUI["temperature"]?.toDoubleOrNull()
            val humidity = weatherInfoFromUI["humidity"]?.toDoubleOrNull()
            val condition = weatherInfoFromUI["condition"]
            val time = weatherInfoFromUI["time"]

            Log.d(tag, "saving weather ${temperature}, ${humidity}, ${condition}, ${time}")

            lifecycleScope.launch {
                if(temperature == null || humidity == null || condition == null || time == null){
                    Snackbar.make(binding.root, "Invalid data", Snackbar.LENGTH_LONG).show()
                    return@launch
                }

                Log.d(tag, "location is ${location}")
                val city = location!!.locality ?: location!!.adminArea ?: location!!.countryName
                if(city == null){
                    Log.d(tag, "City is null")
                    Snackbar.make(binding.root, "City is null", Snackbar.LENGTH_LONG).show()
                    return@launch
                }

                val currentWeather = WeatherHistory(city, temperature, humidity, condition, time)
                Log.d(tag, "CurrentWeather is ${currentWeather}")
                weatherRepository.insertWeatherHistory(currentWeather)
                Snackbar.make(binding.root, "Current Weather saved", Snackbar.LENGTH_LONG).show()
            }

        }
    }

    // Option Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        if(menu != null) {
            menu.findItem(R.id.current_weather).setVisible(false)
            menu.findItem(R.id.delete_all).setVisible(false)
            menu.findItem(R.id.search_history).setVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun showWeatherReport(weatherHistory: WeatherHistory){
//        binding.temperature.text = weatherHistory.temperature.toString()
//        binding.humidity.text = weatherHistory.humidity.toString()
//        binding.weatherCondition.text = weatherHistory.weather_condition
//        binding.time.text = weatherHistory.time
        binding.temperature.text = "Temperature: ${weatherHistory.temperature.toString()}°C"
        binding.humidity.text = "Humidity: ${weatherHistory.humidity.toString()}%"
        binding.weatherCondition.text = "Weather Condition: ${weatherHistory.weather_condition}"
        binding.time.text = "Time: ${weatherHistory.time}"
        binding.saveWeatherBtn.visibility = View.VISIBLE
    }

    private fun getWeatherInfoFromUI(): HashMap<String, String>{
        val weatherInfo = hashMapOf<String, String>()
        val temperatureWithUnit = binding.temperature.text.toString().split(": ")[1]
        val humidityWithUnit = binding.humidity.text.toString().split(": ")[1]

        weatherInfo["temperature"] = temperatureWithUnit.substring(0, temperatureWithUnit.length - 2)
        weatherInfo["humidity"] = humidityWithUnit.substring(0, humidityWithUnit.length - 1)
        weatherInfo["condition"] = binding.weatherCondition.text.toString().split(": ")[1]
        weatherInfo["time"] = binding.time.text.toString().split(": ")[1]
        Log.d(tag, "weather ui info ${weatherInfo}")

        return weatherInfo
    }
    private fun clearWeatherReport(){
        binding.temperature.text = ""
        binding.humidity.text = ""
        binding.weatherCondition.text = ""
        binding.time.text = ""
        binding.saveWeatherBtn.visibility = View.GONE
    }
}