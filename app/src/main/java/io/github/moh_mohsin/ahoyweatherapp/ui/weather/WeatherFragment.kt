package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.WeatherFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

class WeatherFragment : Fragment(R.layout.weather_fragment) {

    private val binding by viewBinding(WeatherFragmentBinding::bind)
    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.weather.observe(viewLifecycleOwner) {
            binding.text.text = "Result: $it"
        }
    }
}