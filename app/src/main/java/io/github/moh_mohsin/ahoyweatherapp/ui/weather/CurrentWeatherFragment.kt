package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.util.toast


class CurrentWeatherFragment : WeatherFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getWeatherForCurrentLocation()
        } else {
            // use post to solve and issue with the permission library crashing because of FragmentManager
            Handler(Looper.myLooper()!!).post {
                runWithPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
                    getWeatherForCurrentLocation()
                }
            }
        }
    }

    override fun retry() {
        getWeatherForCurrentLocation()
    }

    override fun getTitle(weatherInfo: WeatherInfo) = weatherInfo.timezone

    @SuppressLint("MissingPermission")
    private fun getWeatherForCurrentLocation() {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val pendingIntent = PendingIntent.getActivities(
            requireContext(), 0, arrayOf(Intent()),
            PendingIntent.FLAG_ONE_SHOT
        )
        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), pendingIntent)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    subscribe(it.latitude, it.longitude)
                } ?: run {
                    showRetry(true)
                    toast(R.string.location_not_available)
                }
            }
    }
}