package io.github.moh_mohsin.ahoyweatherapp.ui.city

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.impl.CityRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class CityModule {

    @Binds
    abstract fun bindCityRepository(
        cityRepositoryImpl: CityRepositoryImpl
    ): CityRepository


}