package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cities")
data class CityDto(
    @PrimaryKey val id: Int,
    @ColumnInfo(index = true) val city: String,
    @ColumnInfo(name = "city_ascii")
    @SerializedName("city_ascii")
    val cityAscii: String,
    var lat: Double,
    var lng: Double,
    @ColumnInfo(index = true) val country: String,
    var iso2: String,
    var iso3: String,
    @ColumnInfo(name = "admin_name")
    @SerializedName("admin_name")
    val adminName: String,
    var capital: String?,
)