package io.github.moh_mohsin.ahoyweatherapp.ui.city.search

//import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            is State.Data -> LazyColumn {
                                items(state.data) { cityWithFavorite ->
                                    CityItem(
                                        name = cityWithFavorite.city.name,
                                        favorite = cityWithFavorite.favorite
                                    ) {
                                        if (cityWithFavorite.favorite)
                                            viewModel.removeFromFavorite(cityWithFavorite.city)
                                        else
                                            viewModel.addToFavorite(cityWithFavorite.city)
                                    }
                                }
                            }
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


    @Preview(showBackground = true)
    @Composable
    fun CityItemPreview() {
        CityItem(name = "Abu Dhabi (United Arab Emirates)", favorite = true) {

        }
    }

    @Composable
    fun CityItem(name: String, favorite: Boolean, onClick: () -> Unit) {
        Column(modifier = Modifier.padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 4.dp )) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = name,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple(
                                bounded = false,
                            ),
                            onClick = onClick,
                        )
                        .padding(horizontal = 8.dp),
                    painter = painterResource(
                        id = if (favorite) R.drawable.ic_baseline_favorite
                        else R.drawable.ic_baseline_favorite_border
                    ),
                    contentDescription = stringResource(
                        id = R.string.content_desc_favorite_a_city
                    ),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
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