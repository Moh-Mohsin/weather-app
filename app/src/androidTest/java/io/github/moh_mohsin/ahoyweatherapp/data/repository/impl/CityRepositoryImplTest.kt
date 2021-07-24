package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CityRepositoryImplTest {

//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)

    private val db = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java
    ).build()
    private val cityRepositoryImpl = CityRepositoryImpl(db)

    @Test
    fun should_return_inserted_list_with_matching_query() = runBlocking {
        assertEquals(listOf(), cityRepositoryImpl.searchCities("city"))
        // city0, city1, city2
        val cities = TestUtil.dummyCities().mapIndexed { index, c -> c.copy(name = "city$index") }
        cityRepositoryImpl.insertAll(cities)
        assertEquals(
            cities.sortedBy(City::id), // must sort so equality of lists is not affected by order
            cityRepositoryImpl.searchCities("city").sortedBy(City::id)
        )
    }

    @Test
    fun should_return_city_after_adding_to_favorite() = runBlocking {
        val cities = TestUtil.dummyCities()
        cityRepositoryImpl.insertAll(cities)
        cityRepositoryImpl.addToFavorites(cities.first())
        val favoritesCities = cityRepositoryImpl.getFavorites().first()
        assertTrue { favoritesCities.contains(cities.first()) }
    }

    @Test
    fun should_not_return_favorite_city_after_removing() = runBlocking {
        val cities = TestUtil.dummyCities()
        cityRepositoryImpl.insertAll(cities)
        cityRepositoryImpl.addToFavorites(cities.first())
        val favoritesCities = cityRepositoryImpl.getFavorites().first()
        assertTrue { favoritesCities.contains(cities.first()) }
        cityRepositoryImpl.removeFromFavorites(cities.first())
        val favoritesCities1 = cityRepositoryImpl.getFavorites().first()
        assertTrue { favoritesCities1.contains(cities.first()).not() }
    }


    @After
    fun tearDown() {
        db.close()
    }

}