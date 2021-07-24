package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.moh_mohsin.ahoyweatherapp.domain.CleanWeatherCacheUseCase
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SettingsViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val cleanWeatherCacheUseCase by instance<CleanWeatherCacheUseCase>()

    fun cleanWeatherCache() {
        viewModelScope.launch {
            cleanWeatherCacheUseCase()
        }
    }
}