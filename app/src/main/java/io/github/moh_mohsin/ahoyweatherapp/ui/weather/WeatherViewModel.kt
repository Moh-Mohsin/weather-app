package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.domain.GetWeatherUseCase
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WeatherViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val getWeatherUseCase by instance<GetWeatherUseCase>()

    fun getWeather(lat: Double, lon: Double): LiveData<Result<WeatherInfo>> {
        return getWeatherUseCase(lat, lon).asLiveData()
    }
}