package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityDto(
    @PrimaryKey
    val cityId: Int
)