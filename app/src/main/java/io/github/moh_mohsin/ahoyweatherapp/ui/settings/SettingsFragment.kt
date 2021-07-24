package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.moh_mohsin.ahoyweatherapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<SettingsViewModel>()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {

        val tempScaleKey = requireContext().resources.getString(R.string.temp_scale_key)
        when (preference.key) {
            tempScaleKey -> {
                viewModel.cleanWeatherCache()
            }
            else -> {
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}