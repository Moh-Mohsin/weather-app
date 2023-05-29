package io.github.moh_mohsin.ahoyweatherapp.ui.city.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.moh_mohsin.ahoyweatherapp.data.State
import io.github.moh_mohsin.ahoyweatherapp.data.map
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.domain.AddCityToFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.GetFavoriteCitiesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.RemoveCityFromFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.SearchCitiesUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val addCityToFavoritesUseCase: AddCityToFavoritesUseCase,
    private val removeCityFromFavoritesUseCase: RemoveCityFromFavoritesUseCase,
) : ViewModel() {

    var query: String = ""

//    private val _cities: MutableLiveData<List<City>> = MutableLiveData(listOf())
//    val cities: LiveData<List<City>> = _cities

    private val _state: MutableLiveData<State<List<City>>> = MutableLiveData(State.Idle)
    private val _favoriteCities: LiveData<List<City>> = getFavoriteCitiesUseCase().asLiveData()

    private val _combinedState = MediatorLiveData<State<List<CityWithFavorite>>>()
    val state: LiveData<State<List<CityWithFavorite>>> = _combinedState

    init {

        _combinedState.apply {
            fun <T> filterOutFavorite() = { _: T ->
                val favoriteCities = _favoriteCities.value ?: listOf()
                _combinedState.value = _state.value?.map { city ->
                    CityWithFavorite(
                        city,
                        favoriteCities.contains(city)
                    )
                }
            }
            addSource(_favoriteCities, filterOutFavorite())
            addSource(_state, filterOutFavorite())
        }
    }

    private fun searchCities(query: String) {
        if (query.isBlank()) {
            _state.value = State.Idle
            return
        }
        viewModelScope.launch {
            if (_state.value !is State.Data) {
                _state.value = State.Loading
            }
            val result = searchCitiesUseCase(query) //TODO: error handling?
            _state.value = State.Data(result)
        }
    }

    fun setSearchQuery(query: String) {
        this.query = query
        searchCities(query)
        Timber.d("query: $query")
    }

    fun addToFavorite(city: City) {
        viewModelScope.launch {
            addCityToFavoritesUseCase(city)
        }
    }

    fun removeFromFavorite(city: City) {
        viewModelScope.launch {
            removeCityFromFavoritesUseCase(city)
        }
    }
}


data class CityWithFavorite(
    val city: City,
    val favorite: Boolean
)