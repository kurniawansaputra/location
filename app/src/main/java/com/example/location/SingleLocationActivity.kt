package com.example.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.location.databinding.ActivitySingleLocationBinding
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams

class SingleLocationActivity : AppCompatActivity() {
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var destinationLat: Double = -7.6793076
    private var destinationLng: Double = 109.6671484
    private var distanceInKm: Int = 0

    private lateinit var binding: ActivitySingleLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentLoc()
        setListener()
    }

    private fun getCurrentLoc() {
        val builder = LocationParams.Builder()
            .setAccuracy(LocationAccuracy.HIGH)
            .setDistance(0f)
            .setInterval(5000)

        SmartLocation.with(this)
            .location()
            .continuous()
            .config(LocationParams.BEST_EFFORT)
            .config(builder.build())
            .start {
                currentLat = it.latitude
                currentLng = it.longitude

                validateInput()
                setDetail()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(this).location().stop()
    }

    @SuppressLint("SetTextI18n")
    private fun setDetail() {
        distanceInKm = getDistanceInKm(currentLat, currentLng, destinationLat, destinationLng).toInt()

        binding.apply {
            textCurrentLatLng.text = "Current LatLng: $currentLat, $currentLng"
            textDestinationLatLng.text = "Destination LatLng: $destinationLat, $destinationLng"
            textDistance.text = "Distance: $distanceInKm KM"
        }
    }

    private fun setListener() {
        binding.apply {
            buttonDistance.setOnClickListener {
                Toast.makeText(this@SingleLocationActivity, "Distance: $distanceInKm KM", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput() {
        binding.apply {
            buttonDistance.isEnabled = distanceInKm <= 0.3 && currentLat != 0.0 && currentLng != 0.0
        }
    }
}