package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.NetworkModelInterest
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client

class ApiInterests(
    private val client: Client,
) {

    /**Получить Все Интересы [List [NetworkModelInterest]] */
    suspend fun getInterests(
        page: Int? = null,
        name: String? = null,
    ): BaseApi<List<NetworkModelInterest>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/interests/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
          response.body<BaseApi<List<NetworkModelInterest>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}