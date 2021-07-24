package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.FavoriteCityDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_cities")
    suspend fun getAll(): List<FavoriteCityDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg favoriteCityDto: FavoriteCityDto): List<Long>
    @Delete
    suspend fun delete(favoriteCityDto: FavoriteCityDto)

    @Query("Delete FROM favorite_cities")
    suspend fun deleteAll()
}