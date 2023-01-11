package io.github.moh_mohsin.ahoyweatherapp.ui.city.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.domain.GetFavoriteCitiesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.RemoveCityFromFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.ui.city.search.CityWithFavorite
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCitiesViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val removeCityFromFavoritesUseCase: RemoveCityFromFavoritesUseCase,
) : ViewModel(){


    val favoriteCities: LiveData<List<CityWithFavorite>> =
        getFavoriteCitiesUseCase().map { it.map { city -> CityWithFavorite(city, true) } }
            .asLiveData()


    fun removeFromFavorite(city: City) {
        viewModelScope.launch {
            removeCityFromFavoritesUseCase(city)
        }
    }
}