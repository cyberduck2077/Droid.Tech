package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.local.maps.RequestType
import ru.data.common.models.network.DescriptionBody
import ru.data.common.models.network.ExistsResponse
import ru.data.common.models.network.NetworkModelUser
import ru.data.common.models.network.IsAcceptedBody
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.getRequest
import ru.data.common.network.util.postRequest

/**Клиентское Приложение / Пользователи*/
class ApiUsers(
    private val client: Client,
) {

    /**
     * Проверить На Существование Пользователя
     *
     * @param [Uri] GET /test/users/exists/
     *
     * @param [String] email (query)
     * @param [String] tel (query)
     *
     * @return [ExistsResponse]
     */
    suspend fun getUserExists(
        email: String? = null,
        tel: String? = null,
    ): BaseApi<ExistsResponse> {
        return try {
            val queryParams = Parameters.build {
                email?.let { append("email", email) }
                tel?.let { append("tel", tel) }
            }
            val response = client.api.get(
                urlString = "/test/users/exists/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<ExistsResponse>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     * Получить Всех Пользователей
     *
     * @param [Uri] GET /test/users/
     *
     * @param [String] full_name (query)
     * @param [Int] gender (query)
     * @param [Int] age_from (query)
     * @param [Int] age_to (query)
     * @param [Boolean] in_current_collectives (query)
     * @param [List [Int]] Droid_ids (query)
     * @param [List [Int]] location_ids (query)
     * @param [List [Int]] birth_location_ids (query)
     * @param [Int] page (query)
     *
     * @return [List [NetworkModelUser]
     */
    suspend fun getUsers(
        name: String?,
        page: Int?,
        fullName: String?,
        gender: Int?,
        ageFrom: Long?,
        ageTo: Long?,
        inCurrentCollectives: Boolean?,
        DroidIds: List<Int>?,
        locationIds: List<Int>?,
        birthLocationIds: List<Int>?,
    ): BaseApi<List<NetworkModelUser>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
                fullName?.let { append("full_name", fullName.toString()) }
                gender?.let { append("gender", gender.toString()) }
                ageFrom?.let { append("age_from", ageFrom.toString()) }
                ageTo?.let { append("age_to", ageTo.toString()) }

                inCurrentCollectives?.let {
                    append(
                        "in_current_collectives",
                        inCurrentCollectives.toString()
                    )
                }

                DroidIds?.forEach { listIds ->
                    append(
                        "Droid_ids",
                        listIds.toString()) }

                locationIds?.forEach { listIds ->
                    append(
                        "location_ids",
                        listIds.toString()) }

                birthLocationIds?.forEach { listIds ->
                    append(
                        "birth_location_ids",
                        listIds.toString()
                    )
                }
            }
            val response = client.api.get(
                urlString = "/test/users/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelUser>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Получить Пользователя По Идентификатору
     *
     * @param [Uri] GET /test/users/{user_id}/
     * @param [Int] user_id (path)
     *
     * @return [NetworkModelUser] [NetworkModelUserWithFriendship]
     */
    suspend fun getUser(
        userId: Int,
    ): BaseApi<NetworkModelUser> {
        return try {
            val response = client.api.get(
                urlString = "/test/users/${userId}/",
            )
            response.body<BaseApi<NetworkModelUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Отправить Запрос На Дружбу
     *
     * @param [Uri] POST /test/users/{user_id}/friendship/
     * @param [Int] user_id (path)
     *
     * @param [DescriptionBody] (body)
     *
     * @return [Nothing]
     */
    suspend fun postFriendship(
        userId: Int,
        text: String,
    ): BaseApi<Any> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/${userId}/friendship/",
                body = DescriptionBody(description = text)
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Обработать Запрос На Дружбу
     *
     * @param [Uri] PUT /test/users/{user_id}/friendship/is-accepted/
     * @param [Int] user_id (path)
     *
     * @param [IsAcceptedBody] (body)
     *
     * @return [Nothing]
     */
    suspend fun putFriendshipAccepted(
        userId: Int,
        isAccepted: Boolean
    ): BaseApi<Any> {
        return try {
            val response = client.api.getRequest(
                urlString = "/test/users/${userId}/friendship/is-accepted/",
                body = IsAcceptedBody(is_accepted = isAccepted)
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * Получить Своих Друзей
     *
     * @param [Uri] GET /test/users/me/friends/
     *
     * @param [Boolean] with_accepted (query)
     * @param [Boolean] with_declined (query)
     * @param [Boolean] with_waiting (query)
     * @param [String] request_type (query) :  null , in ,  out
     * @param [Int] page (query)
     *
     * @return [NetworkModelUser]
     */
    suspend fun getFriends(
        withAccepted: Boolean? = null,
        withDeclined: Boolean? = null,
        withWaiting: Boolean? = null,
        requestType: RequestType? = null,
        page: Int? = null,
    ): BaseApi<List<NetworkModelUser>> {
        return try {
            val queryParams = Parameters.build {
                withAccepted?.let { append("with_accepted", withAccepted.toString()) }
                withDeclined?.let { append("with_declined", withDeclined.toString()) }
                withWaiting?.let { append("with_waiting", withWaiting.toString()) }
                requestType?.let { append("request_type", requestType.nameType) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/users/me/friends/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelUser>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


}
