package io.github.moh_mohsin.ahoyweatherapp.di

import androidx.room.Room
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.CityRepositoryImpl
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.WeatherRepositoryImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherLocalDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherRemoteDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.WeatherLocalDataSourceImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.WeatherRemoteDataSourceImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherApi
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherService
import io.github.moh_mohsin.ahoyweatherapp.domain.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import kotlin.coroutines.CoroutineContext


val appDependencies = Kodein.Module("app") {

    bind<CoroutineContext>() with provider { Dispatchers.Default }

    bind<OpenWeatherApi>() with singleton { OpenWeatherService().getService() }
    bind<WeatherLocalDataSource>() with singleton { WeatherLocalDataSourceImpl(instance()) }
    bind<WeatherRemoteDataSource>() with singleton { WeatherRemoteDataSourceImpl(instance())}
    bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance()) }

    bind<GetWeatherUseCase>() with provider { GetWeatherUseCase(instance()) }
}
val cityFeatureDependencies = Kodein.Module("city"){
    bind<CityRepository>() with singleton { CityRepositoryImpl(instance()) }

    bind<AddCityToFavoritesUseCase>() with provider { AddCityToFavoritesUseCase(instance()) }
    bind<RemoveCityFromFavoritesUseCase>() with provider { RemoveCityFromFavoritesUseCase(instance()) }
    bind<GetFavoriteCitiesUseCase>() with provider { GetFavoriteCitiesUseCase(instance()) }
    bind<SearchCitiesUseCase>() with provider { SearchCitiesUseCase(instance()) }
}