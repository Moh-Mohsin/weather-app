package io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.FavoriteCitiesFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.DarkColors
import io.github.moh_mohsin.ahoyweatherapp.ui.LightColors
import io.github.moh_mohsin.ahoyweatherapp.util.Center
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

@AndroidEntryPoint
class FavoriteCitiesFragment : Fragment(R.layout.favorite_cities_fragment) {

    private val binding by viewBinding(FavoriteCitiesFragmentBinding::bind)
    private val viewModel by viewModels<FavoriteCitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addCity.setOnClickListener {
            findNavController().navigate(R.id.citySearchFragment)
        }

        viewModel.favoriteCities.observe(viewLifecycleOwner) { citiesWithFavorite ->
            binding.composeView.setContent {
                MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColors else LightColors) {
                    Surface {
                        if (citiesWithFavorite.isNotEmpty())
                            CityList(
                                citiesWithFavorite,
                                onRemoveFavorite = {
                                    viewModel.removeFromFavorite(it.city)
                                },
                                onClick = {
                                    val action = FavoriteCitiesFragmentDirections.actionGlobalCityWeatherFragment(it.city)
                                    findNavController().navigate(action)
                                },
                            )
                        else
                            Center {
                                Text(text = stringResource(id = R.string.no_favorites_added))
                            }
                    }
                }
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
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