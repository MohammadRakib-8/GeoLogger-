package com.example.geologger

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geologger.data.db.AppDatabase
import com.example.geologger.databinding.ActivityMainBinding
import com.example.geologger.service.LocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LocationAdapter
    private lateinit var database: AppDatabase
    private val apiKey = BuildConfig.WEATHER_API_KEY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)

        adapter = LocationAdapter()
        binding.locationRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.locationRecyclerView.adapter = adapter

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )

        binding.startServiceBtn.setOnClickListener {
            val intent = Intent(this, LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Toast.makeText(
                this@MainActivity,
                "Sucessfully location service started",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.stopServiceBtn.setOnClickListener {
            stopService(Intent(this, LocationService::class.java))
            Toast.makeText(
                this@MainActivity,
                "Sucessfully stop Location service",
                Toast.LENGTH_SHORT
            ).show()
        }
        

        loadLocations()
    }

    private fun loadLocations() {
        CoroutineScope(Dispatchers.Main).launch {
            database.locationDao().getAllLocations().collect { list ->
                adapter.setLocations(list)

                if (list.isNotEmpty()) {
                    val last = list.last()
                    fetchWeather(last.latitude, last.longitude)
                }
            }
        }
    }


    private fun fetchWeather(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitInstance.api.getWeather(
                    latitude,
                    longitude,
                    apiKey
                )

                val temp = response.main.temp
//                val feels = response.main.feels_like
                val wind = response.wind.speed

                binding.weatherText.text =
                    "🌤 Temp: $temp°C\n🤒\n💨 Wind: $wind m/s"

            } catch (e: Exception) {
                binding.weatherText.text = "Weather error !!"
                Toast.makeText(
                    this@MainActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}


//    private fun fetchApiData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = URL("https://jsonplaceholder.typicode.com/posts/1").readText()
//                runOnUiThread {
//                    Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                runOnUiThread {
//                    Toast.makeText(this@MainActivity, "API Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

