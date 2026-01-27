package com.example.geologger

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LocationAdapter
    private lateinit var database: AppDatabase
    private val apiKey = BuildConfig.WEATHER_API_KEY

    private val REQUEST_LOCATION_PERMISSION = 100
    private val REQUEST_NOTIFICATION_PERMISSION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)

        adapter = LocationAdapter()
        binding.locationRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.locationRecyclerView.adapter = adapter


        binding.startServiceBtn.setOnClickListener {
            if (!isLocationPermissionGranted()) {
                openAppSettings()
                return@setOnClickListener

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!isNotificationPermissionGranted()) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        REQUEST_NOTIFICATION_PERMISSION
                    )
                    return@setOnClickListener
                }
            }

            startLocationService()
        }

        binding.stopServiceBtn.setOnClickListener {
            stopService(Intent(this, LocationService::class.java))
            Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show()

        }

        loadLocations()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Location Service Started", Toast.LENGTH_SHORT).show()
    }

    private fun checkLocationPermission() {
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

    }
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            }
        }

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            }
        }
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
                val wind = response.wind.speed

                binding.weatherText.text =
                    "🌤 Temp: $temp°C\n💨 Wind: $wind m/s"

            } catch (e: Exception) {
                binding.weatherText.text = "Weather error !!"
            }
        }
    }
}
