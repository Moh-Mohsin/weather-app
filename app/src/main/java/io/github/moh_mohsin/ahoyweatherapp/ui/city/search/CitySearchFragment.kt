package io.github.moh_mohsin.ahoyweatherapp.ui.city.search

//import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.State
import io.github.moh_mohsin.ahoyweatherapp.data.get
import io.github.moh_mohsin.ahoyweatherapp.databinding.CitySearchFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.DarkColors
import io.github.moh_mohsin.ahoyweatherapp.ui.LightColors
import io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite.CityList
import io.github.moh_mohsin.ahoyweatherapp.util.Center
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding

@AndroidEntryPoint
class CitySearchFragment : Fragment(R.layout.city_search_fragment), SearchView.OnQueryTextListener {

    private val binding by viewBinding(CitySearchFragmentBinding::bind)
    private val viewModel by viewModels<CitySearchViewModel>()

    private var savedSearch: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.city_search_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView
                searchView.queryHint = getString(R.string.search)

                savedSearch?.let { query ->
                    searchItem.expandActionView()
                    searchView.setQuery(query, true)
                    searchView.clearFocus()
                }
                searchView.setOnQueryTextListener(this@CitySearchFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.composeView.setContent {
                MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColors else LightColors) {
                    Surface {
                        when (state) {
                            is State.Data ->
                                CityList(
                                    state.data,
                                    onAddFavorite = {
                                            viewModel.addToFavorite(it.city)
                                    },
                                    onRemoveFavorite = {
                                        viewModel.removeFromFavorite(it.city)
                                    },
                                )
                            is State.Error -> Center {
                                Text(text = state.message.get())
                            }
                            State.Loading -> Center {
                                CircularProgressIndicator()
                            }
                            State.Idle -> Center {
                                Text(text = stringResource(id = R.string.search_result_here))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.setSearchQuery(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.setSearchQuery(newText)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_VIEW_KEY, viewModel.query)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val SEARCH_VIEW_KEY = "SEARCH_VIEW_KEY"
    }
}