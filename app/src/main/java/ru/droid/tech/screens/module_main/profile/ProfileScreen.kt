package ru.droid.tech.screens.module_main.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogContentDetailedInformation
import ru.droid.tech.base.common_composable.DialogGetImageList
import ru.droid.tech.base.common_composable.DialogZoomImagePager
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.ItemsPost
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextLabel
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.LocalFixedInsets
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.local.screens.ScreenStatusShortContent
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.formatTimeElapsed

class ProfileScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileModel>()
        val userData by model.userData.collectAsState()
        val listDroid by model.listDroid.collectAsState()
        val chooserDroid by model.chooserDroid.collectAsState()
        val menuRibbon by model.menuProfile.collectAsState()
        val listMedia by model.listMedia.collectAsState()
        val listWishes by model.listWishes.collectAsState()
        val listPosts by model.listPosts.collectAsState()
        val screenStatus by model.screenStatus.collectAsState()

        var getImage by rememberState { false }
        var dialogPagerImage by rememberState<Pair<List<AttachmentUI>, Int>?> { null }
        var focusDetails by rememberState { false }
        val focusManager = LocalFocusManager.current

        BackPressHandler(onBackPressed = model::goBackStack)
        ProfileScr(
            onClickBack = model::goBackStack,
            onClickChangeDroid = model::changeDroid,
            onClickSettings = {
                gDMessage("Stub")
                /**TODO("onClickSettings")*/
            },
            onClickDescriptions = { focusDetails = true },
            onClickRedactionProfile = model::goToRedaction,
            onClickMember = model::goToInfoUser,
            onClickAllDroid = model::goToDroid,
            onClickAllMedia = model::goToAllMedia,
            onClickUploadAPhoto = { getImage = true },
            listDroid = listDroid,
            members = chooserDroid?.members ?: listOf(),
            listMedia = listMedia,
            listPosts = listPosts,
            listWishes = listWishes,
            chooserDroid = chooserDroid,
            avatar = userData.avatar,
            name = userData.getNameAndLastName(),
            locationCity = userData.location?.name,
            screenStatus = screenStatus,
            onClickAddWish = model::goToNewWish,
            onClickViewAllWishes = model::goToWishes,
            onClickChooseMenu = model::chooseMenu,
            menuRibbon = menuRibbon,
            onClickOneMedia = { model.goToViewScreen(it.id) },
            onClickLike = { model.likePost(it) },
            onClickNewPost = model::goToNewPost,
            onClickLink = {
                gDMessage("Stub")
                /**TODO("onClickLink")*/
            },
            onClickUser = {
                gDMessage("Stub")
                /**TODO("onClickUser")*/
            },
            onClickComment = model::goToPostWithComment,
            onClickShare = {
                gDMessage("Stub")
                /**TODO("onClickShare")*/
            },
            onClickRemake = {
                gDMessage("Stub")
                /**TODO("onClickRemake")*/
            },
            onClickWish = model::goToWish,
            onClickDelete = {
                gDMessage("Stub")
                /**TODO("onClickDelete")*/
            },
            onClickImage = { list, index ->
                dialogPagerImage = Pair(list, index)
            },
        )
        if (getImage) {
            DialogGetImageList(
                onDismiss = { getImage = false },
                getPhoto = {
                    if (it.isNotEmpty()) {
                        model.uploadPhoto(it)
                        getImage = false
                    }
                    getImage = false
                }
            )
        }
        if (focusDetails) {
            DialogBottomSheet(
                onDismiss = {
                    focusManager.clearFocus()
                    focusDetails = false
                }) {

                DialogContentDetailedInformation(
                    DroidName = chooserDroid?.getNameDroid(),
                    numDroid = chooserDroid?.members?.size,
                    birthday = userData.birthdateHuman,
                    city = userData.location?.name,
                    cityOrigin = userData.birthLocation?.name,
                    phone = userData.tel,
                    tg = userData.tg,
                    aboutYou = userData.description,
                    doing = userData.work,
                    interests = userData.interests.joinToString(separator = ", "),
                    likeMusic = userData.favoriteMusic,
                    films = userData.favoriteMovies,
                    books = userData.favoriteBooks,
                    games = userData.favoriteGames,
                )
            }
        }

        dialogPagerImage?.let { attach ->
            DialogZoomImagePager(
                images = attach.first.map { it.url },
                dismiss = { dialogPagerImage = null },
                initPageNumb = attach.second,
                offset = IntOffset(0, LocalFixedInsets.current.statusBar)
            )
        }
    }
}

@Composable
private fun ProfileScr(
    onClickBack: () -> Unit,
    onClickChangeDroid: (DroidUI) -> Unit,
    onClickSettings: () -> Unit,
    onClickDescriptions: () -> Unit,
    onClickRedactionProfile: () -> Unit,
    onClickMember: (userId: Int?) -> Unit,
    onClickAllDroid: () -> Unit,
    onClickAllMedia: () -> Unit,
    onClickUploadAPhoto: () -> Unit,
    listDroid: List<DroidUI>,
    members: List<DroidMemberUI>,
    listMedia: List<MediaUI>,
    listPosts: List<PostWithCommentUI>,
    listWishes: List<WishUI>,
    chooserDroid: DroidUI?,
    avatar: Any?,
    screenStatus: ScreenStatusShortContent,
    name: String,
    locationCity: String?,
    onClickAddWish: () -> Unit,
    onClickViewAllWishes: () -> Unit,
    onClickChooseMenu: (MenuProfile) -> Unit,
    menuRibbon: MenuProfile,
    onClickOneMedia: (MediaUI) -> Unit,
    onClickWish: (WishUI) -> Unit,
    onClickLike: (PostWithCommentUI) -> Unit,
    onClickNewPost: () -> Unit,
    onClickLink: (PostWithCommentUI) -> Unit,
    onClickUser: (PostWithCommentUI) -> Unit,
    onClickComment: (PostWithCommentUI) -> Unit,
    onClickShare: (PostWithCommentUI) -> Unit,
    onClickRemake: (PostWithCommentUI) -> Unit,
    onClickDelete: (PostWithCommentUI) -> Unit,
    onClickImage: (List<AttachmentUI>, Int) -> Unit,
) {

    var expandedDroid by rememberState { false }
    val isVisibilityDescriptions by rememberState { true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(
            onClickBack = onClickBack,
            container = ThemeApp.colors.background,
            content = {
                if (screenStatus != ScreenStatusShortContent.Load) {
                    DropMenuInDropMenuDroid(
                        content = {
                            IconButtonApp(
                                modifier = Modifier,
                                onClick = { expandedDroid = !expandedDroid }
                            ) {
                                IconApp(painter = rememberImageRaw(R.raw.ic_dashboard))
                            }
                        },
                        expanded = expandedDroid,
                        chooserDroid = chooserDroid,
                        onDismiss = { expandedDroid = false },
                        listDroid = listDroid,
                        onChooseDroid = onClickChangeDroid
                    )
                    IconButtonApp(
                        modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                        onClick = onClickSettings
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_settings))
                    }
                }
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    ContentCardWithAvatar(
                        avatar = avatar,
                        isVisibilityDescriptions = isVisibilityDescriptions,
                        name = name,
                        locationCity = locationCity,
                        onClickDescriptions = onClickDescriptions,
                        onClickRedactionProfile = onClickRedactionProfile
                    )
                }

                if (members.isNotEmpty()) {
                    item {
                        BoxSpacer(.5f)
                        RowWithDroid(
                            members = members,
                            onClickMember = onClickMember,
                            onClickAll = onClickAllDroid

                        )
                    }
                }
                item {
                    BoxSpacer(.5f)
                    RibbonPager(
                        onClickChooseMenu = onClickChooseMenu,
                        menuRibbon = menuRibbon,
                        listMedia = listMedia,
                        onClickAllMedia = onClickAllMedia,
                        onClickUploadAPhoto = onClickUploadAPhoto,
                        onClickOneMedia = onClickOneMedia,
                        listWishes = listWishes,
                        onClickAddWish = onClickAddWish,
                        onClickViewAllWishes = onClickViewAllWishes,
                        onClickWish = onClickWish,
                    )
                    BoxSpacer(.3f)
                }

                item {

                    Row(
                        modifier = Modifier
                            .padding(vertical = DimApp.shadowElevation)
                            .fillMaxWidth()
                            .shadow(
                                elevation = DimApp.shadowElevation,
                                shape = ThemeApp.shape.mediumAll
                            )
                            .background(color = ThemeApp.colors.backgroundVariant)
                    ) {
                        ButtonWeakApp(
                            modifier = Modifier
                                .padding(horizontal = DimApp.screenPadding)
                                .padding(vertical = DimApp.shadowElevation)
                                .fillMaxWidth(),
                            onClick = onClickNewPost,
                            text = TextApp.textCreateAPost,
                            contentStart = {
                                IconApp(painter = rememberImageRaw(R.raw.ic_edit))
                                BoxSpacer()
                            })
                    }
                }

                items(
                    items = listPosts,
                    key = { item -> item.id }) { item ->
                    ItemsPost(
                        avatar = item.user.avatar,
                        name = item.user.getNameAndLastName(),
                        lastVisited = item.user.lastVisit?.formatTimeElapsed() ?: "",
                        countLikes = item.votesCount,
                        countComments = item.commentsCount,
                        imageList = item.attachments,
                        description = item.text ?: "",
                        isLike = item.isVote ?: false,
                        onClickLike = { onClickLike.invoke(item) },
                        onClickLink = { onClickLink.invoke(item) },
                        onClickUser = { onClickUser.invoke(item) },
                        onRemake = { onClickRemake.invoke(item) },
                        onDelete = { onClickDelete.invoke(item) },
                        onClickComment = { onClickComment.invoke(item) },
                        onClickShare = { onClickShare.invoke(item) },
                        onClickImage = onClickImage
                    )
                }
            }
            if (screenStatus == ScreenStatusShortContent.Load) LoadScreen()
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RibbonPager(
    onClickChooseMenu: (MenuProfile) -> Unit,
    menuRibbon: MenuProfile,
    listMedia: List<MediaUI>,
    listWishes: List<WishUI>,
    onClickAllMedia: () -> Unit,
    onClickAddWish: () -> Unit,
    onClickViewAllWishes: () -> Unit,
    onClickWish: (WishUI) -> Unit,
    onClickUploadAPhoto: () -> Unit,
    onClickOneMedia: (MediaUI) -> Unit
) {

    val menuList by rememberState { MenuProfile.values() }
    val pagerState: PagerState = rememberPagerState() { menuList.size }
    val scope = rememberCoroutineScope()

    var offsetTargetDot by rememberState { 0.dp }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")
    val des = LocalDensity.current

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        menuList.getOrNull(pagerState.currentPage)?.let {
            onClickChooseMenu.invoke(it)
        }
    })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(bottom = DimApp.screenPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                menuList.forEachIndexed { index, item ->
                    TextButtonApp(
                        modifier = Modifier.onGloballyPositioned {
                            if (menuRibbon == item) {
                                offsetTargetDot =
                                    with(des) { it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp() }
                            }
                        },
                        contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                        colors = colorsButtonAccentTextApp().copy(
                            contentColor = if (menuRibbon == item) {
                                ThemeApp.colors.primary
                            } else {
                                ThemeApp.colors.textDark
                            }
                        ),
                        onClick = {
                            onClickChooseMenu.invoke(item)
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = item.getTextMenu()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = offsetDot - (DimApp.menuItemsWidth * .5f))
                    .width(DimApp.menuItemsWidth)
                    .height(DimApp.menuItemsHeight)
                    .clip(ThemeApp.shape.smallTop)
                    .background(ThemeApp.colors.primary)
            )
            FillLineHorizontal(modifier = Modifier.fillMaxWidth())
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
                verticalAlignment = Alignment.Top,
                state = pagerState
            ) { page ->

                val menuItem = menuList.getOrNull(page) ?: return@HorizontalPager
                when (menuItem) {
                    MenuProfile.MEDIA -> ContentMediaMenu(
                        listMedia = listMedia,
                        onClickAllMedia = onClickAllMedia,
                        onClickUploadAPhoto = onClickUploadAPhoto,
                        onClickOneMedia = onClickOneMedia
                    )

                    MenuProfile.WISHLIST -> ContentProfileWishList(
                        listWishes = listWishes,
                        onClickAddWish = onClickAddWish,
                        onClickViewAllWishes = onClickViewAllWishes,
                        onClickWish = onClickWish
                    )

                    MenuProfile.AFFAIRS -> {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .background(ThemeApp.colors.primary.copy(.1f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextBodyLarge(text = menuItem.getTextMenu())
                        }
                    }

                    MenuProfile.AWARDS -> {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .background(ThemeApp.colors.primary.copy(.1f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextBodyLarge(text = menuItem.getTextMenu())
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ContentMediaMenu(
    listMedia: List<MediaUI>,
    onClickAllMedia: () -> Unit,
    onClickUploadAPhoto: () -> Unit,
    onClickOneMedia: (MediaUI) -> Unit,
) {

    val configuration = LocalConfiguration.current
    val widthImage by rememberState { (configuration.screenWidthDp * 0.3f).dp.coerceAtMost(DimApp.sizeImageInPager) }

    val listMediaRemember by rememberState(listMedia) { listMedia.chunked(3) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = DimApp.screenPadding)
            .padding(horizontal = DimApp.screenPadding),
    ) {
        listMediaRemember.take(2).forEach { listTree ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listTree.forEach {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(widthImage)
                            .clip(ThemeApp.shape.smallAll)
                            .clickableRipple { onClickOneMedia.invoke(it) },
                        image = it.url,
                    )
                }
            }
            BoxSpacer(.3f)
        }
        BoxSpacer(.7f)
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickAllMedia,
                text = TextApp.textViewAll
            )
            BoxSpacer()
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickUploadAPhoto,
                text = TextApp.textUploadAPhoto
            )
        }
    }
}

@Composable
private fun DropMenuInDropMenuDroid(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onChooseDroid: (DroidUI) -> Unit,
    listDroid: List<DroidUI>,
    chooserDroid: DroidUI?,
    content: @Composable BoxScope.() -> Unit,
) {
    var dropDownWidth by rememberState { 0.dp }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant)
                .padding(horizontal = DimApp.screenPadding),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            var chooserDroidChild by rememberState { chooserDroid }
            var dropMenuExpanded by rememberState { false }
            var dropDownOffset by rememberState { DpOffset(0.dp, 0.dp) }
            val des = LocalDensity.current
            Column(modifier = Modifier) {

                TextTitleSmall(text = TextApp.titleChooseDroid)

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = DimApp.screenPadding)
                            .onGloballyPositioned { layout ->

                                val x = with(des) { layout.positionInWindow().x.toDp() }
                                val y = with(des) { layout.positionInWindow().y.toDp() }
                                val width = with(des) { layout.size.width.toDp() }
                                val height = with(des) { layout.size.height.toDp() }

                                dropDownOffset = DpOffset(x = x, y = y + height)
                                dropDownWidth = width

                            }
                            .clip(ThemeApp.shape.smallAll)
                            .widthIn(min = DimApp.widthTextViewSize)
                            .background(ThemeApp.colors.container)
                            .clickableRipple { dropMenuExpanded = !dropMenuExpanded }
                            .padding(start = DimApp.screenPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        TextBodyLarge(
                            maxLines = 1,
                            text = "${chooserDroidChild?.name ?: ""}#${chooserDroidChild?.id ?: ""}"
                        )

                        BoxFillWeight()

                        IconButtonApp(
                            modifier = Modifier,
                            onClick = { dropMenuExpanded = !dropMenuExpanded }
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_drop))
                        }

                    }

                    DropdownMenu(
                        modifier = Modifier
                            .width(dropDownWidth)
                            .background(color = ThemeApp.colors.backgroundVariant)
                            .padding(horizontal = DimApp.screenPadding),
                        offset = dropDownOffset,
                        expanded = dropMenuExpanded,
                        onDismissRequest = {
                            dropMenuExpanded = false
                        }
                    ) {

                        listDroid.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    chooserDroidChild = item
                                    dropMenuExpanded = false
                                },
                                text = {
                                    Column() {
                                        TextBodyLarge(text = "${item.name ?: ""}#${item.id ?: ""}")
                                        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                                    }
                                })
                        }
                    }
                }

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButtonApp(
                        colors = colorsButtonAccentTextApp().copy(contentColor = ThemeApp.colors.textDark),
                        onClick = onDismiss,
                        text = TextApp.titleCancel
                    )
                    TextButtonApp(
                        onClick = {
                            chooserDroidChild?.let {
                                onChooseDroid.invoke(it)
                                onDismiss.invoke()
                            }
                        },
                        text = TextApp.titleOk
                    )
                }
            }
        }
    }
}

@Composable
private fun RowWithDroid(
    members: List<DroidMemberUI>,
    onClickMember: (userId: Int?) -> Unit,
    onClickAll: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(horizontal = DimApp.screenPadding)
    ) {

        Row(
            modifier = Modifier
                .height(DimApp.heightButtonInLine)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextBodyLarge(
                text = TextApp.holderDroid
            )
            BoxFillWeight()
            if (members.size > 4) {
                TextButtonApp(
                    onClick = onClickAll,
                    text = TextApp.holderShowAll
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (members.size > 3) DimApp.screenPadding else 0.dp),
            horizontalArrangement = if (members.size > 3) Arrangement.SpaceBetween
            else Arrangement.spacedBy(DimApp.screenPadding * 2f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            members.take(4).forEach { item ->
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickableRipple(
                            bounded = false,
                            radius = DimApp.iconSizeTouchStandard
                        ) {
                            onClickMember.invoke(item.user?.id)
                        },

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(DimApp.iconSizeTouchStandard)
                            .clip(CircleShape),
                        drawableError = R.drawable.stab_avatar,
                        drawablePlaceholder = R.drawable.stab_avatar,
                        image = item.user?.avatar
                    )
                    TextLabel(text = item.firstName ?: "")
                }

            }
        }
        BoxSpacer()
    }
}


@Composable
private fun LoadScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background.copy(.5f))
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(20))
                .background(ThemeApp.colors.background)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(DimApp.screenPadding),
                color = ThemeApp.colors.primary
            )
        }
    }
}
