package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.FavoriteCityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto

@Database(entities = [WeatherInfoDto::class, CityDto::class, FavoriteCityDto::class], exportSchema = false, version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherInfoDao(): WeatherInfoDao
    abstract fun cityDao(): CityDao
    abstract fun favoriteDao(): FavoriteDao
}