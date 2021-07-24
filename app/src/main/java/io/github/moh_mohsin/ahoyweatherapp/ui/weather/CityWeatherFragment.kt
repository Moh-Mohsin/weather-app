package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
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

class CityWeatherFragment : Fragment(R.layout.weather_fragment) {

    private val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel by viewModels<WeatherViewModel>()
    private val args by navArgs<CityWeatherFragmentArgs>()

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyWeatherAdapter = HourlyWeatherAdapter()
        dailyWeatherAdapter = DailyWeatherAdapter()

        binding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        binding.dailyWeatherList.adapter = dailyWeatherAdapter

        (requireActivity() as MainActivity).supportActionBar?.title = args.city.name
        subscribe(args.city.lat, args.city.lng)

        binding.retry.setOnClickListener {
            subscribe(args.city.lat, args.city.lng)
        }
    }

    private fun subscribe(lat: Double, lon: Double) {
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) { result ->
            Timber.d("City Weather: $result")
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