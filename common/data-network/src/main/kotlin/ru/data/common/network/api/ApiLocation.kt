package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.NetworkModelLocation
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client

class ApiLocation(
    private val client: Client,
) {

    /**Получить Все Местоположения [List [NetworkModelLocation]] */
    suspend fun getLocations(
        page: Int? = null,
        name: String? = null,
    ): BaseApi<List<NetworkModelLocation>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/locations/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
          response.body<BaseApi<List<NetworkModelLocation>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}