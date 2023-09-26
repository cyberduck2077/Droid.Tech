package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonWeakSquareApp
import ru.droid.tech.base.common_composable.ItemsPost
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.local.maps.FriendshipStatuses
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.res.TextApp

@Composable
fun ColumnScope.ContentInfoUserInMyDroid(
    avatar: String?,
    firstAndLastName: String,
    name: String,
    isShowNews: Boolean,
    statusButton: FriendshipStatuses,
    locationCity: String?,
    onClickDescriptions: () -> Unit,
    onClickStatusButton: () -> Unit,
    onClickShowNews: (Boolean) -> Unit,
    onClickDeleteUser: () -> Unit,
    onClickComplain: () -> Unit,
    onClickBlockUser: () -> Unit,
    onClickWishList: () -> Unit,
    onClickWish: (Int) -> Unit,
    onClickAllMedia: () -> Unit,
    onClickMedia: (media: MediaUI) -> Unit,
    onClickDroidUsers: () -> Unit,
    onClickInfoUser: (Int?) -> Unit,
    onClickChooseMenu: (ribbon: MenuProfile) -> Unit,
    menuRibbon: MenuProfile,
    listMedia: List<MediaUI>,
    listWishes: List<WishUI>,
    listMembers: List<DroidMemberUI>,
    listPosts: List<PostWithCommentUI>,
    onClickPostLike: (postId: Int, vote: Boolean) -> Unit,
    onClickComment: (Int) -> Unit,
    onClickImage: (List<AttachmentUI>, Int) -> Unit
) {
    var expandedMero by rememberState { false }
    val onDismissDropMenu = remember {
        {
            expandedMero = false
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            ContentInfoUserProfileCart(
                avatar = avatar,
                name = firstAndLastName,
                locationCity = locationCity,
                onClickDescriptions = onClickDescriptions,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BoxSpacer()
                    ButtonAccentApp(
                        modifier = Modifier.weight(1f),
                        onClick = onClickStatusButton,
                        enabled = statusButton.isEnabledButton(),
                        text = statusButton.textButton()
                    )
                    BoxSpacer()
                    ButtonWeakSquareApp(
                        onClick = onClickDeleteUser,
                        icon = R.raw.ic_person_check
                    )
                    BoxSpacer()
                    Box(modifier = Modifier.wrapContentSize()) {
                        ButtonWeakSquareApp(
                            onClick = { expandedMero = !expandedMero },
                            icon = R.raw.ic_more
                        )
                        DropdownMenu(
                            modifier = Modifier
                                .background(color = ThemeApp.colors.backgroundVariant),
                            expanded = expandedMero,
                            onDismissRequest = onDismissDropMenu,
                        ) {
                            ContentInfoUserDropMenuItem(
                                onClick = {
                                    onClickShowNews.invoke(!isShowNews)
                                    onDismissDropMenu.invoke()
                                },
                                icon = R.raw.ic_news,
                                text = TextApp.formatShowNews(name, isShowNews)
                            )
                            ContentInfoUserDropMenuItem(
                                onClick = {
                                    onClickBlockUser.invoke()
                                    onDismissDropMenu.invoke()
                                },
                                icon = R.raw.ic_block,
                                text = TextApp.textBlockUser
                            )
                            ContentInfoUserDropMenuItem(
                                onClick = {
                                    onClickComplain.invoke()
                                    onDismissDropMenu.invoke()
                                },
                                icon = R.raw.ic_info,
                                text = TextApp.textComplain
                            )
                        }
                    }
                    BoxSpacer()
                }
            }
        }

        if (listMembers.isNotEmpty()) {
            item {
                BoxSpacer(.5f)
                ContentInfoUserRowWithDroid(
                    members = listMembers,
                    onClickMember = onClickInfoUser,
                    onClickAll = onClickDroidUsers

                )
            }
        }

        item {
            BoxSpacer(.5f)
            ContentInfoUserRibbonPager(
                onClickChooseMenu = onClickChooseMenu,
                menuRibbon = menuRibbon,
                listMedia = listMedia,
                onClickAllMedia = onClickAllMedia,
                onClickMedia = onClickMedia,
                listWishes = listWishes,
                onClickViewAllWishes = onClickWishList,
                onClickWish = onClickWish,
            )
            BoxSpacer(.3f)
        }

        items(
            items = listPosts,
            key = { item -> item.id }) { item ->
            ItemsPost(
                avatar = item.user.avatar,
                name = item.user.getNameAndLastName(),
                lastVisited = item.user.lastVisitHuman ?: "",
                countLikes = item.votesCount,
                countComments = item.commentsCount,
                imageList = item.attachments,
                description = item.text ?: "",
                isLike = item.isVote ?: false,
                onClickLike = { onClickPostLike.invoke(item.id, it) },
                onClickLink = { },
                onClickUser = { },
                onRemake = { },
                onDelete = { },
                onClickComment = { onClickComment.invoke(item.id) },
                onClickShare = { },
                onClickImage = onClickImage
            )
        }
    }
}
