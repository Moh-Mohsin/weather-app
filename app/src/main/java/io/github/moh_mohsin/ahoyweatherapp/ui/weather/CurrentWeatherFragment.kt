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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.permissionx.guolindev.PermissionX
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
                    if (allGranted) {
                        showLoading()
                        getWeatherForCurrentLocation()
                    } else {
                        showGrantLocationAccessButton()
                    }
                }
        }
    }

    private fun showGrantLocationAccessButton() {
        binding.composeView.setContent {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Button(onClick = { requestLocationAccess() }) {
                    Text(text = stringResource(id = R.string.allow_location_access))
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
                subscribeCompose(location?.latitude, location?.longitude)
                if (location == null)
                    toast(R.string.location_not_available)
            }
    }
}