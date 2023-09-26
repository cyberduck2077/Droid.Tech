package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogContentDetailedInformation
import ru.droid.tech.base.common_composable.DialogReports
import ru.droid.tech.base.common_composable.DialogYesOrNo
import ru.droid.tech.base.common_composable.DialogZoomImagePager
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.local.maps.FriendshipStatuses
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.local.screens.StatusScreenInfoUser
import ru.data.common.models.res.TextApp

class InfoUser(private val userId: Int) : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<InfoUserModel>()
        var dialogDetailsUserDroid by rememberState { false }
        var dialogDetailsUserSomeone by rememberState { false }
        var dialogDeleteUser by rememberState { false }
        var dialogAddUserInDroid by rememberState { false }
        var dialogCoveringLetter by rememberState { false }
        var dialogReports by rememberState { false }
        var dialogBlockUser by rememberState { false }
        var dialogPagerImage by rememberState<Pair<List<AttachmentUI>, Int>?> { null }
        LifeScreen(onCreate = { model.getUserInfo(userId) })
        val user by model.user.collectAsState()
        val cityUser by model.cityUser.collectAsState()
        val cityUserOrigin by model.cityUserOrigin.collectAsState()
        val userDroid by model.userDroid.collectAsState()
        val menuRibbon by model.menuRibbon.collectAsState()
        val listPosts by model.listPosts.collectAsState()
        val listMedia by model.listMedia.collectAsState()
        val listWishes by model.listWishes.collectAsState()
        val screenStatus by model.screenStatus.collectAsState()

        val onBackPressed = remember(screenStatus) {
            {
                model.goBackStack()
            }
        }
        BackPressHandler(onBackPressed = onBackPressed)

        InfoUserScreen(
            screenStatus = screenStatus,
            onClickBack = onBackPressed,
            cityUser = cityUser,
            menuRibbon = menuRibbon,
            listPosts = listPosts,
            listMedia = listMedia,
            listMembers = userDroid?.members ?: listOf(),
            listWishes = listWishes,
            onClickDescriptions = {
                when (screenStatus) {
                    is StatusScreenInfoUser.UserInMyDroid -> {
                        dialogDetailsUserDroid = true
                    }

                    else                                   -> {
                        dialogDetailsUserSomeone = true
                    }
                }
            },
            onClickStatusButton = {
                when (user?.friendshipStatus) {
                    FriendshipStatuses.IS_NOT_A_FRIEND,
                    FriendshipStatuses.YOU_REJECTED,
                    FriendshipStatuses.YOUR_APPLICATION_WAS_REJECTED -> {
                        dialogAddUserInDroid = true
                    }

                    FriendshipStatuses.SENT_TO_YOU_RESPONSE_EXPECTED -> {
                        gDMessage("Stub")
                    }

                    FriendshipStatuses.SENT_TO_YOU_REQUEST_ACCEPTED  -> {
                        model.goToChat()
                    }

                    else                                             -> {}
                }
            },
            onClickComplain = {
                dialogReports = true
            },
            onClickComment = model::goToPostWithComment,
            onClickWishList = model::goToWishList,
            onClickBlockUser = {
                dialogBlockUser = true
            },
            onClickShowNews = model::setShowNews,
            onClickDeleteUser = {
                dialogDeleteUser = true
            },
            onClickAllMedia = model::goToAllMedia,
            onClickDroidUser = model::goToDroidUser,
            onClickInfoUser = model::goToInfoUser,
            onClickMenu = model::chooseMenu,
            onClickPostLike = model::likePost,
            onClickWish = { gDMessage("Stab") },
            onClickMedia = { gDMessage("Stab") },
            onClickImage = { list, index ->
                dialogPagerImage = Pair(list, index)
            },
        )

        if (dialogDetailsUserDroid) {
            DialogBottomSheet(
                onDismiss = {
                    dialogDetailsUserDroid = false
                }) {
                DialogContentDetailedInformation(
                    DroidName = userDroid?.getNameDroid(),
                    numDroid = userDroid?.members?.size,
                    birthday = user?.birthdateHuman,
                    city = cityUser?.name,
                    cityOrigin = cityUserOrigin?.name,
                    phone = user?.tel,
                    tg = user?.tg,
                    aboutYou = user?.description,
                    doing = user?.work,
                    interests = user?.interests?.joinToString(separator = ", "),
                    likeMusic = user?.favoriteMusic,
                    films = user?.favoriteMovies,
                    books = user?.favoriteBooks,
                    games = user?.favoriteGames,
                )
            }
        }

        if (dialogDetailsUserSomeone) {
            DialogBottomSheet(
                onDismiss = {
                    dialogDetailsUserSomeone = false
                }) {
                DialogContentDetailedInformation(
                    DroidName = userDroid?.getNameDroid(),
                    birthday = user?.birthdateHuman,
                    city = cityUser?.name,
                    cityOrigin = cityUserOrigin?.name,
                )
            }
        }

        if (dialogDeleteUser) {
            DialogYesOrNo(
                onDismiss = { dialogDeleteUser = false },
                onClick = { model.removeUserFromDroid() },
                title = TextApp.textDeletingPost,
                buttonOk = TextApp.textDelete,
                body = TextApp.formatDeletingUserInDroid(user?.firstName)
            )
        }

        if (dialogAddUserInDroid) {
            DialogYesOrNo(
                onDismiss = { dialogAddUserInDroid = false },
                onClick = { dialogCoveringLetter = true },
                title = TextApp.textAddToDroid,
                body = TextApp.textWriteAnAccompanying,
                buttonOk = TextApp.textWrite
            )
        }

        if (dialogReports) {
            DialogReports(
                onDismiss = { dialogReports = false },
                onClick = model::complainUser
            )
        }

        if (dialogBlockUser) {
            DialogYesOrNo(
                onDismiss = { dialogBlockUser = false },
                onClick = model::blockUser,
                title = TextApp.textBlock,
                body = TextApp.formatBlockUser(user?.firstName),
            )
        }

        if (dialogCoveringLetter) {
            ContentInfoUserCoveringLetter(
                onDismiss = { dialogCoveringLetter = false },
                onClickSendLetter = model::addInDroidUser
            )
        }

        dialogPagerImage?.let { attach ->
            DialogZoomImagePager(
                images = attach.first.map { it.url },
                dismiss = { dialogPagerImage = null },
                initPageNumb = attach.second,
            )
        }
    }


}

@Composable
private fun InfoUserScreen(
    onClickBack: () -> Unit,
    screenStatus: StatusScreenInfoUser,
    cityUser: City?,
    menuRibbon: MenuProfile,
    listPosts: List<PostWithCommentUI>,
    listMedia: List<MediaUI>,
    listWishes: List<WishUI>,
    listMembers: List<DroidMemberUI>,
    onClickDescriptions: () -> Unit,
    onClickStatusButton: () -> Unit,
    onClickComplain: () -> Unit,
    onClickBlockUser: () -> Unit,
    onClickShowNews: (Boolean) -> Unit,
    onClickDeleteUser: () -> Unit,
    onClickComment: (Int) -> Unit,
    onClickWishList: () -> Unit,
    onClickAllMedia: () -> Unit,
    onClickDroidUser: () -> Unit,
    onClickInfoUser: (Int?) -> Unit,
    onClickWish: (Int) -> Unit,
    onClickMenu: (ribbon: MenuProfile) -> Unit,
    onClickMedia: (media: MediaUI) -> Unit,
    onClickPostLike: (postId: Int, vote: Boolean) -> Unit,
    onClickImage: (List<AttachmentUI>, Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(
            onClickBack = onClickBack,
            container = ThemeApp.colors.background,
        )

        when (screenStatus) {
            StatusScreenInfoUser.Load -> {
                ContentInfoUserStatusLoad()
            }

            is StatusScreenInfoUser.UserInMyDroid -> {
                ContentInfoUserInMyDroid(
                    avatar = screenStatus.user.avatar,
                    firstAndLastName = screenStatus.user.getNameAndLastName(),
                    name = screenStatus.user.firstName ?: "",
                    statusButton = screenStatus.user.friendshipStatus,
                    isShowNews = false, //TODO("isShowNews")
                    locationCity = cityUser?.name,
                    onClickDescriptions = onClickDescriptions,
                    onClickStatusButton = onClickStatusButton,
                    onClickShowNews = onClickShowNews,
                    onClickDeleteUser = onClickDeleteUser,
                    onClickComplain = onClickComplain,
                    onClickBlockUser = onClickBlockUser,
                    menuRibbon = menuRibbon,
                    listPosts = listPosts,
                    listMedia = listMedia,
                    listWishes = listWishes,
                    onClickComment = onClickComment,
                    onClickWishList = onClickWishList,
                    onClickAllMedia = onClickAllMedia,
                    onClickDroidUsers = onClickDroidUser,
                    onClickInfoUser = onClickInfoUser,
                    onClickChooseMenu = onClickMenu,
                    onClickPostLike = onClickPostLike,
                    onClickWish = onClickWish,
                    onClickMedia = onClickMedia,
                    listMembers = listMembers,
                    onClickImage = onClickImage,
                )
            }

            is StatusScreenInfoUser.UserSomeone -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ContentInfoUserSomeone(
                        avatar = screenStatus.user.avatar,
                        firstAndLastName = screenStatus.user.getNameAndLastName(),
                        locationCity = cityUser?.name,
                        onClickDescriptions = onClickDescriptions,
                        onClickStatusButton = onClickStatusButton,
                        onClickComplain = onClickComplain,
                        onClickBlockUser = onClickBlockUser,
                        statusButton = screenStatus.user.friendshipStatus,
                    )
                }
            }
        }
    }
}