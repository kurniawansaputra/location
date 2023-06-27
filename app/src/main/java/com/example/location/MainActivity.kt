package com.example.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.location.databinding.ActivityMainBinding
import io.nlopez.smartlocation.SmartLocation

class MainActivity : AppCompatActivity() {
    private val mapPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener() {
        binding.apply {
            buttonSingleLocation.setOnClickListener {
                if (checkPermission()) {
                    if (SmartLocation.with(this@MainActivity).location().state().locationServicesEnabled()) {
                        val intent = Intent(this@MainActivity, SingleLocationActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Please, turn on your location service", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    setRequestPermission()
                }
            }
            buttonMultiLocation.setOnClickListener {
                if (SmartLocation.with(this@MainActivity).location().state().locationServicesEnabled()) {
                    val intent = Intent(this@MainActivity, MultipleLocationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Please, turn on your location service", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        var isHasPermission = false
        this.let {
            for (permission in mapPermissions){
                isHasPermission = ActivityCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return isHasPermission
    }

    // Permission for location
    private fun setRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mapPermissions, REQUEST_CODE_MAP_PERMISSIONS)
        }
    }

    companion object {
        private const val REQUEST_CODE_MAP_PERMISSIONS = 1000
    }
}