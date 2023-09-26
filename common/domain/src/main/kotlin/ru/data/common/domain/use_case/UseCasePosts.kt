package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.data.common.db.CityDaoRoom
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.CommentUI
import ru.data.common.models.local.maps.FilterPosts
import ru.data.common.models.local.maps.PostUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TopicUI
import ru.data.common.models.local.maps.TypeReason
import ru.data.common.models.network.CreatingPost
import ru.data.common.models.network.CreatingPostReport
import ru.data.common.models.network.NetworkModelAttachment
import ru.data.common.network.api.ApiAttachments
import ru.data.common.network.api.ApiPosts
import ru.data.common.network.api.ApiTopics
import java.io.File

class UseCasePosts(
    private val apiPosts: ApiPosts,
    private val apiTopics: ApiTopics,
    private val apiAttachments: ApiAttachments,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getAllPostsPagination(
        userIds: List<Int>? = null,
        DroidIds: List<Int>? = null,
        filterPosts: FilterPosts = FilterPosts(),
        page: Int? = null,
        listStatus: (
            hasNext: Boolean?,
            hasPrev: Boolean?,
            codeResponse: Int,
        ) -> Unit
    ): List<PostWithCommentUI> {
        val response = apiPosts.getPosts(
            userIds = userIds,
            DroidIds = DroidIds,
            publishedFrom = filterPosts.publishedFrom,
            publishedTo = filterPosts.publishedTo,
            withPolling = filterPosts.getPollingChoose(),
            withText = filterPosts.getTextChoose(),
            withPhoto = filterPosts.getPhotoChoose(),
            withVideo = filterPosts.getVideoChoose(),
            page = page
        )

        listStatus.invoke(
            response.meta?.paginator?.has_next,
            response.meta?.paginator?.has_prev,
            response.getCodeResponse()
        )

        return response.data?.map { data ->
            PostWithCommentUI.mapFrom(
                post = data,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
        } ?: listOf()
    }

    suspend fun getAllPosts(
        userIds: List<Int>? = null,
        DroidIds: List<Int>? = null,
        filterPosts: FilterPosts = FilterPosts(),
        page: Int? = null,
    ): List<PostWithCommentUI> = apiPosts.getPosts(
        userIds = userIds,
        DroidIds = DroidIds,
        publishedFrom = filterPosts.publishedFrom,
        publishedTo = filterPosts.publishedTo,
        withPolling = filterPosts.getPollingChoose(),
        withText = filterPosts.getTextChoose(),
        withPhoto = filterPosts.getPhotoChoose(),
        withVideo = filterPosts.getVideoChoose(),
        page = page
    ).data?.map { data ->
        PostWithCommentUI.mapFrom(
            post = data,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()


    suspend fun putLike(
        postId: Int,
        isVote: Boolean,
        flowStart: () -> Unit = {},
        flowSuccess: (PostUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiPosts.putLike(postId = postId, isVote = isVote)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }


        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            val data = PostUI.mapFrom(
                post = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun getPostById(
        postId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (PostWithCommentUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiPosts.getPostById(postId = postId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            val data = PostWithCommentUI.mapFrom(
                post = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postPost(
        body: CreatingPost,
        flowStart: () -> Unit = {},
        flowSuccess: (PostWithCommentUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiPosts.postPost(body = body)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            val data = PostWithCommentUI.mapFrom(
                post = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }

    suspend fun postPostWithImage(
        text: String,
        topicId: Int?,
        listImage: List<String>,
        compressedFile: (String) -> File,
        flowStart: () -> Unit = {},
        flowSuccess: (PostWithCommentUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val listMediaUris = mutableListOf<NetworkModelAttachment>()
        listImage.forEach { media ->
            val compressedImageFile = compressedFile.invoke(media)

            val resp = apiAttachments.postAttachment(compressedImageFile)

            if (resp.status == HttpStatusCode.Unauthorized) {
                flowStop.invoke()
                flowMessage.invoke(resp.getDescriptionRudApi())
                flowUnauthorized.invoke()
                return
            }

            resp.data?.let {
                listMediaUris.add(it)
            } ?: run {
                flowMessage.invoke(resp.getDescriptionRudApi())
            }
        }

        val response = apiPosts.postPost(
            body = CreatingPost(
                text = text,
                published = null,
                attachment_ids = listMediaUris.map { it.id },
                media_ids = listOf(),
                polling_id = null,
                topic_id = topicId
            )
        )

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }

        response.data?.let { newData ->
            val data = PostWithCommentUI.mapFrom(
                post = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        }
        flowMessage.invoke(response.getDescriptionRudApi())
        flowStop.invoke()
    }

    suspend fun updatePostWithImage(
        idPost: Int,
        text: String,
        topicId: Int?,
        compressedFile: (String) -> File,
        listImage: List<String>,
        listImageAttachment: List<AttachmentUI>,
        flowStart: () -> Unit = {},
        flowSuccess: (PostWithCommentUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val listMediaUris = mutableListOf<NetworkModelAttachment>()
        listImage.forEach { media ->
            val compressedImageFile = compressedFile.invoke(media)
            val resp = apiAttachments.postAttachment(compressedImageFile)

            if (resp.status == HttpStatusCode.Unauthorized) {
                flowStop.invoke()
                flowMessage.invoke(resp.getDescriptionRudApi())
                flowUnauthorized.invoke()
                return
            }

            resp.data?.let {
                listMediaUris.add(it)
            } ?: run {
                flowMessage.invoke(resp.getDescriptionRudApi())
            }
        }

        val response = apiPosts.putPost(
            postId = idPost,
            body = CreatingPost(
                text = text,
                published = null,
                attachment_ids = listMediaUris.map { it.id } + listImageAttachment.map { it.id },
                media_ids = listOf(),
                polling_id = null,
                topic_id = topicId,
            )
        )

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }

        response.data?.let { newData ->
            val data = PostWithCommentUI.mapFrom(
                post = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        }
        flowMessage.invoke(response.getDescriptionRudApi())
        flowStop.invoke()
    }

    suspend fun postReportsPost(
        postId: Int,
        reason: TypeReason,
        additionalText: String,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiPosts.postReportsPost(
            postId = postId,
            body = CreatingPostReport(
                reason = reason,
                additional_text = additionalText
            )
        )

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let {
            flowSuccess.invoke()
        }
        flowMessage.invoke(data.getDescriptionRudApi())
        flowStop.invoke()
    }


    suspend fun deletePost(
        postId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val data = apiPosts.deletePost(postId = postId)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        flowMessage.invoke(data.getDescriptionRudApi())
        flowSuccess.invoke()
        flowStop.invoke()
    }


    suspend fun getCommentsByPostId(
        postId: Int,
        page: Int?,
        parentId: Int?,
        withChildren: Boolean?,
    ): List<CommentUI> = apiPosts.getCommentsById(
        postId = postId,
        page = page,
        parentId = parentId,
        withChildren = withChildren,
    ).data?.map {
        CommentUI.mapFore(
            comment = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()

    suspend fun postCommentsById(
        postId: Int,
        parentId: Int?,
        message: String,
        flowStart: () -> Unit = {},
        flowSuccess: (CommentUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiPosts.postCommentsById(
            postId = postId,
            message = message,
            parentId = parentId
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            return
        }
        response.data?.let { newData ->
            val data = CommentUI.mapFore(
                comment = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
        }
        flowStop.invoke()
    }


    suspend fun getTopics(
        page: Int? = null,
        name: String? = null,
    ): List<TopicUI> = apiTopics.getTopics(
        page = page,
        name = name
    ).data?.map { TopicUI.mapFrom(it) } ?: listOf()
}