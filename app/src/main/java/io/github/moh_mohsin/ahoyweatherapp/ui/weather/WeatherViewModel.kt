package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.domain.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class WeatherViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val getWeatherUseCase by instance<GetWeatherUseCase>()

//    private val _weather: MutableStateFlow<Result<WeatherInfo>> = MutableStateFlow(Result.Loading)
    val weather: LiveData<Result<WeatherInfo>> = getWeatherUseCase(35.6897, 139.6922).asLiveData()

//    init {
//        getWeather()
//    }
//
//    fun getWeather() {
//        viewModelScope.launch {
//            val result =
//            _weather.value = result.value
//        }
//    }
}