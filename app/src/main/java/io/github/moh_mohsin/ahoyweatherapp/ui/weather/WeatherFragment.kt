package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.util.DebugLogger
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherDetail
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.databinding.WeatherFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.daily.DailyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.daily.HourlyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.getDateDiff
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber
import java.util.*

class WeatherFragment : Fragment(R.layout.weather_fragment) {

    private val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel by viewModels<WeatherViewModel>()

    lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyWeatherAdapter = HourlyWeatherAdapter()
        dailyWeatherAdapter = DailyWeatherAdapter()

        binding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        binding.dailyWeatherList.adapter = dailyWeatherAdapter

        Handler(Looper.myLooper()!!).post {

            runWithPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION) {
                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            viewModel.getWeather(it.latitude, it.longitude).observe(viewLifecycleOwner) { result ->
                                when(result){
                                    is Result.Error -> {
                                        toast(result.exception.msg)
                                    }
                                    Result.Loading -> showLoading(true)
                                    is Result.Success -> {
                                        showLoading(false)
                                        bindViews(result.data)
                                    }
                                }
                            }
                        } ?: toast("Location not available")
                    }
            }
            viewModel
        }
    }

    private fun showLoading(show: Boolean){
        binding.loading.showOrHide(show)
        binding.content.showOrHide(!show)
    }

    private fun bindViews(weatherInfo: WeatherInfo) {
        val todayWeather = weatherInfo.daily.firstOrNull { it.dt.getDateDiff(Date()) == 0L }
        Timber.d("todayWeather: $todayWeather")
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
}