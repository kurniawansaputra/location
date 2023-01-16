package id.go.kebumenkab.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import id.go.kebumenkab.location.databinding.ActivityMainBinding
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

        init()
    }

    private fun init() {
        setListener()
    }

    private fun setListener() {
        binding.buttonLocation.setOnClickListener {
            if (checkPermission()) {
                if (SmartLocation.with(this).location().state().locationServicesEnabled()) {
                    val intent = Intent(this, LocationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Hidupkan location service anda", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequestPermission()
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

    private fun setRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mapPermissions, REQUEST_CODE_MAP_PERMISSIONS)
        }
    }

    companion object {
        private const val REQUEST_CODE_MAP_PERMISSIONS = 1000
    }
}