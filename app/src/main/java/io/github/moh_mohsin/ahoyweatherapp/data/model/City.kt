package io.github.moh_mohsin.ahoyweatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class City(
    val id: Int,
    val name: String,
    val nameAscii: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val iso2: String,
    val iso3: String,
    val adminName: String,
    val capital: String?,
)