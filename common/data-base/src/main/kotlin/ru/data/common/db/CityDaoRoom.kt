package ru.data.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.data.common.models.data_base.City
import ru.data.common.models.data_base.CityDao
import ru.data.common.models.data_base.CountryDao
import ru.data.common.models.data_base.RegionDao


@Dao
interface CityDaoRoom {

    @Query("SELECT (SELECT COUNT(*) FROM ${CityDao.TABLE_NAME}) == 0")
    suspend fun isEmptyCityDao(): Boolean


    @Query("SELECT * FROM ${CityDao.TABLE_NAME}")
    suspend fun getAllCitiesDao(): List<CityDao>

    @Query("SELECT * FROM ${CityDao.TABLE_NAME} LIMIT :limit")
    suspend fun getAllCityDaoLimit(limit: Int = 30): List<CityDao>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityDao(vararg data: CityDao)

    @Query("SELECT * FROM ${CityDao.TABLE_NAME} WHERE LOWER(${CityDao.NAME}) LIKE '%' || LOWER(:name) || '%' LIMIT :limit")
    suspend fun getAllCityDaoLimit(name: String, limit: Int = 30): List<CityDao>

    @Query("SELECT * FROM ${CityDao.TABLE_NAME} WHERE ${CityDao.ID} = :id LIMIT 1")
    suspend fun getCityDao(id: Int): CityDao?


    @Query("SELECT * FROM ${RegionDao.TABLE_NAME}")
    suspend fun getAllRegionDao(): List<RegionDao>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegionDao(vararg data: RegionDao)

    @Query("SELECT * FROM ${RegionDao.TABLE_NAME} WHERE ${RegionDao.NAME} = LOWER(:name)")
    suspend fun getAllRegionDao(name: String): List<RegionDao>

    @Query("SELECT * FROM ${RegionDao.TABLE_NAME} WHERE ${RegionDao.NAME} = :id LIMIT 1")
    suspend fun getRegionDao(id: Int): RegionDao?


    @Query("SELECT * FROM ${CountryDao.TABLE_NAME}")
    suspend fun getAllCountryDao(): List<CountryDao>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryDao(vararg data: CountryDao)

    @Query("SELECT * FROM ${CountryDao.TABLE_NAME} WHERE ${CountryDao.NAME} = LOWER(:name)")
    suspend fun getAllCountryDao(name: String): List<CountryDao>

    @Query("SELECT * FROM ${CountryDao.TABLE_NAME} WHERE ${CountryDao.NAME} = :id LIMIT 1")
    suspend fun getCountryDao(id: Int): CountryDao?


    @Transaction
    suspend fun getCitiesLimit(): List<City> {
        return getAllCityDaoLimit().map {
            City.mupToCity(
                it,
                region = getRegionDao(it.regionId),
                country = getCountryDao(it.countryId)
            )
        }
    }

    @Transaction
    suspend fun getCity(id: Int): City? {
        val city = getCityDao(id = id) ?: return null
        return City.mupToCity(
            city = city,
            region = getRegionDao(city.regionId),
            country = getCountryDao(city.countryId)
        )
    }


    @Transaction
    suspend fun getCitiesLimit(name: String? = null): List<City> {
        if (name == null) return getCitiesLimit()
        return getAllCityDaoLimit(name = name).map {
            City.mupToCity(
                it,
                region = getRegionDao(it.regionId),
                country = getCountryDao(it.countryId)
            )
        }
    }


    @Transaction
    suspend fun setCitiesData(list: List<City>) {
        list.forEach {
            insertCityDao(it.mapToCityDao())
            insertCountryDao(it.mapToCountryDao())
            insertCountryDao(it.mapToCountryDao())
        }
    }
}