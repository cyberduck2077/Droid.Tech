package ru.droid.tech.screens.module_main.post_with_comment

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.post_new.NewPost
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.models.local.maps.CommentUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.res.TextApp

class PostWithCommentModel(
    private val apiPosts: UseCasePosts,
) : BaseModel() {

    private val _post = MutableStateFlow<PostWithCommentUI?>(null)
    val post = _post.asStateFlow()

    private val _comments = MutableStateFlow<List<CommentUI>>(listOf())
    val comments = _comments.asStateFlow()

    fun deleteMyPost() = coroutineScope.launch {
        post.value?.let { postValue ->
            apiPosts.deletePost(
                postId = postValue.id,
                flowStart = ::gDLoaderStart,
                flowSuccess = {
                    goBackStack()
                },
                flowStop = ::gDLoaderStop,
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowMessage = ::message
            )
        }
    }

    fun goToRemakeMyPost() {
        post.value?.id?.let { postValue ->
            getNavigationLevel(NavLevel.MAIN)?.push(NewPost(postValue))
        }
    }

    fun getPost(postId: Int) = coroutineScope.launch {
        apiPosts.getPostById(
            postId = postId,
            flowStart = ::gDLoaderStart,
            flowSuccess = { resp ->
                _post.value = resp
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {},
            flowMessage = {})
        getComments()
    }

    private fun getComments() = coroutineScope.launch {
        val postNotNull = post.value ?: kotlin.run {
            gDMessage(TextApp.textSomethingWentWrong)
            return@launch
        }
        _comments.value = apiPosts.getCommentsByPostId(
            postId = postNotNull.id,
            page = null,
            parentId = null,
            withChildren = true
        )
    }


    fun likePost() = coroutineScope.launch {
        if (_post.value != null) {
            val value = !(_post.value?.isVote ?: false)
            apiPosts.putLike(
                postId = _post.value!!.id,
                isVote = value,
                flowStart = {},
                flowSuccess = {
                    _post.value = _post.value!!.copy(isVote = value)
                },
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowStop = {},
                flowMessage = ::gDMessage
            )
        }
    }

    fun sendMessage(comment: String, id: Int?) = coroutineScope.launch {
        val postNotNull = post.value ?: kotlin.run {
            gDMessage(TextApp.textSomethingWentWrong)
            return@launch
        }
        apiPosts.postCommentsById(
            postId = postNotNull.id,
            parentId = id,
            message = comment,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                getComments()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {},
            flowMessage = {}
        )
    }
}
