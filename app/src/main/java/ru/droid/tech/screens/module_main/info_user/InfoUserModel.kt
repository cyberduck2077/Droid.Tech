package ru.droid.tech.screens.module_main.info_user

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.chat_user.ChatUser
import ru.droid.tech.screens.module_main.post_with_comment.PostWithComment
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.FriendshipStatuses
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TypeReason
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.local.screens.StatusScreenInfoUser


class InfoUserModel(
    private val apiUsers: UseCaseUsers,
    private val apiPosts: UseCasePosts,
) : BaseModel() {

    private val _user = MutableStateFlow<UserUI?>(null)
    val user = _user.asStateFlow()

    private val _cityUser = MutableStateFlow<City?>(null)
    val cityUser = _cityUser.asStateFlow()

    private val _cityUserOrigin = MutableStateFlow<City?>(null)
    val cityUserOrigin = _cityUserOrigin.asStateFlow()

    private val _userDroid = MutableStateFlow<DroidUI?>(null)
    val userDroid = _userDroid.asStateFlow()

    private val _menuRibbon = MutableStateFlow(MenuProfile.MEDIA)
    val menuRibbon = _menuRibbon.asStateFlow()

    private val _screenStatus = MutableStateFlow<StatusScreenInfoUser>(StatusScreenInfoUser.Load)
    val screenStatus = _screenStatus.asStateFlow()

    private val _listPosts = MutableStateFlow(listOf<PostWithCommentUI>())
    val listPosts = _listPosts.asStateFlow()

    private val _listMedia = MutableStateFlow(listOf<MediaUI>())
    val listMedia = _listMedia.asStateFlow()

    private val _listWishes = MutableStateFlow(listOf<WishUI>())
    val listWishes = _listWishes.asStateFlow()


    fun getUserInfo(userId: Int) = coroutineScope.launch {
        apiUsers.getUser(
            userId = userId,
            flowStart = {},
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
            flowSuccess = ::setUserData
        )
    }

    private fun setUserData(respUser: UserUI) = coroutineScope.launch {
        _user.update { respUser }
        _listPosts.update {
            apiPosts.getAllPosts(userIds = listOf(respUser.id))
        }

        _listMedia.update {
            listOf() // TODO("_listMedia InfoUser empty")
        }

        _listWishes.update {
            listOf() // TODO("_listWishes InfoUser empty")
        }

        when (respUser.friendshipStatus) {
            FriendshipStatuses.YOU_SENT_REQUEST_ACCEPTED,
            FriendshipStatuses.SENT_TO_YOU_REQUEST_ACCEPTED -> {
                _screenStatus.update { StatusScreenInfoUser.UserInMyDroid(respUser) }
            }

            else                                            -> {
                _screenStatus.update { StatusScreenInfoUser.UserSomeone(respUser) }
            }
        }
    }

    fun removeUserFromDroid() {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick removeUserFromDroid  NO API")*/
        message("Stub")
    }

    fun likePost(postId: Int, vote: Boolean) = coroutineScope.launch {
        apiPosts.putLike(
            postId = postId,
            isVote = vote,
            flowStart = {},
            flowSuccess = { result ->
                _listPosts.update { listOld ->
                    listOld.map {
                        if (it.id == postId) {
                            it.copy(isVote = result.isVote)
                        } else {
                            it
                        }
                    }
                }
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::gDMessage
        )
    }

    fun chooseMenu(menu: MenuProfile) {
        _menuRibbon.update { menu }
    }

    fun blockUser() {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick blockUser  NO API")*/
        message("Stub")
    }

    fun complainUser(typeReason: TypeReason, text: String) {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick complainUser NO API")*/
        message("Stub")
    }

    fun addInDroidUser(text: String) {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick complainUser  NO API")*/
        message("Stub")
    }

    fun setShowNews(isShow: Boolean) {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick setShowNews  NO API")*/
        message("Stub")
    }

    fun goToAllMedia() {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick goToAllMedia  NO API")*/
        message("Stub")
    }


    fun goToWishList() {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick goToWishList  NO API")*/
        message("Stub")
    }

    fun goToDroidUser() {
        val userId: Int = user.value?.id ?: return
        /**TODO("onClick goToDroidUser  NO API")*/
        message("Stub")
    }

    fun goToPostWithComment(postId: Int) {
        getNavigationLevel(NavLevel.MAIN)?.push(PostWithComment(postId))
    }

    fun goToInfoUser(userId: Int?) {
        userId ?: return
        getNavigationLevel(NavLevel.MAIN)?.push(InfoUser(userId))
    }

    fun goToChat() {
        val userId: Int = user.value?.id ?: return
        getNavigationLevel(NavLevel.MAIN)?.push(ChatUser(userId))
    }

}
