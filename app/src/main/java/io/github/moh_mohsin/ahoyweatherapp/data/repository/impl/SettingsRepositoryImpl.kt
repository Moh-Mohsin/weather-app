package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings
import io.github.moh_mohsin.ahoyweatherapp.data.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : SettingsRepository {
    override suspend fun getSettings(): Settings {
        val settingsString = sharedPreferences.getString(SETTINGS_KEY, null)
        return gson.fromJson(settingsString, Settings::class.java) ?: Settings()
    }

    override suspend fun saveSettings(settings: Settings) {
        sharedPreferences.edit {
            putString(gson.toJson(settings), SETTINGS_KEY)
        }
    }

    companion object {

        private const val SETTINGS_KEY = "SETTINGS_KEY"
    }
}