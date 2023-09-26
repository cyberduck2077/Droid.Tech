package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.CreatingAlbum
import ru.data.common.models.network.NetworkModelAlbum
import ru.data.common.models.network.NetworkModelMedia
import ru.data.common.models.network.IsFavoriteBody
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest

class ApiAlbums(
    private val client: Client
) {

    /**
     * GET /test/albums/ Получить Все Альбомы
     *
     * @since
     * name string (query)
     *
     * page integer (query)
     *
     * Droid_ids  array [integer] (query)
     *
     * is_personal boolean (query)
     *
     * is_private boolean (query)
     *
     * is_favorite boolean (query)
     *
     * @return
     * response [List] <[NetworkModelAlbum]>
     *
     */
    suspend fun getAlbums(
        name: String? = null,
        page: Int? = null,
        DroidIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        isFavorite: Boolean? = null,
    ): BaseApi<List<NetworkModelAlbum>> {
        return try {
            val queryParams = Parameters.build {
                name?.let { append("name", name) }
                page?.let { append("page", page.toString()) }
                DroidIds?.forEach { listIds -> append("Droid_ids", listIds.toString()) }
                isPersonal?.let { append("is_personal", isPersonal.toString()) }
                isPrivate?.let { append("is_private", isPrivate.toString()) }
                isFavorite?.let { append("is_favorite", isFavorite.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/albums/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )

           response.body<BaseApi<List<NetworkModelAlbum>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * POST /test/albums/  Создать Альбом
     *
     * @since
     * [CreatingAlbum] {
     * "name": "string",
     * "description": "string",
     * "location": "string",
     * "custom_date": 0,
     * "is_private": false
     * }
     * @return
     * response [NetworkModelAlbum]
     */
    suspend fun postAlbums(
        bodyAlbum: CreatingAlbum,
    ): BaseApi<NetworkModelAlbum> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/albums/",
                body = bodyAlbum
            )
            response.body<BaseApi<NetworkModelAlbum>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  GET  /test/albums/{album_id}/ Получить Альбом По Идентификатору
     *
     * @since
     * album_id  integer (path)
     *
     * @return
     * response [NetworkModelAlbum]
     */
    suspend fun getAlbumId(
        albumId: Int
    ): BaseApi<NetworkModelAlbum> {
        return try {
            val response = client.api.get(urlString = "/test/albums/${albumId}/")
            response.body<BaseApi<NetworkModelAlbum>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  PUT  /test/albums/{album_id}/ Изменить Альбом
     *
     * @since
     * album_id  integer (path)
     * [CreatingAlbum] {
     * "name": "string",
     * "description": "string",
     * "location": "string",
     * "custom_date": 0,
     * "is_private": false
     * }
     * @return
     * response [NetworkModelAlbum]
     *
     */
    suspend fun putAlbums(
        albumId: Int,
        updatingAlbum: CreatingAlbum,
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/albums/${albumId}/",
                body = updatingAlbum
            )
            response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * DELETE /test/albums/{album_id}/ Удалить Альбом
     *
     * @since
     * album_id  integer (path)
     *
     * @return
     * response [null]
     */
    suspend fun deleteAlbum(
        albumId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/albums/${albumId}/"
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  PUT /test/albums/{album_id}/is-favorite/ Изменить Альбом
     *
     * @since
     * album_id  integer (path)
     *
     * [IsFavoriteBody] {
     * "is_favorite": true
     * }
     *
     * @return
     * response [null]
     *
     */
    suspend fun putAlbumInFavorite(
        albumId: Int,
        favorite: IsFavoriteBody,
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/albums/${albumId}/is-favorite/",
                body = favorite
            )
            response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     * GET /test/albums/{album_id}/download/ Получить Архив Альбома
     *
     * @since
     * album_id [integer] (query)
     *
     * @return
     * response [Any]
     *
     */
    suspend fun downloadAlbum(
        albumId: Int,
    ): Any {
        return try {
            val response = client.api.get(
                urlString = "/test/albums/$albumId/download/",
            )
            response.body<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}