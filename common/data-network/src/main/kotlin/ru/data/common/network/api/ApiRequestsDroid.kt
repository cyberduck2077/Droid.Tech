package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.BodyAny
import ru.data.common.models.network.NetworkModelDroidRequest
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingDroidRequest
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.getRequest
import ru.data.common.network.util.postRequest

/**
 * Клиентское приложение / Запросы на членство
 */
class ApiRequestsDroid(
    private val client: Client,
) {

    /**
     * Получить Все Запросы На Членство
     *
     * @param [Uri] GET /test/collectives/{Droid_id}/requests/
     * @param [Int] Droid_id (path)
     * @param [Int] user_id (path)
     * @param [Int] page (query)
     *
     * @return [List [NetworkModelDroidRequest]]
     */
    suspend fun getCollectivesRequests(
        DroidId: Int,
        userId: Int?,
        page: Int = 1,
    ): BaseApi<List<NetworkModelDroidRequest>> {
        return try {
            val queryParams = Parameters.build {
                userId?.let { append("first_name", "$userId") }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/test/collectives/${DroidId}/requests/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelDroidRequest>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Отправить Запрос На Членство
     *
     * @param [Uri] POST /test/collectives/{Droid_id}/requests/
     * @param [Int] Droid_id (path)
     *
     * @param [BodyAny] (body) пустое тело
     *
     * @return [NetworkModelDroidRequest]
     */
    suspend fun postCollectivesRequests(
        DroidId: Int,
    ): BaseApi<NetworkModelDroidRequest> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/collectives/${DroidId}/requests/",
                body = BodyAny()
            )
            response.body<BaseApi<NetworkModelDroidRequest>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Получить Запрос На Членство По Идентификатору
     *
     * @param [Uri] GET /test/collectives/requests/{request_id}/
     * @param [Int] request_id (path)
     *
     * @param [BodyAny] (body) пустое тело
     *
     * @return [NetworkModelDroidRequest]
     */
    suspend fun getCollectivesRequest(
        requestId: Int,
    ): BaseApi<NetworkModelDroidRequest> {
        return try {
            val response = client.api.get(urlString = "/test/collectives/requests/${requestId}/")
            response.body<BaseApi<NetworkModelDroidRequest>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Изменить Запрос На Членство В Коллективе
     *
     * @param [Uri] PUT /test/collectives/requests/{request_id}/is-approved/
     * @param [Int] request_id (path)
     *
     * @param [UpdatingDroidRequest] (body)
     *
     * @return [NetworkModelDroidRequest]
     */
    suspend fun putCollectivesRequestApproved(
        requestId: Int,
        isApproved: Boolean,
        DroidRole: Int?,
    ): BaseApi<NetworkModelDroidRequest> {
        return try {
            val response = client.api.getRequest(
                urlString = "/test/collectives/requests/${requestId}/is-approved/",
                body = UpdatingDroidRequest(
                    is_approved = isApproved,
                    Droid_role = DroidRole
                )
            )
            response.body<BaseApi<NetworkModelDroidRequest>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Удалить Запрос На Членство В Коллективе
     *
     * @param [Uri] DELETE /test/collectives/requests/{request_id}/
     * @param [Int] request_id (path)
     *
     * @return [Nothing]
     *
     */
    suspend fun deleteCollectivesRequest(
        requestId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(urlString = "/test/collectives/requests/${requestId}/")
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

}