package io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.FavoriteCitiesFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.city.adapter.CityAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

class FavoriteCitiesFragment : Fragment(R.layout.favorite_cities_fragment) {

    private val binding by viewBinding(FavoriteCitiesFragmentBinding::bind)
    private val viewModel by viewModels<FavoriteCitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val adapter = CityAdapter(onClick = {
            val action = FavoriteCitiesFragmentDirections.actionGlobalCityWeatherFragment(it.city)
            findNavController().navigate(action)
        }, removeFromFavorite = {
            viewModel.removeFromFavorite(it.city)
        })
        binding.favoriteCitiesList.adapter = adapter
        binding.addCity.setOnClickListener {
            findNavController().navigate(R.id.citySearchFragment)
        }
        viewModel.favoriteCities.observe(viewLifecycleOwner) { favoriteCities ->
            binding.emptyList.showOrHide(favoriteCities.isEmpty())
            adapter.submitList(favoriteCities)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}