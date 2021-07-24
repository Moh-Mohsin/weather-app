package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherDescDto
import io.github.moh_mohsin.ahoyweatherapp.domain.GetWeatherUseCase
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

class WeatherViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val getWeatherUseCase by instance<GetWeatherUseCase>()
    private val cityRepository by instance<CityRepository>()

    fun getWeather(lat: Double, lon: Double): LiveData<Result<WeatherInfo>> {
        return getWeatherUseCase(lat, lon).asLiveData()
    }

    init {
        viewModelScope.launch {
            //TODO: remove later when schema is solid, used to generate db file for pre-population of Room
            Timber.d("db: start")
            val textFile = app.applicationContext.resources.openRawResource(R.raw.simplemaps_worldcities).bufferedReader().use { it.readText() }
            Timber.d("db: file read: size = ${textFile.length}")
            val cityDtos: List<CityDto> = Gson().fromJson(textFile, object : TypeToken<List<CityDto>>() {}.type)
            Timber.d("db: cityDtos ready: size = ${cityDtos.size}")
            cityRepository.insertAll(cityDtos)
            Timber.d("db: insertion done")
        }
    }
}