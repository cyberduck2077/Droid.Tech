package ru.droid.tech.screens.module_main.profile

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.core_main.HomeMainScreen
import ru.droid.tech.screens.module_main.info_user.InfoUser
import ru.droid.tech.screens.module_main.media_and_albums_all.MediaAndAlbumsAllScreen
import ru.droid.tech.screens.module_main.media_list.MediaListScreen
import ru.droid.tech.screens.module_main.post_new.NewPost
import ru.droid.tech.screens.module_main.post_with_comment.PostWithComment
import ru.droid.tech.screens.module_main.profile_redaction.ProfileRedactionScreen
import ru.droid.tech.screens.module_main.show_Droid.ShowDroid
import ru.droid.tech.screens.module_main.wish_detail.DetailWish
import ru.droid.tech.screens.module_main.wish_new.NewWish
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.memory.gDSetScreenMain
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.DataForPostingMedia
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.local.screens.ScreenStatusShortContent
import ru.data.common.models.local.screens.ScreensHome
import ru.data.common.models.res.TextApp

class ProfileModel(
    private val apiUser: UseCaseUser,
    private val apiDroid: UseCaseDroid,
    private val apiMedia: UseCaseMedia,
    private val apiPosts: UseCasePosts,
    private val apiWishesAndList: UseCaseWishesAndList,
    private val context: Context,
) : BaseModel() {

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _listDroid = MutableStateFlow(listOf<DroidUI>())
    val listDroid = _listDroid.asStateFlow()

    private val _listMedia = MutableStateFlow(listOf<MediaUI>())
    val listMedia = _listMedia.asStateFlow()

    private val _listWishes = MutableStateFlow(listOf<WishUI>())
    val listWishes = _listWishes.asStateFlow()

    private val _listPosts = MutableStateFlow(listOf<PostWithCommentUI>())
    val listPosts = _listPosts.asStateFlow()

    private val _menuProfile = MutableStateFlow(MenuProfile.MEDIA)
    val menuProfile = _menuProfile.asStateFlow()

    private val _chooserDroid = MutableStateFlow<DroidUI?>(null)
    val chooserDroid = _chooserDroid.asStateFlow()

    private val _screenStatus =
        MutableStateFlow<ScreenStatusShortContent>(ScreenStatusShortContent.Load)
    val screenStatus = _screenStatus.asStateFlow()


    init {
        coroutineScope.launch {
            _screenStatus.update { ScreenStatusShortContent.Load }
            val me = getMe()
            val myDroid = getMyDroid()
            val media = getMedia()
            val wishesAndList = getWishesAndList()
            joinAll(
                me,
                myDroid,
                media,
                wishesAndList,
            )
            getPosts()
            _screenStatus.update { ScreenStatusShortContent.Ready }
        }
    }

    private suspend fun getMedia() = coroutineScope.launch(Dispatchers.IO) {
        _listMedia.value = apiMedia.getMedias()
    }

    private suspend fun getPosts() = coroutineScope.launch(Dispatchers.IO) {
        _listPosts.value =
            apiPosts.getAllPosts(userIds = listOf(userData.value.id))
    }

    private suspend fun getWishesAndList() = coroutineScope.launch(Dispatchers.IO) {
        _listWishes.value = apiWishesAndList.getWishes(page = 1)
    }

    private suspend fun getMe() = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getMe(
            flowStart = {},
            flowSuccess = {
                _userData.value = it
            },
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
            flowError = {

            }
        )
    }

    fun getMyDroid() = coroutineScope.launch(Dispatchers.IO) {
        apiDroid.getMyDroid(
            flowStart = {},
            flowSuccess = {
                _listDroid.value = it
                initChooseDroidId()
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::message
        )
    }

    fun changeDroid(Droid: DroidUI) = coroutineScope.launch(Dispatchers.IO) {
        apiUser.setChooseDroidId(Droid.id)
        _chooserDroid.value = Droid
    }

    private fun initChooseDroidId() = coroutineScope.launch(Dispatchers.IO) {
        apiUser.getChooseDroidId()?.let { loadDroid(it) } ?: run {
            _listDroid.value.firstOrNull()?.let {
                changeDroid(it)
            }
        }
    }

    fun chooseMenu(menu: MenuProfile) {
        _menuProfile.value = menu
    }

    fun loadDroid(id: Int) = coroutineScope.launch(Dispatchers.IO) {
        apiDroid.getDroidId(
            id = id,
            flowStart = {},
            flowSuccess = {
                _chooserDroid.value = it
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::message
        )
    }

    fun likePost(post: PostWithCommentUI) = coroutineScope.launch(Dispatchers.IO) {
        val value = !(post.isVote ?: true)
        apiPosts.putLike(
            postId = post.id,
            isVote = value,
            flowStart = {},
            flowSuccess = { result ->
                val mutList = _listPosts.value.toMutableList()
                mutList.replaceAll { if (it == post) it.copy(isVote = value) else it }
                _listPosts.value = mutList
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = {},
            flowMessage = ::gDMessage
        )
    }

    fun uploadPhoto(image: List<Uri>) = coroutineScope.launch(Dispatchers.IO) {
        apiMedia.postNewMedia(
            mediaList = image.map { imageItem ->
                DataForPostingMedia(
                    uri = imageItem.toString(),
                    album_id = null,//загружает в общий альбом
                    name = null,//todo()
                    is_favorite = true,//todo()
                    address = null,//todo()
                    lat = null,//todo()
                    lon = null,//todo()
                    happened = null,//todo()
                    description = null,//todo()
                )
            },
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                gDSetLoader(false)
                _listMedia.value = it
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message,
        )
    }

    fun goToViewScreen(mediaId: Int) {
        navigator.push(MediaListScreen(mediaId = mediaId))
    }


    fun goToRedaction() {
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionScreen())
    }

    fun goToDroid() {
        getNavigationLevel(NavLevel.MAIN)?.push(ShowDroid())
    }

    fun goToInfoUser(userId: Int?) {
        userId ?: kotlin.run {
            message(TextApp.textUserExist)
            return
        }
        getNavigationLevel(NavLevel.MAIN)?.push(InfoUser(userId))
    }

    fun goToPostWithComment(post: PostWithCommentUI) {
        navigator.push(PostWithComment(post.id))
    }

    fun goToWish(wish: WishUI) {
        navigator.push(DetailWish(wish.id))
    }

    fun goToWishes() {
        gDSetScreenMain(ScreensHome.GIFTS_SCREEN)
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(HomeMainScreen())
    }

    fun goToNewWish() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWish())
    }

    fun goToNewPost() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewPost())
    }

    fun goToAllMedia() {
        getNavigationLevel(NavLevel.MAIN)?.push(MediaAndAlbumsAllScreen())
    }

}