package ru.droid.tech.screens.module_main.main_ribbon

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxImageRowRes
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonAccentTextApp
import ru.droid.tech.base.common_composable.CheckerApp
import ru.droid.tech.base.common_composable.ContentErrorLoad
import ru.droid.tech.base.common_composable.DialogBackPressExit
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogReports
import ru.droid.tech.base.common_composable.DialogYesOrNo
import ru.droid.tech.base.common_composable.DialogZoomImagePager
import ru.droid.tech.base.common_composable.DropMenuRibbonTop
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.FloatingActionButtonApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.ItemsPost
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.addUniqueElements
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberOpenIntentUrl
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.base.util.removeElementsIfPresent
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.local.maps.AttachmentUI
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.FilterPosts
import ru.data.common.models.local.maps.PostWithCommentUI
import ru.data.common.models.local.maps.TypeReason
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.screens.TypeLink
import ru.data.common.models.res.TextApp
import kotlin.math.abs

class Ribbon : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RibbonModel>()
        val userData by model.userData.collectAsState()
        val screenRibbon by model.screenRibbon.collectAsState()
        val filter by model.filter.collectAsState()
        val listDroid by model.listDroid.collectAsState()
        val filterDroid by model.filterDroid.collectAsState()
        val listUserSearch by model.listUserSearch.collectAsState()

        val uriHelper = rememberOpenIntentUrl()
        DialogBackPressExit()

        StepOneStr(
            avatarUser = userData.avatar,
            nameUser = userData.firstName,
            myID = userData.id,
            haveNewNotifications = true,
            screenRibbon = screenRibbon,
            onClickRibbonStage = model::chooseMenu,
            onClickProfile = model::goToProfile,
            filter = filter,
            listDroid = listDroid,
            filterDroid = filterDroid,
            listAllPosts = model.pagerAllRibbon.getFlow(),
            listDroidPosts = model.pagerDroidRibbon.getFlow(),
            onClickNotification = model::goToNotification,
            onClickComment = model::goToPostWithComment,
            setFilterDroid = model::setFilterDroid,
            onClickLike = model::likePost,
            onClickLink = { uriHelper.invoke(it) },
            setFilter = model::setFilter,
            onClickUser = model::goToInfoUser,
            onClickShare = {
                /**TODO()*/
                gDMessage("Stub")
            },
            onSaveToMyFiles = {
                /**TODO()*/
                gDMessage("Stub")
            },
            onDeleteMyPost = model::deleteMyPost,
            onRemakeMyPost = model::goToRemakeMyPost,
            onClickReportsPost = model::reportsPost,
            onClickAddPost = model::goToMyPost,
            listUser = listUserSearch,
            onSearchUser = model::onSearchUser,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StepOneStr(
    avatarUser: String?,
    nameUser: String?,
    myID: Int,
    haveNewNotifications: Boolean,
    filter: FilterPosts,
    listDroid: List<DroidUI>,
    filterDroid: List<DroidUI>,
    listUser: List<UserUI>,
    setFilter: (FilterPosts) -> Unit,
    listAllPosts: LazyPagingItems<PostWithCommentUI>,
    listDroidPosts: LazyPagingItems<PostWithCommentUI>,
    screenRibbon: RibbonStatusScr,
    onClickRibbonStage: (RibbonStatusScr) -> Unit,
    onClickProfile: () -> Unit,
    onClickLike: (PostWithCommentUI) -> Unit,
    onClickLink: (String) -> Unit,
    onClickUser: (idUser: Int) -> Unit,
    onClickNotification: () -> Unit,
    onClickComment: (PostWithCommentUI) -> Unit,
    onClickShare: (PostWithCommentUI) -> Unit,
    onClickReportsPost: (postId: Int, typeReason: TypeReason, text: String) -> Unit,
    onSaveToMyFiles: (PostWithCommentUI) -> Unit,
    onRemakeMyPost: (PostWithCommentUI) -> Unit,
    setFilterDroid: (List<DroidUI>) -> Unit,
    onClickAddPost: () -> Unit,
    onDeleteMyPost: (PostWithCommentUI) -> Unit,
    onSearchUser: (String?) -> Unit,
) {

    val menuList by rememberState { RibbonStatusScr.values() }
    val pagerState: PagerState = rememberPagerState() { menuList.size }
    val scope = rememberCoroutineScope()
    var dialogFilter by rememberState { false }
    var dialogFilterDroid by rememberState { false }
    var dialogDeleteMyPost by rememberState<PostWithCommentUI?> { null }
    var dialogReportsPost by rememberState<PostWithCommentUI?> { null }
    var dialogSharePost by rememberState<PostWithCommentUI?> { null }
    var dialogPagerImage by rememberState<Pair<List<AttachmentUI>, Int>?> { null }

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        menuList.getOrNull(pagerState.currentPage)?.let {
            onClickRibbonStage.invoke(it)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopRibbon(
            onClickProfile = onClickProfile,
            onClickSearch = {},
            onClickNotifications = onClickNotification,
            onClickTypePost = {
                dialogFilter = true
            },
            onClickAllDroid = {
                dialogFilterDroid = true
            },
            text = nameUser,
            avatar = avatarUser,
            haveNewNotifications = haveNewNotifications,
            onClickRibbonStage = {
                onClickRibbonStage.invoke(it)
                scope.launch {
                    pagerState.animateScrollToPage(it.ordinal)
                }
            },
            screenRibbon = screenRibbon,
        )


        HorizontalPager(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(0.dp),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { page ->

            val menuItem = menuList.getOrNull(page) ?: return@HorizontalPager
            when (menuItem) {
                RibbonStatusScr.ALL    -> {
                    ListPosts(
                        list = listAllPosts,
                        youID = myID,
                        onClickLike = onClickLike,
                        onClickLink = onClickLink,
                        onClickUser = onClickUser,
                        onClickComment = onClickComment,
                        onClickShare = { dialogSharePost = it },
                        onClickToComplain = { dialogReportsPost = it },
                        onSaveToMyFiles = onSaveToMyFiles,
                        onRemakeMyPost = onRemakeMyPost,
                        onDeleteMyPost = { dialogDeleteMyPost = it },
                        onClickAddPost = onClickAddPost,
                        onClickImage = { list, index ->
                            dialogPagerImage = Pair(list, index)
                        },
                    )
                }

                RibbonStatusScr.Droid -> {
                    ListPosts(
                        list = listDroidPosts,
                        youID = myID,
                        onClickLike = onClickLike,
                        onClickLink = onClickLink,
                        onClickUser = onClickUser,
                        onClickComment = onClickComment,
                        onClickShare = { dialogSharePost = it },
                        onClickToComplain = { dialogReportsPost = it },
                        onSaveToMyFiles = onSaveToMyFiles,
                        onRemakeMyPost = onRemakeMyPost,
                        onDeleteMyPost = { dialogDeleteMyPost = it },
                        onClickAddPost = onClickAddPost,
                        onClickImage = { list, index ->
                            dialogPagerImage = Pair(list, index)
                        },
                    )
                }
            }
        }
    }

    dialogPagerImage?.let { attach ->
        DialogZoomImagePager(
            images = attach.first.map { it.url },
            dismiss = { dialogPagerImage = null },
            initPageNumb = attach.second,
        )
    }

    if (dialogFilter) {
        DialogFilter(
            onDismiss = { dialogFilter = false },
            filter = filter,
            setFilter = setFilter
        )
    }

    dialogDeleteMyPost?.let { myPost ->
        DialogYesOrNo(
            onDismiss = { dialogDeleteMyPost = null },
            onClick = { onDeleteMyPost.invoke(myPost) },
            title = TextApp.textDeletingPost,
            body = TextApp.formatReallyWantDeletingPost(myPost.text?.take(20))
        )
    }

    if (dialogFilterDroid) {
        DialogFilterDroid(
            onDismiss = { dialogFilterDroid = false },
            listDroid = listDroid,
            filterDroid = filterDroid,
            setFilterDroid = { list ->
                scope.launch {
                    pagerState.animateScrollToPage(RibbonStatusScr.Droid.ordinal)
                    setFilterDroid.invoke(list)
                }
            },
        )
    }

    dialogReportsPost?.let { post ->
        DialogReports(
            onDismiss = { dialogReportsPost = null },
            onClick = { typeReason, s ->
                onClickReportsPost.invoke(post.id, typeReason, s)
            })
    }

    dialogSharePost?.let { post ->
        DialogBottomSheet(onDismiss = { dialogSharePost = null }) {
            DialogSharePost(
                onClickShare = { comment: String,
                                 typeLink: TypeLink,
                                 listUser: List<UserUI> ->
                    //TODO ("DialogSharePost ")
                    onClickShare.invoke(post)
                },
                onDismiss = it::invoke,
                onSearchUser = onSearchUser,
                listUser = listUser
            )

        }

    }
}

@Composable
private fun DialogFilter(
    onDismiss: () -> Unit,
    filter: FilterPosts,
    setFilter: (FilterPosts) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        var checkboxStories by rememberState(filter) { filter.getTextChooseForeUI() }
        var checkboxPhoto by rememberState(filter) { filter.getPhotoChooseForeUI() }
        var checkboxVideo by rememberState(filter) { filter.getVideoChooseForeUI() }
        var checkboxPolls by rememberState(filter) { filter.getPollingChooseForeUI() }
        val checkboxAll = remember(
            checkboxStories,
            checkboxPhoto,
            checkboxVideo,
            checkboxPolls,
        ) {
            checkboxStories && checkboxPhoto && checkboxVideo && checkboxPolls
        }

        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitleSmall(text = TextApp.titlePostType)
                IconButtonApp(
                    modifier = Modifier.offset(x = DimApp.screenPadding),
                    onClick = onDismiss
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_close))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckerApp(checked = checkboxAll, onCheckedChange = {
                    if (!checkboxAll) {
                        checkboxStories = true
                        checkboxPhoto = true
                        checkboxVideo = true
                        checkboxPolls = true
                    } else {
                        checkboxStories = true
                        checkboxPhoto = false
                        checkboxVideo = false
                        checkboxPolls = false
                    }
                })

                BoxSpacer()
                TextBodyMedium(text = TextApp.titleAllPosts)
                BoxSpacer()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckerApp(checked = checkboxStories, onCheckedChange = {
                    checkboxStories = it
                })

                BoxSpacer()
                TextBodyMedium(text = TextApp.titleStories)
                BoxSpacer()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CheckerApp(checked = checkboxPhoto, onCheckedChange = {
                    checkboxPhoto = it
                })

                BoxSpacer()
                TextBodyMedium(text = TextApp.titlePhoto)
                BoxSpacer()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CheckerApp(checked = checkboxVideo, onCheckedChange = {
                    checkboxVideo = it
                })

                BoxSpacer()
                TextBodyMedium(text = TextApp.titleVideo)
                BoxSpacer()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CheckerApp(checked = checkboxPolls, onCheckedChange = {
                    checkboxPolls = it
                })

                BoxSpacer()
                TextBodyMedium(text = TextApp.titlePolls)
                BoxSpacer()
            }

            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)
            ) {
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        setFilter.invoke(
                            FilterPosts(
                                withPolling = checkboxStories,
                                withText = checkboxPhoto,
                                withPhoto = checkboxVideo,
                                withVideo = checkboxPolls,
                            )
                        )
                        onDismiss.invoke()
                    },
                    text = TextApp.holderSave
                )
            }
            BoxSpacer()
        }
    }
}


@Composable
private fun DialogFilterDroid(
    onDismiss: () -> Unit,
    listDroid: List<DroidUI>,
    filterDroid: List<DroidUI>,
    setFilterDroid: (List<DroidUI>) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        var filterDroidDialog by rememberState { filterDroid }
        val isCheckedAll by rememberState(filterDroidDialog) {
            filterDroidDialog.toSet() == listDroid.toSet()
        }
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitleSmall(text = TextApp.titlePostType)
                IconButtonApp(
                    modifier = Modifier.offset(x = DimApp.screenPadding),
                    onClick = onDismiss
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_close))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DimApp.screenPadding * .3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckerApp(
                    checked = isCheckedAll,
                    onCheckedChange = {
                        filterDroidDialog = if (isCheckedAll)
                            listDroid.take(1) else listDroid
                    })
                BoxSpacer()
                TextBodyMedium(text = TextApp.textAllCollectives)
                BoxSpacer()
            }

            listDroid.forEach { Droid ->
                val isChecked by rememberState(filterDroidDialog) {
                    filterDroidDialog.contains(Droid)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DimApp.screenPadding * .3f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CheckerApp(
                        checked = isChecked,
                        onCheckedChange = {
                            val chooseF = listDroid.filter { it.id == Droid.id }
                            filterDroidDialog = if (isChecked) {
                                filterDroidDialog.removeElementsIfPresent(chooseF)
                            } else {
                                filterDroidDialog.addUniqueElements(chooseF)
                            }
                            if (filterDroidDialog.isEmpty()) {
                                filterDroidDialog = listDroid.take(1)
                            }
                        })
                    BoxSpacer()
                    TextBodyMedium(text = Droid.getNameDroid())
                    BoxSpacer()
                }
            }

            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)
            ) {
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        setFilterDroid.invoke(filterDroidDialog)
                        onDismiss.invoke()
                    },
                    text = TextApp.holderSave
                )
            }
            BoxSpacer()
        }
    }
}

@Composable
fun ColumnScope.ListPosts(
    list: LazyPagingItems<PostWithCommentUI>,
    youID: Int,
    onClickLike: (PostWithCommentUI) -> Unit,
    onClickLink: (String) -> Unit,
    onClickUser: (userId: Int) -> Unit,
    onClickToComplain: (PostWithCommentUI) -> Unit,
    onSaveToMyFiles: (PostWithCommentUI) -> Unit,
    onRemakeMyPost: (PostWithCommentUI) -> Unit,
    onDeleteMyPost: (PostWithCommentUI) -> Unit,
    onClickComment: (PostWithCommentUI) -> Unit,
    onClickAddPost: () -> Unit,
    onClickShare: (PostWithCommentUI) -> Unit,
    onClickImage: (List<AttachmentUI>, Int) -> Unit,
) {

    val heightOffset = remember { 100f }
    var dynamicOffsetHeightPx by remember { mutableStateOf(0f) }
    var isFloatButtonView by rememberState(list) { true }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = dynamicOffsetHeightPx + delta
                dynamicOffsetHeightPx = newOffset.coerceIn(-heightOffset, 0f)
                isFloatButtonView = abs(dynamicOffsetHeightPx) / heightOffset < 0.3
                return Offset.Zero
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .weight(1f)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = if (list.itemCount < 1) {
                Arrangement.Center
            } else {
                Arrangement.Top
            }
        ) {

            item() {
                ContentErrorLoad(visible = list.loadState.refresh is LoadState.Error)
            }

            items(
                count = list.itemCount,
                key = list.itemKey { item ->
                    return@itemKey item.id
                },
            ) { index ->
                val item = list[index] ?: return@items
                ItemsPost(
                    avatar = item.user.avatar,
                    name = item.user.getNameAndLastName(),
                    lastVisited = item.user.lastVisitHuman ?: "",
                    countLikes = item.votesCount,
                    countComments = item.commentsCount,
                    imageList = item.attachments,
                    description = item.text ?: "",
                    isLike = item.isVote ?: false,
                    isYourPost = item.user.id == youID,
                    onRemake = { onRemakeMyPost.invoke(item) },
                    onDelete = { onDeleteMyPost.invoke(item) },
                    onClickLike = { onClickLike.invoke(item) },
                    onClickLink = { onClickLink.invoke(it) },
                    onClickUser = { onClickUser.invoke(item.user.id) },
                    onClickComment = { onClickComment.invoke(item) },
                    onClickShare = { onClickShare.invoke(item) },
                    onToComplain = { onClickToComplain.invoke(item) },
                    onSaveToYourFiles = { onSaveToMyFiles.invoke(item) },
                    onClickImage = onClickImage,
                )
            }

            if (list.loadState.refresh is LoadState.Loading) {
                item() {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(DimApp.screenPadding)
                                .wrapContentSize()
                                .shadow(
                                    elevation = DimApp.shadowElevation,
                                    shape = ThemeApp.shape.mediumAll
                                )

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
            }

            if (list.loadState.refresh is LoadState.NotLoading) {
                item() {
                    if (list.itemCount < 1) {
                        Column(modifier = Modifier) {
                            BoxImageRowRes(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(DimApp.stubSize),
                                image = R.raw.ic_news_off,
                            )
                            TextBodyLarge(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = DimApp.screenPadding)
                                    .padding(horizontal = DimApp.screenPadding),
                                textAlign = TextAlign.Center,
                                text = TextApp.textItEmpty,
                                color = ThemeApp.colors.textDark
                            )
                        }
                    }
                }
            }
        }

        androidx.compose.animation.AnimatedVisibility(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = isFloatButtonView
        ) {
            FloatingActionButtonApp(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(DimApp.screenPadding),
                onClick = onClickAddPost
            ) {
                IconApp(
                    modifier = Modifier,
                    painter = rememberImageRaw(id = R.raw.ic_edit)
                )
            }
        }
    }
}

@Composable
private fun PanelTopRibbon(
    onClickRibbonStage: (RibbonStatusScr) -> Unit,
    screenRibbon: RibbonStatusScr,
    onClickProfile: () -> Unit,
    onClickSearch: () -> Unit,
    onClickNotifications: () -> Unit,
    onClickTypePost: () -> Unit,
    onClickAllDroid: () -> Unit,
    text: String?,
    avatar: String?,
    haveNewNotifications: Boolean,
    styleMenuText: TextStyle = ThemeApp.typography.button
) {
    val menuList by rememberState { RibbonStatusScr.values() }
    var expandedSettings by rememberState { false }
    var offsetTargetDot by rememberState { 0.dp }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")
    val des = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DimApp.heightTopNavigationPanel),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            BoxImageLoad(
                modifier = Modifier
                    .padding(DimApp.screenPadding)
                    .size(DimApp.iconSizeBig)
                    .clip(CircleShape)
                    .clickableRipple { onClickProfile.invoke() },
                drawableError = R.drawable.stab_avatar,
                drawablePlaceholder = R.drawable.stab_avatar,
                image = avatar
            )
            TextTitleMedium(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Start,
                text = text ?: ""
            )
            IconButtonApp(
                modifier = Modifier,
                onClick = onClickSearch
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_search))
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = DimApp.screenPadding * 0.5f),
                contentAlignment = Alignment.Center
            ) {
                IconButtonApp(
                    onClick = onClickNotifications
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_notifications))
                }

                if (haveNewNotifications) {
                    Box(
                        modifier = Modifier
                            .padding(DimApp.badgePadding)
                            .size(DimApp.badgeLittle)
                            .align(Alignment.TopEnd)
                            .background(
                                color = ThemeApp.colors.primary,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimApp.screenPadding)
                .padding(end = DimApp.screenPadding / 2),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            menuList.forEachIndexed { index, item ->
                var fontSize by rememberState { 1f }
                val isChoose by rememberState(screenRibbon) {
                    val choose = screenRibbon == item
                    fontSize = if (choose) 1.3f else 1f
                    choose
                }
                val animFontSize = animateFloatAsState(targetValue = fontSize, label = "")


                TextButtonApp(
                    modifier = Modifier.onGloballyPositioned {
                        if (isChoose) {
                            offsetTargetDot =
                                with(des) { it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp() }
                        }
                    },
                    ifCorrectTextSize = false,
                    styleText = styleMenuText.copy(fontSize = styleMenuText.fontSize * animFontSize.value),
                    contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                    colors = colorsButtonAccentTextApp().copy(
                        contentColor = if (screenRibbon == item) {
                            ThemeApp.colors.primary
                        } else {
                            ThemeApp.colors.textDark
                        }
                    ),
                    onClick = {
                        onClickRibbonStage.invoke(item)
                    },
                    text = item.getTextRibbon()
                )
                BoxSpacer()
            }
            BoxFillWeight()
            IconButtonApp(
                modifier = Modifier,
                onClick = {
                    expandedSettings = !expandedSettings
                }
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_setting_post))
            }
            DropMenuRibbonTop(
                expanded = expandedSettings,
                onDismiss = { expandedSettings = !expandedSettings },
                onTypePosts = onClickTypePost,
                onAllDroid = onClickAllDroid
            )

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
    }
}

enum class RibbonStatusScr {
    ALL,
    Droid;

    fun getTextRibbon() = when (this) {
        ALL    -> TextApp.titleAllRibbon
        Droid -> TextApp.titleDroidRibbon
    }
}