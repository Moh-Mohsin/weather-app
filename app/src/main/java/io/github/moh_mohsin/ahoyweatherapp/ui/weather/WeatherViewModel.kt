package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.domain.GetWeatherUseCase
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {
    fun getWeather(lat: Double, lon: Double): LiveData<Result<WeatherInfo>> {
        return getWeatherUseCase(lat, lon).asLiveData()
    }
}