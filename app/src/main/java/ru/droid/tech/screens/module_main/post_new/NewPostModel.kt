package ru.droid.tech.screens.module_main.post_new

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TopicUI

class NewPostModel(
    private val apiPosts: UseCasePosts,
    private val context: Context,
) : BaseModel() {

    private val _gettingPost = MutableStateFlow<PostWithCommentUI?>(null)
    val gettingPost = _gettingPost.asStateFlow()

    private val _gettingTopics = MutableStateFlow<List<TopicUI>>(listOf())
    val gettingTopics = _gettingTopics.asStateFlow()

    private val _isUpdatePost = MutableStateFlow(false)
    val isUpdatePost = _isUpdatePost.asStateFlow()

    fun getTopics() = coroutineScope.launch {
        _gettingTopics.update { apiPosts.getTopics() }
    }

    fun setNetworkModelPost(post: Int?) = coroutineScope.launch {
        _isUpdatePost.value = post != null
        if (post == null) return@launch
        apiPosts.getPostById(
            postId = post,
            flowStart = ::gDLoaderStart,
            flowSuccess = { resp ->
                _gettingPost.value = resp
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {},
            flowMessage = {})
    }

    fun updatePost(
        text: String,
        attachmentNew: List<String>,
        attachment: List<AttachmentUI>,
        topic: TopicUI?,
        isPrivate: Boolean, // TODO("isPrivate")
    ) = coroutineScope.launch {
        val post = _gettingPost.value ?: return@launch
        apiPosts.updatePostWithImage(
            idPost = post.id,
            topicId = topic?.id,
            text = text,
            listImage = attachmentNew,
            listImageAttachment = attachment,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                _gettingPost.value = it
                _isUpdatePost.value = true
                goBackStack()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun sendNewPost(
        text: String,
        attachment: List<String>,
        topic: TopicUI?,
        isPrivate: Boolean,// TODO("isPrivate")
    ) = coroutineScope.launch {
        apiPosts.postPostWithImage(
            text = text,
            topicId = topic?.id,
            listImage = attachment,
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                _gettingPost.value = it
                _isUpdatePost.value = true
                goBackStack()
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }
}