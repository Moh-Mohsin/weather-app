package io.github.moh_mohsin.ahoyweatherapp.data.repository
import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings

interface SettingsRepository {
    suspend fun getSettings(): Settings

    suspend fun saveSettings(settings: Settings)
}