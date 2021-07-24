package io.github.moh_mohsin.ahoyweatherapp.ui.city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.domain.GetFavoriteCitiesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.RemoveCityFromFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.ui.citysearch.CityWithFavorite
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FavoriteCitiesViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val getFavoriteCitiesUseCase by instance<GetFavoriteCitiesUseCase>()
    private val removeCityFromFavoritesUseCase by instance<RemoveCityFromFavoritesUseCase>()

    val favoriteCities: LiveData<List<CityWithFavorite>> =
        getFavoriteCitiesUseCase().map { it.map { city -> CityWithFavorite(city, true) } }
            .asLiveData()


    fun removeFromFavorite(city: City) {
        viewModelScope.launch {
            removeCityFromFavoritesUseCase(city)
        }
    }
}