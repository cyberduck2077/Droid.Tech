package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import ru.data.common.models.network.CreatingWish
import ru.data.common.models.network.CreatingWishlist
import ru.data.common.models.network.NetworkModelWish
import ru.data.common.models.network.NetworkModelWishlist
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest
import java.io.File

/**
 * Клиентское приложение / Желания + Вишлисты
 */
class ApiWishesAndList(private val client: Client) {

    /**
     *  Получить Список Желаний
     *
     * @param [Uri] GET /test/users/me/wishes/
     * @param [Int] wishlist_id (query)
     * @param [String] title (query)
     * @param [Int] page (query)
     *
     * @return List [NetworkModelWish]
     */
    suspend fun getWishes(
        wishlistId: Int?,
        title: String?,
        page: Int?,
    ): BaseApi<List<NetworkModelWish>> {
        return try {
            val queryParams = Parameters.build {
                wishlistId?.let { append("wishlist_id", wishlistId.toString()) }
                title?.let { append("title", title.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/users/me/wishes/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )

           response.body<BaseApi<List<NetworkModelWish>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  Добавить Желание
     *
     * @param [Uri] POST /test/users/me/wishes/
     * @param [CreatingWish] {
     *
     *  "title": [String],
     *
     *  "description": [String],
     *
     *  "price": [Int],
     *
     *  "link": [String],
     *
     *   "wishlist_id": [Int]
     *
     * }
     * @return [NetworkModelWish]
     */
    suspend fun postWishlist(
        bodyWish: CreatingWish,
    ): BaseApi<NetworkModelWish> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/wishes/",
                body = bodyWish
            )
            response.body<BaseApi<NetworkModelWish>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Получить Желание
     *
     * @param [Uri] GET /test/users/wishes/{wish_id}/
     * @param [Int] wish_id (path)
     * @return [NetworkModelWish]
     */
    suspend fun getWish(
        wishId: Int
    ): BaseApi<NetworkModelWish> {
        return try {
            val response = client.api.get(urlString = "/test/users/wishes/${wishId}/")
            response.body<BaseApi<NetworkModelWish>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Изменить Желание
     *
     * @param [Uri] PUT /test/users/me/wishes/{wish_id}/
     * @param [Int] wish_id (path)
     * @param [CreatingWish] {
     *
     *  "title": [String],
     *
     *  "description": [String],
     *
     *  "price": [Int],
     *
     *  "link": [String],
     *
     *   "wishlist_id": [Int]
     *
     * }
     * @return [NetworkModelWish]
     */
    suspend fun putWish(
        wishId: Int,
        updatingWish: CreatingWish,
    ): BaseApi<NetworkModelWish> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/wishes/${wishId}/",
                body = updatingWish
            )
            response.body<BaseApi<NetworkModelWish>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Удалить Желание
     *
     * @param [Uri] DELETE /test/users/me/wishes/{wish_id}/
     * @param [Int] wish_id (path)

     * @return [Nothing]
     */
    suspend fun deleteWish(
        wishId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/users/me/wishes/${wishId}/",
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Изменить Обложку Желания
     *
     * @param [Uri] POST /test/users/me/wishes/{wish_id}/cover/
     * @param [Int] wish_id (path)
     * @param [File] new_cover (body)
     * @return [CreatingWish]
     */
    suspend fun postWishCover(
        wishId: Int,
        file: File,
    ): BaseApi<NetworkModelWish> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/wishes/${wishId}/cover/",
                body = MultiPartFormDataContent(formData {
                    append("new_cover", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/*")
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                })
            )
            response.body<BaseApi<NetworkModelWish>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  Забронировать Желание
     *
     * @param [Uri] POST /test/users/wishes/{wish_id}/fulfillments/
     * @param [Int] wish_id (path)
     * @return [CreatingWish]
     */
    suspend fun postWishFulfillment(
        wishId: Int,
    ): BaseApi<NetworkModelWish> {
        return try {
            val response = client.api.post(
                urlString = "/test/users/wishes/${wishId}/fulfillments/",
            )
            response.body<BaseApi<NetworkModelWish>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**Желания--------------------------------------------------------------*/

    /**
     *  Получить Список Желаний
     *
     * @param [Uri] GET /test/users/me/wishlists/
     * @param [String] title (query)
     * @param [Int] page (query)
     * @return List [NetworkModelWishlist]
     */
    suspend fun getWishlists(
        title: String?,
        page: Int?,
    ): BaseApi<List<NetworkModelWishlist>> {
        return try {
            val queryParams = Parameters.build {
                title?.let { append("title", title.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/users/me/wishlists/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
            response.body<BaseApi<List<NetworkModelWishlist>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Добавить Желание
     *
     * @param [Uri] POST /test/users/me/wishlists/
     * @param [CreatingWishlist] {
     *
     *  "title": [String],
     *
     *  "is_secret": [Boolean],
     *
     *  "author_ids": List [Int],
     *
     * }
     * @return [NetworkModelWishlist]
     */
    suspend fun postWishlist(
        bodyWishlist: CreatingWishlist,
    ): BaseApi<NetworkModelWishlist> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/wishlists/",
                body = bodyWishlist
            )
            response.body<BaseApi<NetworkModelWishlist>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Получить Желание
     *
     * @param [Uri] GET /test/users/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)
     * @return [NetworkModelWishlist]
     */
    suspend fun getWishlist(
        wishlistId: Int
    ): BaseApi<NetworkModelWishlist> {
        return try {
            val response = client.api.get(urlString = "/test/users/wishlists/${wishlistId}/")
            response.body<BaseApi<NetworkModelWishlist>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**
     *  Изменить Желание
     *
     * @param [Uri] PUT /test/users/me/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)
     * @param [CreatingWishlist] {
     *
     *  "title": [String],
     *
     *  "is_secret": [Boolean],
     *
     *  "author_ids": List [Int],
     *
     * }
     * @return [NetworkModelWishlist]
     */
    suspend fun putWishlist(
        wishlistId: Int,
        bodyWishlist: CreatingWishlist,
    ): BaseApi<NetworkModelWishlist> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/wishlists/${wishlistId}/",
                body = bodyWishlist
            )
            response.body<BaseApi<NetworkModelWishlist>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**
     *  Удалить Желание
     *
     * @param [Uri] DELETE /test/users/me/wishlists/{wishlist_id}/
     * @param [Int] wishlist_id (path)

     * @return [Nothing]
     */
    suspend fun deleteWishlist(
        wishlistId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/users/me/wishlists/${wishlistId}/",
            )
            response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}