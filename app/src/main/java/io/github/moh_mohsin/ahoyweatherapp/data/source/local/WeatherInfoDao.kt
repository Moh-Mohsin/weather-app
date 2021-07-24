package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherInfoDao {
    @Query("SELECT * FROM weatherinfodto")
    fun getAll(): Flow<List<WeatherInfoDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg weatherInfoDtoList: WeatherInfoDto)


    @Query("SELECT * FROM weatherinfodto WHERE lat = :lat AND lon = :lon LIMIT 1")
    fun findByLatLon(lat: Double, lon: Double): Flow<WeatherInfoDto?>

    @Delete
    suspend fun delete(weatherInfoDto: WeatherInfoDto)

    @Query("Delete FROM weatherinfodto")
    suspend fun deleteAll()
}