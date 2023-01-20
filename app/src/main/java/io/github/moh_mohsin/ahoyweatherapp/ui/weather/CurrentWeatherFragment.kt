package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.permissionx.guolindev.PermissionX
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
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
            requestLocationAccess()
        }
        binding.locationAccessButton.setOnClickListener {
            requestLocationAccess()
        }
    }

    private fun requestLocationAccess() {
        Handler(Looper.myLooper()!!).post {
            PermissionX.init(requireActivity())
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList,
                        getString(R.string.location_permission_rationale),
                        getString(android.R.string.ok),
                        getString(android.R.string.cancel),
                    )
                }
                .request { allGranted, _, _ ->
                    binding.composeView.showOrHide(allGranted)
                    binding.locationAccessButton.showOrHide(!allGranted)
                    if (allGranted) {
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

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        val pendingIntent = PendingIntent.getActivities(
            requireContext(), 0, arrayOf(Intent()),
            flag
        )

        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), pendingIntent)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    subscribeCompose(it.latitude, it.longitude)
                } ?: run {
                    showRetry(true)
                    toast(R.string.location_not_available)
                }
            }
    }
}