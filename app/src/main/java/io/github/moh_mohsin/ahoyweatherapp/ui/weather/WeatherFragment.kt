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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import io.github.moh_mohsin.ahoyweatherapp.MainActivity
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.databinding.WeatherFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.DailyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.HourlyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.getDateDiff
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import timber.log.Timber
import java.util.*


class WeatherFragment : Fragment(R.layout.weather_fragment) {

    private val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel by viewModels<WeatherViewModel>()

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hourlyWeatherAdapter = HourlyWeatherAdapter()
        dailyWeatherAdapter = DailyWeatherAdapter()

        binding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        binding.dailyWeatherList.adapter = dailyWeatherAdapter

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

        binding.retry.setOnClickListener {
            getWeatherForCurrentLocation()
        }
    }

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

    private fun subscribe(lat: Double, lon: Double) {
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    showContent(false)
                    showRetry(true)
                    toast(result.exception.msg)
                }
                Result.Loading -> {
                    showLoading(true)
                    showContent(false)
                    showRetry(false)
                }
                is Result.Success -> {
                    showContent(true)
                    showLoading(false)
                    showRetry(false)
                    bindViews(result.data)
                }
            }
        }
    }

    private fun showContent(show: Boolean) {
        binding.content.showOrHide(show)
    }

    private fun showLoading(show: Boolean) {
        binding.loading.showOrHide(show)
    }

    private fun showRetry(show: Boolean) {
        binding.retry.showOrHide(show)
    }

    private fun bindViews(weatherInfo: WeatherInfo) {
        val todayWeather = weatherInfo.daily.firstOrNull { it.dt.getDateDiff(Date()) == 0L }
        Timber.d("todayWeather: $todayWeather")

        (requireActivity() as MainActivity).supportActionBar?.title = weatherInfo.timezone
        todayWeather?.let {
            binding.temp.text = "${it.temp.day}°"
            binding.minMaxTemp.text = "${it.temp.max}°  /  ${it.temp.min}°"
            it.weather.firstOrNull()?.let { weather ->
                binding.weatherDesc.text = weather.description
                binding.weatherIcon.load(weather.icon)
            }
        } ?: run {
            toast("Updating cache...")
        }
        val hourly = weatherInfo.hourly.filter { it.dt.getDateDiff(Date()) == 0L }
        val daily = weatherInfo.daily.drop(1) //remove first day since its the current day

        Timber.d("hourly: ${hourly.size}")
        Timber.d("daily: ${daily.size}")
        Timber.d("current: ${weatherInfo.current}")
        hourlyWeatherAdapter.submitList(hourly)
        dailyWeatherAdapter.submitList(daily)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}