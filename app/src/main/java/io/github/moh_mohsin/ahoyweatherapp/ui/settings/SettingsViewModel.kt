package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.moh_mohsin.ahoyweatherapp.domain.CleanWeatherCacheUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val cleanWeatherCacheUseCase: CleanWeatherCacheUseCase,
) : ViewModel() {

    fun cleanWeatherCache() {
        viewModelScope.launch {
            cleanWeatherCacheUseCase()
        }
    }
}