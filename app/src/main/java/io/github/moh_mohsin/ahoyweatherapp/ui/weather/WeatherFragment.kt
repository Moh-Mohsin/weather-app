package io.github.moh_mohsin.ahoyweatherapp.ui.weather

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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
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
import io.github.moh_mohsin.ahoyweatherapp.util.format
import io.github.moh_mohsin.ahoyweatherapp.util.getDateDiff
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import java.util.*


@AndroidEntryPoint
abstract class WeatherFragment : Fragment(R.layout.weather_fragment) {

    val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel by viewModels<WeatherViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    fun subscribeCompose(lat: Double?, lon: Double?) {
        if (lat == null || lon == null) {
            binding.composeView.setContent {
                RetryButton(onClick = {
                    retry()
                })
            }
            return
        }
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) { result ->
            binding.composeView.setContent {
                when (result) {
                    is Result.Error -> {
                        RetryButton {
                            retry()
                        }
                        toast(result.exception.msg)
                    }
                    Result.Loading -> {
                        Loading()
                    }
                    is Result.Success -> {
                        Weather(result.data)
                    }
                }

            }
        }
    }

    fun showLoading(){
        binding.composeView.setContent {
            Loading()
        }
    }

}

@Composable
fun Weather(weatherInfo: WeatherInfo) {

    MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColors else LightColors) {
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
                        style = MaterialTheme.typography.body2,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    todayWeather.weather.firstOrNull()?.let { weather ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = weather.description,
                                style = MaterialTheme.typography.body2,
                            )
                            AsyncImage(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(16.dp),
                                model = weather.icon,
                                contentDescription = "Weather Icon",
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .width(60.dp)
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.humidity_value, todayWeather.humidity),
                        style = MaterialTheme.typography.body2,
                    )
                    val windSpeedStr = when (weatherInfo.tempScale) {
                        TempScale.METRIC -> stringResource(R.string.meter_second)
                        TempScale.IMPERIAL -> stringResource(R.string.miles_hour)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.wind_speed_value, todayWeather.windSpeed, windSpeedStr
                        ),
                        style = MaterialTheme.typography.body2,
                    )
                    val hourly = weatherInfo.hourly.filter { it.dt.getDateDiff(Date()) == 0L }
                    val daily =
                        weatherInfo.daily.drop(1) //remove first day since its the current day

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Row(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        for (hourlyWeather in weatherInfo.hourly) {
                            HourlyWeatherItem(weatherDetail = hourlyWeather)
                        }
                    }
                    Divider()
                    Spacer(modifier = Modifier.height(6.dp))

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
//        println("weather icon: ${weatherDetail.weather.firstOrNull()?.icon}")
        Text(
            text = weatherDetail.dt.format("hh:mm a"),
            style = MaterialTheme.typography.body2,
        )
        AsyncImage(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(weatherDetail.weather.firstOrNull()?.icon)
                .build(),
            contentDescription = "Weather Icon",
        )
        Text(
            text = "${weatherDetail.temp}°",
            style = MaterialTheme.typography.body2,
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
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = daily.dt.format("d, MMM"),
            style = MaterialTheme.typography.body1,
        )
        AsyncImage(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(32.dp)
                .weight(1.0f),
            model = daily.weather.firstOrNull()?.icon,
            contentDescription = "Weather Icon",
        )
        Text(
            text = "${daily.temp.max}° / ${daily.temp.min}°",
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview
@Composable
fun DailyWeatherItemPreview() {
    DailyWeatherItem(daily = weatherInfo.daily.first())
}

@Preview(showBackground = true)
@Composable
fun RetryPreview() {
    RetryButton {

    }
}

@Composable
fun RetryButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    Loading()
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}