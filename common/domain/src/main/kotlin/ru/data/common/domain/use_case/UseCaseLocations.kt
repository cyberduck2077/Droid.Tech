package ru.data.common.domain.use_case

import ru.data.common.db.CityDaoRoom
import ru.data.common.models.data_base.City
import ru.data.common.models.data_base.ListCity
import ru.data.common.models.network.NetworkModelLocation
import ru.data.common.models.network.BaseApi
import ru.data.common.models.util.jsonToObject
import ru.data.common.network.api.ApiLocation

class UseCaseLocations(
    private val apiLocation: ApiLocation,
    private val daoLocation: CityDaoRoom,
) {

    suspend fun initDdCities(locations: () -> String) {
        if (daoLocation.isEmptyCityDao()) {
            val cities: List<City> = locations().jsonToObject<ListCity>()?.data ?: listOf()
            daoLocation.setCitiesData(cities)
        }
    }

    suspend fun getDdCities(
        name: String? = null,
    ): List<City> = daoLocation.getCitiesLimit(name)

    suspend fun getDdCity(
        id: Int,
    ): City? = daoLocation.getCity(id)

    suspend fun getLocationsApi(
        page: Int = 1,
        name: String? = null,
    ): BaseApi<List<NetworkModelLocation>> = apiLocation.getLocations(
        name = name,
        page = page
    )
}