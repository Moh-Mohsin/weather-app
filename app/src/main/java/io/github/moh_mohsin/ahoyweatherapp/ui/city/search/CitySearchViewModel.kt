package io.github.moh_mohsin.ahoyweatherapp.ui.city.search

import android.app.Application
import androidx.lifecycle.*
import io.github.moh_mohsin.ahoyweatherapp.data.Message
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.domain.AddCityToFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.GetFavoriteCitiesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.RemoveCityFromFavoritesUseCase
import io.github.moh_mohsin.ahoyweatherapp.domain.SearchCitiesUseCase
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

class CitySearchViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein(app)

    private val searchCitiesUseCase by instance<SearchCitiesUseCase>()
    private val getFavoriteCitiesUseCase by instance<GetFavoriteCitiesUseCase>()
    private val addCityToFavoritesUseCase by instance<AddCityToFavoritesUseCase>()
    private val removeCityFromFavoritesUseCase by instance<RemoveCityFromFavoritesUseCase>()

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

sealed class State<out T> {
    data class Data<T>(val data: T) : State<T>()
    data class Error(val message: Message) : State<Nothing>()
    object Loading : State<Nothing>()
    object Idle : State<Nothing>()
}

fun <T> State<List<T>>.filter(predicate: (T) -> Boolean): State<List<T>> {
    return when (this) {
        is State.Data -> State.Data(data.filter(predicate))
        else -> this
    }
}

fun <T, R> State<List<T>>.map(transform: (T) -> R): State<List<R>> {
    return when (this) {
        is State.Data -> State.Data(data.map(transform))
        is State.Error -> State.Error(message)
        State.Idle -> State.Idle
        State.Loading -> State.Loading
    }
}

data class CityWithFavorite(
    val city: City,
    val favorite: Boolean
)