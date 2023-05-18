package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings
import io.github.moh_mohsin.ahoyweatherapp.data.repository.SettingsRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(settings: Settings) {
        return settingsRepository.saveSettings(settings)
    }
}