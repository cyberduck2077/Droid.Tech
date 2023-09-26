package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.CreatingDroidMember
import ru.data.common.models.network.NetworkModelDroidMember
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest


/**
 *  Клиентское приложение / Члены семьи
 */
class ApiDroidMembers(
    private val client: Client,
) {

    /**
     *  GET
     *  /test/collectives/{Droid_id}/members/
     *
     *  Получить Всех Членов Семьи
     *
     *  Droid_id integer (path)
     *
     *  first_name string (query)
     *
     *  last_name string (query)
     *
     *  patronymic string (query)
     *
     *  role integer (query)
     *
     *  List [NetworkModelDroidMember]
     */

    suspend fun getDroidMembers(
        DroidId: Int,
        firstName: String?,
        lastName: String?,
        patronymic: String?,
        role: Int?,
        page: Int = 1,
    ): BaseApi<List<NetworkModelDroidMember>> {
        return try {
            val queryParams = Parameters.build {
                firstName?.let { append("first_name", firstName) }
                lastName?.let { append("last_name", lastName) }
                patronymic?.let { append("patronymic", patronymic) }
                role?.let { append("role", role.toString()) }
                append("page", page.toString())
            }
            val response = client.api.get(
                urlString = "/test/collectives/${DroidId}/members/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelDroidMember>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  POST
     *  /test/collectives/{Droid_id}/members/
     *
     *  Создать Члена Семьи
     *
     *  Droid_id integer (path)
     *
     *  [CreatingDroidMember] (body)
     *
     *  [NetworkModelDroidMember] (response)
     *
     */

    suspend fun postCreateDroidMember(
        DroidId: Int,
        DroidMember: CreatingDroidMember,
    ): BaseApi<NetworkModelDroidMember> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/collectives/${DroidId}/members/",
                body = DroidMember
            )
            response.body<BaseApi<NetworkModelDroidMember>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  GET
     *  /test/collectives/members/{member_id}/
     *
     *  Получить Члена Семьи По Идентификатору
     *
     *  member_id integer (path)
     *
     *  [NetworkModelDroidMember] (response)
     */

    suspend fun getDroidMemberFromId(
        memberId: Int
    ): BaseApi<NetworkModelDroidMember> {
        return try {
            val response = client.api.get(urlString = "/test/collectives/members/${memberId}/")
            response.body<BaseApi<NetworkModelDroidMember>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  PUT
     *  /test/collectives/members/{member_id}/
     *
     *  Изменить Члена Семьи
     *
     *  member_id integer (path)
     *
     *  [NetworkModelDroidMember] (response)
     */


    suspend fun putUpdateDroidMember(
        memberId: Int,
        DroidMember: CreatingDroidMember,
    ): BaseApi<NetworkModelDroidMember> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/collectives/members/${memberId}/",
                body = DroidMember
            )
            response.body<BaseApi<NetworkModelDroidMember>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  DELETE
     *  /test/collectives/members/{member_id}/
     *
     *  Удалить Члена Семьи
     *
     *  member_id integer (path)
     *
     *  [Any] (response)
     */

    suspend fun deleteDroidMember(
        memberId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/collectives/members/${memberId}/",
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

}