package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.NetworkModelTopic
import ru.data.common.models.network.BaseApi
import ru.data.common.network.Client

class ApiTopics(
    private val client: Client,
) {

    /**
     * Получить Все Темы
     * @param [Uri] GET /test/topics/
     *
     * @param [Int] page (query)
     * @param [String] name (query)
     *
     * @return [List [NetworkModelTopic]]
     */
    suspend fun getTopics(
        page: Int?,
        name: String?,
    ): BaseApi<List<NetworkModelTopic>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("parent_id", name) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/topics/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelTopic>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }
}