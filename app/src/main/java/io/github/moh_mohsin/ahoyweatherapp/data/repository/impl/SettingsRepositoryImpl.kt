package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.github.moh_mohsin.ahoyweatherapp.data.model.Settings
import io.github.moh_mohsin.ahoyweatherapp.data.repository.SettingsRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : SettingsRepository {
    override fun getSettings(): Settings {
        val settingsString = sharedPreferences.getString(SETTINGS_KEY, null)
        Timber.d("settingsString: $settingsString")
        return gson.fromJson(settingsString, Settings::class.java) ?: Settings()
    }

    override suspend fun saveSettings(settings: Settings) {
        sharedPreferences.edit(commit = true) {
            putString(SETTINGS_KEY, gson.toJson(settings))
        }

    }

    companion object {

        private const val SETTINGS_KEY = "SETTINGS_KEY"
    }
}