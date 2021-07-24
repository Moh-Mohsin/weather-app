package io.github.moh_mohsin.ahoyweatherapp.ui.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.CitiesFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.databinding.CitySearchFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

class CitiesFragment : Fragment(R.layout.cities_fragment) {

    private val binding by viewBinding(CitiesFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCity.setOnClickListener {
            findNavController().navigate(R.id.citySearchFragment)
        }
    }
}