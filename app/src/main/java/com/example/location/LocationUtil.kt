package com.example.location

import android.location.Location
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun getDistanceInKm(
    currentLat: Double,
    currentLng: Double,
    destinationLat: Double,
    destinationLng: Double)
: String {
    val currentLoc = Location("currentLoc")
    currentLoc.latitude = currentLat
    currentLoc.longitude = currentLng

    val destinationLoc = Location("destinationLoc")
    destinationLoc.latitude = destinationLat
    destinationLoc.longitude = destinationLng

    val distanceInMeters = currentLoc.distanceTo(destinationLoc)

    val df = DecimalFormat("#.###",  DecimalFormatSymbols(Locale.US))
    df.roundingMode = RoundingMode.CEILING

    return df.format(distanceInMeters / 1000)
}

fun getDistanceInMeters(
    currentLat: Double,
    currentLng: Double,
    destinationLat: Double,
    destinationLng: Double
): String {
    val currentLoc = Location("currentLoc")
    currentLoc.latitude = currentLat
    currentLoc.longitude = currentLng

    val destinationLoc = Location("destinationLoc")
    destinationLoc.latitude = destinationLat
    destinationLoc.longitude = destinationLng

    val distanceInMeters = currentLoc.distanceTo(destinationLoc)

    return distanceInMeters.toInt().toString()
}