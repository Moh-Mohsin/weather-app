package io.github.moh_mohsin.ahoyweatherapp.di

import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.WeatherRepositoryImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.WeatherRemoteDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherApi
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherService
import io.github.moh_mohsin.ahoyweatherapp.domain.GetWeatherUseCase
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
    bind<WeatherDataSource>("remote") with singleton { WeatherRemoteDataSource(instance())}
    bind<WeatherDataSource>("local") with singleton { WeatherRemoteDataSource(instance())} //TODO: replace with Room impl
    bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance("local"), instance("remote")) }
    bind<GetWeatherUseCase>() with provider { GetWeatherUseCase(instance()) }
}