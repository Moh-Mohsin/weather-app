package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.FavoriteCityDto

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