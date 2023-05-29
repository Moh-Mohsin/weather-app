package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.moh_mohsin.ahoyweatherapp.data.State
import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings
import io.github.moh_mohsin.ahoyweatherapp.domain.CleanWeatherCacheUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.GetSettingsUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.SaveSettingsUseCase
import io.github.moh_mohsin.ahoyweatherapp.util.toLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val cleanWeatherCacheUseCase: CleanWeatherCacheUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<State<Settings>>(State.Idle)
    val state = _state.toLiveData()
    init {
        viewModelScope.launch {
            val settings = getSettingsUseCase()
            _state.value = State.Data(settings)
        }
    }

    private fun cleanWeatherCache() {
        viewModelScope.launch {
            cleanWeatherCacheUseCase()
        }
    }

    fun updateSettings(settings: Settings){
        viewModelScope.launch {
            saveSettingsUseCase(settings = settings)
            cleanWeatherCache()
            _state.value = State.Data(settings)
        }
    }
}