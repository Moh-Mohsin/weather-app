package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import io.github.moh_mohsin.ahoyweatherapp.MainActivity

class CityWeatherFragment : WeatherFragment() {
    private val args by navArgs<CityWeatherFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = args.city.name
        subscribe(args.city.lat, args.city.lng)
    }

    override fun retry() {
        subscribe(args.city.lat, args.city.lng)
    }

}