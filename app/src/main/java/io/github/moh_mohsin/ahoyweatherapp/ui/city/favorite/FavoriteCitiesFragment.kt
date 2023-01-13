package io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.FavoriteCitiesFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.city.adapter.CityAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.showOrHide
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

@AndroidEntryPoint
class FavoriteCitiesFragment : Fragment(R.layout.favorite_cities_fragment) {

    private val binding by viewBinding(FavoriteCitiesFragmentBinding::bind)
    private val viewModel by viewModels<FavoriteCitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        (requireActivity() as MenuHost).addMenuProvider( object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_options, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.settingsFragment)
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}