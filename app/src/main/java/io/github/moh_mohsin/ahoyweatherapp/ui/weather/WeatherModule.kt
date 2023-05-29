package io.github.moh_mohsin.ahoyweatherapp.ui.weather

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.moh_mohsin.ahoyweatherapp.data.repository.SettingsRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.SettingsRepositoryImpl
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.WeatherRepositoryImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherLocalDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherRemoteDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.WeatherLocalDataSourceImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.WeatherRemoteDataSourceImpl
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherApi
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherService
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {
    @Singleton
    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Singleton
    @Binds
    abstract fun bindWeatherLocalDataSource(
        weatherLocalDataSourceImpl: WeatherLocalDataSourceImpl
    ): WeatherLocalDataSource

    @Singleton
    @Binds
    abstract fun bindWeatherRemoteDataSource(
        weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(
        settingRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

}

@Module
@InstallIn(SingletonComponent::class)
object WeatherProvidersModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        Timber.d("providesAppDatabase");
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather-app",
        ).createFromAsset("database/weather-app.db").build()
    }

    @Singleton
    @Provides
    fun providesOpenWeatherApi(): OpenWeatherApi {
        return OpenWeatherService().getService()
    }

    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences {
        return applicationContext.getSharedPreferences("default", MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providesGson() = Gson()
}