package com.example.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.location.databinding.ActivityMultipleLocationBinding
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams

class MultipleLocationActivity : AppCompatActivity() {
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var destinationLatList: List<Double> = listOf(-7.6793076, -7.687070464096957)
    private var destinationLngList: List<Double> = listOf( 109.6671484, 109.67235700951036)
    private var distanceInM: Int = 0
    private val distanceList: MutableList<Int> = mutableListOf()

    private lateinit var binding: ActivityMultipleLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultipleLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentLoc()
        setListener()
    }

    private fun getCurrentLoc() {
        val builder = LocationParams.Builder()
            .setAccuracy(LocationAccuracy.HIGH)
            .setDistance(0f)
            .setInterval(2000)

        SmartLocation.with(this)
            .location()
            .continuous()
            .config(LocationParams.BEST_EFFORT)
            .config(builder.build())
            .start {
                currentLat = it.latitude
                currentLng = it.longitude

                setDetail()
                validateInput()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(this).location().stop()
    }

    @SuppressLint("SetTextI18n")
    private fun setDetail() {
        // Clear the existing distances
        distanceList.clear()

        val distanceStringBuilder = StringBuilder()
        val stringBuilder = StringBuilder()

        for (i in destinationLatList.indices) {
            val destinationLat = destinationLatList[i]
            val destinationLng = destinationLngList[i]

            distanceInM = getDistanceInMeters(currentLat, currentLng, destinationLat, destinationLng).toInt()

            // Adding distance to list
            distanceList.add(distanceInM)

            // Use the distance as needed (e.g., display it, store it in a list, etc.)
            distanceStringBuilder.append("${i+1}: $distanceInM Meters, ")

            validateInput()
        }

        // Iterate over the latitude and longitude lists
        for (i in destinationLatList.indices) {
            val latitude = destinationLatList[i]
            val longitude = destinationLngList[i]

            // Append the formatted coordinates to the StringBuilder
            stringBuilder.append("Destination LatLng ${i+1}: $latitude, $longitude\n")
        }

        binding.apply {
            textCurrentLatLng.text = "Current LatLng: $currentLat, $currentLng"
            textDestinationLatLng.text = stringBuilder
            textDistance.text = distanceStringBuilder
        }
    }

    private fun setListener() {
        binding.apply {
            buttonDistance.setOnClickListener {
                val nearestDistance = distanceList.minOrNull() ?: 0.0
                Toast.makeText(this@MultipleLocationActivity, "Nearest Distance: $nearestDistance Meters", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput() {
        binding.apply {
            var isButtonEnabled = false
            for (distanceValue in distanceList) {
                if (distanceValue <= 300 && currentLat != 0.0 && currentLng != 0.0) {
                    isButtonEnabled = true
                    break
                }
            }
            buttonDistance.isEnabled = isButtonEnabled
        }
    }
}