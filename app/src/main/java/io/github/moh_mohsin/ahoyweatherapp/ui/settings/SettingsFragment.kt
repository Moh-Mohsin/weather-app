package io.github.moh_mohsin.ahoyweatherapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.SettingsFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val viewModel by viewModels<SettingsViewModel>()
    val binding by viewBinding(SettingsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cleanWeatherCache()
    }
//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.root_preferences, rootKey)
//    }
//
//    override fun onPreferenceTreeClick(preference: Preference): Boolean {
//
//        val tempScaleKey = requireContext().resources.getString(R.string.temp_scale_key)
//        when (preference.key) {
//            tempScaleKey -> {
//                viewModel.cleanWeatherCache()
//            }
//            else -> {
//            }
//        }
//        return super.onPreferenceTreeClick(preference)
//    }
}