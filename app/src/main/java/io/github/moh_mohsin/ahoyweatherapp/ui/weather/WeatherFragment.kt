package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.MainActivity
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.databinding.WeatherFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.DailyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.HourlyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.getDateDiff
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
abstract class WeatherFragment : Fragment(R.layout.weather_fragment) {

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

        binding.retry.setOnClickListener {
            retry()
        }
    }

    abstract fun retry()

    abstract fun getTitle(weatherInfo: WeatherInfo): String

    fun subscribe(lat: Double, lon: Double) {
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

    fun showRetry(show: Boolean) {
        binding.retry.showOrHide(show)
    }

    @SuppressLint("SetTextI18n")
    private fun bindViews(weatherInfo: WeatherInfo) {
        val todayWeather = weatherInfo.daily.firstOrNull { it.dt.getDateDiff(Date()) == 0L }
        Timber.d("todayWeather: $todayWeather")

        (requireActivity() as MainActivity).supportActionBar?.title = getTitle(weatherInfo)
        todayWeather?.let {
            binding.temp.text = "${it.temp.day}°"
            binding.minMaxTemp.text = "${it.temp.max}°  /  ${it.temp.min}°"
            it.weather.firstOrNull()?.let { weather ->
                binding.weatherDesc.text = weather.description
                binding.weatherIcon.load(weather.icon)
            }
            binding.humidity.text = getString(R.string.humidity_value, it.humidity)
            val windSpeedStr = when (weatherInfo.tempScale) {
                TempScale.METRIC -> getString(R.string.meter_second)
                TempScale.IMPERIAL -> getString(R.string.miles_hour)
            }
            binding.windSpeed.text =
                getString(R.string.wind_speed_value, it.windSpeed, windSpeedStr)

        } ?: run {
            Timber.d("should update cache...")
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