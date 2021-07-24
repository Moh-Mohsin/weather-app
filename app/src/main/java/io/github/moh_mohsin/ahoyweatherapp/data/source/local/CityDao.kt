package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    suspend fun getAll(): List<CityDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cityDto: CityDto): List<Long>


    @Query("SELECT * FROM cities WHERE city LIKE '%'||:query||'%' OR country LIKE '%'||:query||'%'")
    suspend fun findByNameOrCountry(query: String): List<CityDto>


    @Query("SELECT * FROM cities WHERE id in (SELECT cityId from favorite_cities)") //TODO: turn into a join?
    fun getFavorite(): Flow<List<CityDto>>

    @Delete
    suspend fun delete(cityDto: CityDto)

    @Query("Delete FROM cities")
    suspend fun deleteAll()
}