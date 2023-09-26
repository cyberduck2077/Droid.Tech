package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.Parameters
import ru.data.common.models.network.CreatingComment
import ru.data.common.models.network.CreatingPost
import ru.data.common.models.network.CreatingPostReport
import ru.data.common.models.network.NetworkModelComment
import ru.data.common.models.network.NetworkModelPost
import ru.data.common.models.network.NetworkModelPostWithComment
import ru.data.common.models.network.IsVoteBody
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingComment
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest

/**
 * Клиентское приложение / Посты
 */
class ApiPosts(private val client: Client) {

    /**
     *  Получить Все Посты
     * @param [Uri] GET /test/posts/
     * @param List_Int user_ids (query)
     * @param List_Int Droid_ids (query)
     * @param [Long] published_from (query)
     * @param [Long] published_to (query)
     * @param [Boolean] with_polling (query)
     * @param [Boolean] with_text (query)
     * @param [Boolean] with_photo (query)
     * @param [Boolean] with_video (query)
     * @param [Int] page (query)
     *
     * @return [List [NetworkModelPostWithComment]]
     */
    suspend fun getPosts(
        userIds: List<Int>?,
        DroidIds: List<Int>?,
        publishedFrom: Long?,
        publishedTo: Long?,
        withPolling: Boolean?,
        withText: Boolean?,
        withPhoto: Boolean?,
        withVideo: Boolean?,
        page: Int?,
    ): BaseApi<List<NetworkModelPostWithComment>> {
        return try {
            val queryParams = Parameters.build {
                userIds?.forEach { id -> append("user_ids", id.toString()) }
                DroidIds?.forEach { id -> append("Droid_ids", id.toString()) }
                publishedFrom?.let { append("published_from", publishedFrom.toString()) }
                publishedTo?.let { append("published_to", publishedTo.toString()) }
                withPolling?.let { append("with_polling", withPolling.toString()) }
                withText?.let { append("with_text", withText.toString()) }
                withPhoto?.let { append("with_photo", withPhoto.toString()) }
                withVideo?.let { append("with_video", withVideo.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/posts/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
       response.body<BaseApi<List<NetworkModelPostWithComment>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     *  Получить Пост По Идентификатору
     * @param [Uri] GET /test/posts/{post_id}/
     * @param [Int] post_id (path)
     * @return [NetworkModelPostWithComment]
     */
    suspend fun getPostById(
        postId: Int,
    ): BaseApi<NetworkModelPostWithComment> {
        return try {
            val response = client.api.get(
                urlString = "/test/posts/${postId}/",
            )
          response.body<BaseApi<NetworkModelPostWithComment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Создать Пост
     * @param [Uri] POST /test/posts/
     * @param [CreatingPost] (body)
     * @return [NetworkModelPostWithComment]
     */
    suspend fun postPost(
        body: CreatingPost,
    ): BaseApi<NetworkModelPostWithComment> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/posts/",
                body = body
            )
         response.body<BaseApi<NetworkModelPostWithComment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     * Изменить Пост
     * @param [Uri] PUT /test/posts/{post_id}/
     * @param [Int] post_id (path)
     * @param [CreatingPost] (body)
     * @return [NetworkModelPostWithComment]
     */
    suspend fun putPost(
        postId: Int,
        body: CreatingPost,
    ): BaseApi<NetworkModelPostWithComment> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/posts/${postId}/",
                body = body
            )
        response.body<BaseApi<NetworkModelPostWithComment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     * Удалить Пост
     * @param [Uri] DELETE /test/posts/{post_id}/
     * @param [Int] post_id (path)
     * @return [Nothing]
     */
    suspend fun deletePost(
        postId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/posts/${postId}/",
            )
          response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     *  Поставить Или Снять Лайк Посты
     * @param [Uri] PUT /test/posts/{post_id}/is-vote/
     * @param [Int] post_id (path)
     * @param [IsVoteBody] is_vote (body)
     * @return [NetworkModelPost]
     */
    suspend fun putLike(
        postId: Int,
        isVote: Boolean,
    ): BaseApi<NetworkModelPost> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/posts/${postId}/is-vote/",
                body = IsVoteBody(isVote)
            )
            response.body<BaseApi<NetworkModelPost>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Пожаловаться На Пост
     * @param [Uri] POST /test/posts/{post_id}/reports/
     * @param [Int] post_id (path)
     * @param [CreatingPostReport] (body)
     * @return [Nothing]
     */
    suspend fun postReportsPost(
        postId: Int,
        body: CreatingPostReport,
    ): BaseApi<Nothing> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/posts/${postId}/reports/",
                body = body
            )
           response.body<BaseApi<Nothing>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }


    /**
     * Получить Все Комментарии К Истории
     * @param [Uri] GET /test/posts/{post_id}/comments/
     * @param [Int] post_id (path)
     *
     * @param [Int] parent_id (query)
     * @param [Boolean] with_children (query)
     * @param [Int] page (query)
     *
     * @return [List [NetworkModelComment]]
     */
    suspend fun getCommentsById(
        postId: Int,
        page: Int?,
        parentId: Int?,
        withChildren: Boolean?,
    ): BaseApi<List<NetworkModelComment>> {
        return try {
            val queryParams = Parameters.build {
                parentId?.let { append("parent_id", parentId.toString()) }
                withChildren?.let { append("with_children", withChildren.toString()) }
                page?.let { append("page", page.toString()) }
            }
            val response = client.api.get(
                urlString = "/test/posts/${postId}/comments/",
                block = {
                    url { parameters.appendAll(queryParams) }
                }
            )
         response.body<BaseApi<List<NetworkModelComment>>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Добавить Комментарий К Истории
     * @param [Uri] POST /test/posts/{post_id}/comments/
     * @param [Int] post_id (path)
     *
     * @param [CreatingComment] (body)
     *
     * @return [NetworkModelComment]
     */
    suspend fun postCommentsById(
        postId: Int,
        message: String,
        parentId: Int?,
    ): BaseApi<NetworkModelComment> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/posts/${postId}/comments/",
                body = CreatingComment(
                    text = message,
                    parent_id = parentId
                )
            )
            response.body<BaseApi<NetworkModelComment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Изменить Комментарий К Истории
     * @param [Uri] PUT /test/comments/{comment_id}/
     * @param [Int] comment_id (path)
     *
     * @param [UpdatingComment] (body)
     *
     * @return [NetworkModelComment]
     */
    suspend fun putCommentsById(
        commentId: Int,
        message: String,
    ): BaseApi<NetworkModelComment> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/comments/${commentId}/",
                body = UpdatingComment(text = message)
            )
            response.body<BaseApi<NetworkModelComment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

}