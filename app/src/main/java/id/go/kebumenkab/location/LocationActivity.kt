package id.go.kebumenkab.location

import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.go.kebumenkab.location.databinding.ActivityLocationBinding
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import java.math.RoundingMode
import java.text.DecimalFormat

class LocationActivity : AppCompatActivity() {
    private var latitudeA: Double = 0.0
    private var longitudeA: Double = 0.0
    private var latitudeB: Double = -7.6793076
    private var longitudeB: Double = 109.6671484
    private lateinit var distance: String

    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setLocation()
        setListener()
    }

    private fun setLocation() {
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
                latitudeA = it.latitude
                longitudeA = it.longitude
                Log.d("currentLocation", "$latitudeA, $longitudeA")
                setDetail()
            }
    }

    private fun getDistanceInKm(latA: Double, langA: Double, latB: Double, langB: Double): String {
        val locationA = Location("locationA")
        locationA.latitude = latA
        locationA.longitude = langA

        val locationB = Location("locationB")
        locationB.latitude = latB
        locationB.longitude = langB

        val distanceInMeters = locationA.distanceTo(locationB)

        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING

        return df.format(distanceInMeters / 1000)
    }

    private fun setDetail() {
        distance = getDistanceInKm(latitudeA, longitudeA, latitudeB, longitudeB)
        binding.textDistance.text = "$distance KM"
    }

    private fun setListener() {
        binding.buttonDistance.setOnClickListener {
            if (isDeveloperOptionEnabled()) {
                Toast.makeText(this, "$distance", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Anda terindikasi menggunakan fake gps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isDeveloperOptionEnabled(): Boolean {
        return Settings.Secure.getInt(
            this.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 0
    }

    override fun onDestroy() {
        super.onDestroy()
        SmartLocation.with(this).location().stop()
    }
}