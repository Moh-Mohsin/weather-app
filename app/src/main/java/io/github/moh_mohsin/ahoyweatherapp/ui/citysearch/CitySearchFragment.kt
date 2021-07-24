package io.github.moh_mohsin.ahoyweatherapp.ui.citysearch

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.CitySearchFragmentBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.city.adapter.CityAdapter
import io.github.moh_mohsin.ahoyweatherapp.util.hide
import io.github.moh_mohsin.ahoyweatherapp.util.show
import io.github.moh_mohsin.ahoyweatherapp.util.toast
import io.github.moh_mohsin.ahoyweatherapp.util.viewBinding
import kotlinx.android.synthetic.main.single_city.*
import timber.log.Timber

class CitySearchFragment : Fragment(R.layout.city_search_fragment), SearchView.OnQueryTextListener {

    private val binding by viewBinding(CitySearchFragmentBinding::bind)
    private val viewModel by viewModels<CitySearchViewModel>()

    private var savedSearch: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val adapter = CityAdapter(onClick = {
            toast("city clicked: ${it.city.name}")
        }, addToFavorite = {
            viewModel.addToFavorite(it.city)
        }, removeFromFavorite = {
            viewModel.removeFromFavorite(it.city)
        })
        binding.citySearchResultList.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            Timber.d("state: $state")
            when (state) {
                is State.Data -> {
                    binding.citySearchResultList.show()
                    binding.loading.hide()

                    if (state.data.isEmpty()) {
                        binding.message.show()
                        binding.message.setText(R.string.no_result_matching)
                    } else {
                        binding.message.hide()
                    }

                    adapter.submitList(state.data)
                }
                is State.Error -> {

                }
                State.Loading -> {
                    binding.loading.show()
                    binding.citySearchResultList.hide()
                    binding.message.hide()
                }
                State.Idle -> {
                    binding.citySearchResultList.hide()
                    binding.loading.hide()
                    binding.message.show()
                    binding.message.setText(R.string.search_result_here)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        savedSearch?.let { query ->
            searchItem.expandActionView()
            searchView.setQuery(query, true)
            searchView.clearFocus()
        }
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.setSearchQuery(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
//        viewModel.setSearchQuery(newText)
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