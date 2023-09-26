package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import org.apache.tika.Tika
import ru.data.common.models.network.NetworkModelMedia
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingMedia
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest
import java.io.File

class ApiMedia(
    private val client: Client,
) {

    /**
     * GET [Client.BASE_URL] /test/users/me/medias/
     *
     * Получить Все Медиа
     *
     * @since
     *  name  [String]  (query)
     *
     *  extension [String] (query)
     *
     *  is_favorite [Boolean] (query)
     *
     *  page [Int] (query)
     *
     * @return
     * response [List]<[NetworkModelMedia]>
     *
     */
    suspend fun getMedias(
        name: String? = null,
        extension: String? = null,
        albumIds: List<Int>? = null,
        isFavorite: Boolean? = null,
        withoutAlbum: Boolean? = null,
        DroidIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        page: Int? = null,
    ): BaseApi<List<NetworkModelMedia>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                extension?.let { append("extension", extension) }
                albumIds?.let { listIds -> appendAll("album_ids", listIds.map { it.toString() }) }
                isFavorite?.let { append("is_favorite", isFavorite.toString()) }
                withoutAlbum?.let { append("without_album", withoutAlbum.toString()) }
                DroidIds?.let {listIds ->  appendAll("Droid_ids", listIds.map { it.toString() }) }
                isPersonal?.let { append("is_personal", isPersonal.toString()) }
                isPrivate?.let { append("is_private", isPrivate.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/users/me/medias/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelMedia>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     * GET [Client.BASE_URL]  /test/users/me/medias/{media_id}/
     *
     * Получить Медиа По Id
     *
     * @since
     *  media_id  [Int]  (query)
     *
     * @return
     * response [NetworkModelMedia]
     *
     */
    suspend fun getMediaById(
        media_id:Int
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.get(
                urlString = "/test/users/me/medias/${media_id}/",
            )
            response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * POST  [Client.BASE_URL] /test/users/me/medias/
     *
     * Добавить медиа
     *
     * Request body  new_file string($binary) multipart/form-data
     *
     * response [Nothing]
     *
     */
    suspend fun postMyMedia(
        file: File,
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/medias/",
                body = MultiPartFormDataContent(formData {
                    append("new_file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, Tika().detect(file))
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                })
            )
            response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  PUT [Client.BASE_URL] /test/users/me/medias/{media_id}/
     *
     *  Изменить Медиа
     *
     *  media_id integer (path)
     *
     *  Request body [UpdatingMedia] {
     *      "name": "string",
     *      "is_favorite": true,
     *      "album_id": 0
     * }
     *
     * response [Nothing]
     *
     */
    suspend fun putMyMedia(
        updatingMedia: UpdatingMedia,
        mediaId: Int,
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/medias/${mediaId}/",
                body = updatingMedia
            )
            response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  DELETE [Client.BASE_URL] /test/users/me/medias/{media_id}/
     *
     *  Удалить Медиа
     *
     *  media_id integer (path)
     *
     *  response [null]
     */

    suspend fun deleteMedia(
        mediaId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/users/me/medias/${mediaId}/"
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}