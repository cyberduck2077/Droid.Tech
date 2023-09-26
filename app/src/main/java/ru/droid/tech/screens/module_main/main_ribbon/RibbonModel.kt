package ru.droid.tech.screens.module_main.main_ribbon

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.base.BasePagination
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.info_user.InfoUser
import ru.droid.tech.screens.module_main.notification.Notification
import ru.droid.tech.screens.module_main.post_new.NewPost
import ru.droid.tech.screens.module_main.post_with_comment.PostWithComment
import ru.droid.tech.screens.module_main.profile.ProfileScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.FilterPosts
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TypeReason
import ru.data.common.models.local.maps.UserUI

class RibbonModel(
    private val apiUser: UseCaseUser,
    private val apiUsers: UseCaseUsers,
    private val apiPosts: UseCasePosts,
    private val apiDroid: UseCaseDroid,
) : BaseModel() {

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _screenRibbon = MutableStateFlow(RibbonStatusScr.ALL)
    val screenRibbon = _screenRibbon.asStateFlow()

    private val _listDroid = MutableStateFlow(listOf<DroidUI>())
    val listDroid = _listDroid.asStateFlow()

    private val _listUserSearch = MutableStateFlow(listOf<UserUI>())
    val listUserSearch = _listUserSearch.asStateFlow()

    private val _filter = MutableStateFlow(FilterPosts())
    val filter = _filter.asStateFlow()

    private val _filterDroid = MutableStateFlow(listOf<DroidUI>())
    val filterDroid = _filterDroid.asStateFlow()

    private val scopeForePagination =
        CoroutineScope(coroutineScope.coroutineContext + Dispatchers.IO)
    private var jobSearch: Job = Job()

    val pagerAllRibbon = BasePagination(
        scope = scopeForePagination,
        unit = { pageCurrent, statusPage ->
            apiPosts.getAllPostsPagination(
                userIds = null,
                DroidIds = null,
                filterPosts = filter.value,
                page = pageCurrent,
                listStatus = statusPage,
            )
        }
    )

    val pagerDroidRibbon = BasePagination(
        scope = scopeForePagination,
        unit = { pageCurrent, statusPage ->
            apiPosts.getAllPostsPagination(
                page = pageCurrent,
                listStatus = statusPage,
                userIds = null,
                DroidIds = filterDroid.value.map { it.id },
                filterPosts = filter.value,
            )
        }
    )

    init {
        getMyDroid()
        onSearchUser(userData.value.lastName)
    }

    private fun refreshStatus() {
        when (screenRibbon.value) {
            RibbonStatusScr.ALL -> pagerAllRibbon.refreshFlow()
            RibbonStatusScr.Droid -> pagerDroidRibbon.refreshFlow()
        }
    }

    fun setFilter(newFilter: FilterPosts) {
        _filter.value = newFilter
        refreshStatus()
    }

    fun deleteMyPost(post: PostWithCommentUI) = coroutineScope.launch(Dispatchers.IO) {
        apiPosts.deletePost(
            postId = post.id,
            flowStart = {},
            flowSuccess = {
                refreshStatus()
            },
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun setFilterDroid(list: List<DroidUI>) {
        _filterDroid.value = list
        refreshStatus()
    }

    fun onSearchUser(str: String?) {
        jobSearch.cancel()
        if (str.isNullOrEmpty()) return
        jobSearch = coroutineScope.launch(Dispatchers.IO) {
            delay(500)
            _listUserSearch.update {
                apiUsers.getUsers(page = 1,
                    fullName = str,
                    listStatus = { _, _, _ -> })
            }
        }
    }

    fun likePost(post: PostWithCommentUI) = coroutineScope.launch(Dispatchers.IO) {
        val value = !(post.isVote ?: true)
        apiPosts.putLike(
            postId = post.id,
            isVote = value,
            flowStart = {},
            flowSuccess = { result ->
                pagerAllRibbon.refreshFlow()
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::gDMessage
        )
    }

    fun reportsPost(
        postId: Int,
        typeReason: TypeReason,
        text: String
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiPosts.postReportsPost(
            postId = postId,
            reason = typeReason,
            additionalText = text,
            flowStart = ::gDLoaderStart,
            flowSuccess = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun chooseMenu(stage: RibbonStatusScr) {
        _screenRibbon.value = stage
        refreshStatus()
    }

    fun getMyDroid() = coroutineScope.launch(Dispatchers.IO) {
        apiDroid.getMyDroid(
            flowStart = {},
            flowSuccess = {
                _listDroid.value = it
                _filterDroid.value = _listDroid.value
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::message
        )
    }

    fun goToRemakeMyPost(post: PostWithCommentUI) {
        getNavigationLevel(NavLevel.MAIN)?.push(NewPost(post.id))
    }

    fun goToInfoUser(userId: Int) {
        getNavigationLevel(NavLevel.MAIN)?.push(InfoUser(userId))
    }

    fun goToMyPost() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewPost())
    }

    fun goToPostWithComment(post: PostWithCommentUI) {
        getNavigationLevel(NavLevel.MAIN)?.push(PostWithComment(post.id))
    }

    fun goToProfile() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileScreen())
    }

    fun goToNotification() {
        getNavigationLevel(NavLevel.MAIN)?.push(Notification())
    }

    override fun onDispose() {
        scopeForePagination.cancel()
        super.onDispose()
    }
}
