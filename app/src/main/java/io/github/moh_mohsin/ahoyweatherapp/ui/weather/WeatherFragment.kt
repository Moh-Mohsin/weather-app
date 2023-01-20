package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.MainActivity
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.Daily
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherDetail
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.model.weatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.databinding.WeatherFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.DarkColors
import io.github.moh_mohsin.ahoyweatherapp.ui.LightColors
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.DailyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.ui.weather.adapter.HourlyWeatherAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.*
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
abstract class WeatherFragment : Fragment(R.layout.weather_fragment) {

    val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel by viewModels<WeatherViewModel>()

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        hourlyWeatherAdapter = HourlyWeatherAdapter()
        dailyWeatherAdapter = DailyWeatherAdapter()

        binding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        binding.dailyWeatherList.adapter = dailyWeatherAdapter

        binding.retry.setOnClickListener {
            retry()
        }

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_options, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.settingsFragment)
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    abstract fun retry()

    abstract fun getTitle(weatherInfo: WeatherInfo): String

    fun subscribeCompose(lat: Double, lon: Double) {
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) { result ->
            binding.composeView.setContent {
                when (result) {
                    is Result.Error -> {
                        Text(text = "Error...")
                        toast(result.exception.msg)
                    }
                    Result.Loading -> {
                        Text(text = "Loading...")
                    }
                    is Result.Success -> {
                        Weather(result.data)
                    }
                }

            }
        }
    }

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
}

@Composable
fun Weather(weatherInfo: WeatherInfo) {

    MaterialTheme(colors = if(isSystemInDarkTheme()) DarkColors else LightColors) {
        Surface {

            val todayWeather = weatherInfo.daily.firstOrNull { it.dt.getDateDiff(Date()) == 0L }
            if (todayWeather == null) {
                Text(text = "Please refresh")
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "${todayWeather.temp.day}°",
                        style = MaterialTheme.typography.h3,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${todayWeather.temp.max}°  /  ${todayWeather.temp.min}°",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    todayWeather.weather.firstOrNull()?.let { weather ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(text = weather.description)
                            AsyncImage(
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(horizontal = 8.dp),
                                model = weather.icon,
                                contentDescription = "Weather Icon",
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.humidity_value, todayWeather.humidity),
                    )
                    val windSpeedStr = when (weatherInfo.tempScale) {
                        TempScale.METRIC -> stringResource(R.string.meter_second)
                        TempScale.IMPERIAL -> stringResource(R.string.miles_hour)
                    }
                    Text(
                        text = stringResource(
                            R.string.wind_speed_value, todayWeather.windSpeed, windSpeedStr
                        ),
                    )
                    val hourly = weatherInfo.hourly.filter { it.dt.getDateDiff(Date()) == 0L }
                    val daily = weatherInfo.daily.drop(1) //remove first day since its the current day

                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        for (hourlyWeather in weatherInfo.hourly) {
                            HourlyWeatherItem(weatherDetail = hourlyWeather)
                        }
                    }

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        for (dailyWeather in weatherInfo.daily) {
                            DailyWeatherItem(daily = dailyWeather)
                        }
                    }
//                hourlyWeatherAdapter.submitList(hourly)
//                dailyWeatherAdapter.submitList(daily)

                }
            }
        }
    }
}

@Preview
@Composable
fun WeatherPreview() {
    Weather(weatherInfo = weatherInfo)
}

@Composable
fun HourlyWeatherItem(weatherDetail: WeatherDetail) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = weatherDetail.dt.format("hh:mm a")
        )
        AsyncImage(
            modifier = Modifier
                .size(16.dp)
                .padding(vertical = 8.dp),
            model = weatherDetail.weather.firstOrNull()?.icon,
            contentDescription = "Weather Icon",
        )
        Text(
            text = "${weatherDetail.temp}°"
        )
    }
}

@Preview
@Composable
fun HourlyWeatherItemPreview() {
    HourlyWeatherItem(weatherDetail = weatherInfo.hourly.first())
}


@Composable
fun DailyWeatherItem(daily: Daily) {
    Row (
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = daily.dt.format("hh:mm a")
        )
        AsyncImage(
            modifier = Modifier
                .size(16.dp)
                .padding(vertical = 8.dp)
                .weight(1.0f),
            model = daily.weather.firstOrNull()?.icon,
            contentDescription = "Weather Icon",
        )
        Text(
            text = "${daily.temp.max}° / ${daily.temp.min}°"
        )
    }
}

@Preview
@Composable
fun DailyWeatherItemPreview() {
    DailyWeatherItem(daily = weatherInfo.daily.first())
}