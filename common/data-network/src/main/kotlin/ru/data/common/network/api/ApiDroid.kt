package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.NetworkModelDroid
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingDroid
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest

/**Клиентское приложение / Droid*/
class ApiDroid(
    private val client: Client,
) {

    /**
     *  GET
     *  /test/users/me/collectives/
     *
     *  Получить Все Семьи
     *
     * name string (query)
     *
     * page integer (query)
     *
     * List [NetworkModelDroid]
     */
    suspend fun getCollectives(
        page: Int = 1,
        name: String? = null,
    ): BaseApi<List<NetworkModelDroid>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/test/users/me/collectives/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelDroid>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  GET
     *  /test/collectives/{Droid_id}/
     *
     *  Получить Семья По Идентификатору
     *
     *  Droid_id integer (path)
     *
     *  [NetworkModelDroid]
     */

    suspend fun getDroidId(
        DroidId: Int
    ): BaseApi<NetworkModelDroid> {
        return try {
            val response = client.api.get(urlString = "/test/collectives/${DroidId}/")
            response.body<BaseApi<NetworkModelDroid>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  PUT
     *  /test/collectives/{Droid_id}/
     *
     *  Изменить Семья
     *
     *  Droid_id integer (path)
     *
     *  [UpdatingDroid] (body)
     *
     *  [NetworkModelDroid] (response)
     */

    suspend fun putDroidNameFromId(
        DroidId: Int,
        newNameDroid: String
    ): BaseApi<NetworkModelDroid> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/collectives/${DroidId}/",
                body = UpdatingDroid(newNameDroid)
            )
            response.body<BaseApi<NetworkModelDroid>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  DELETE
     *  /test/collectives/{Droid_id}/
     *
     *  Удалить Семью
     *
     *  Droid_id integer (path)
     *
     *  [null] (response)
     */

    suspend fun deleteDroidFromId(
        DroidId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/collectives/${DroidId}/"
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  POST
     *  /test/collectives/
     *
     *  Создать Семью
     *
     *  [UpdatingDroid] (body)
     *
     *  [NetworkModelDroid] (response)
     */
    suspend fun postCreateDroid(
        nameDroid: String?
    ): BaseApi<NetworkModelDroid> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/collectives/",
                body = UpdatingDroid(nameDroid)
            )
            response.body<BaseApi<NetworkModelDroid>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

}