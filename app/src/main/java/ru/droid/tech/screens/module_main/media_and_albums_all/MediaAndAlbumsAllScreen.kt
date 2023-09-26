package ru.droid.tech.screens.module_main.media_and_albums_all

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogChoseAlbumItem
import ru.droid.tech.base.common_composable.DialogGetImageList
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.FloatingActionButtonApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PagerApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.AlbumUI
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.screens.ScreenStatusShortContent
import ru.data.common.models.local.screens.MenuMediaRibbon
import ru.data.common.models.res.TextApp


class MediaAndAlbumsAllScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaAndAlbumsAllModel>()

        val menuMediaRibbon by model.menuMediaRibbon.collectAsState()

        val listMyMedia by model.listMyMedia.collectAsState()
        val listMyAlbums by model.listMyAlbums.collectAsState()

        val listAllMedia by model.listAllMedia.collectAsState()
        val listAllAlbums by model.listAllAlbums.collectAsState()

        val listDroidMedia by model.listDroidMedia.collectAsState()
        val listDroidAlbums by model.listDroidAlbums.collectAsState()

        val listFavoritesMedia by model.listFavoritesMedia.collectAsState()
        val listFavoritesAlbums by model.listFavoritesAlbums.collectAsState()

        val screenStatus by model.screenStatus.collectAsState()
        var getImage by rememberState { false }

        var focusDetails by rememberState { false }
        val focusManager = LocalFocusManager.current

        LifeScreen(onResume = { model.onStartedInitFilterMedia() })

        MediaScr(
            onClickBack = model::goBackStack,
            menuMediaRibbon = menuMediaRibbon,
            onClickChooseMenu = model::chooseMenu,
            listMyMedia = listMyMedia,
            listMyAlbums = listMyAlbums,
            listAllMedia = listAllMedia,
            listAllAlbums = listAllAlbums,
            listDroidMedia = listDroidMedia,
            listDroidAlbums = listDroidAlbums,
            listFavoritesMedia = listFavoritesMedia,
            listFavoritesAlbums = listFavoritesAlbums,
            screenStatus = screenStatus,
            onClickShowAll = model::goToAllAlbum,
            onClickAlbum = model::goToAlbum,
            onClickFloating = { focusDetails = true },
            onClickMedia = model::goToViewScreen,
        )

        if (getImage) {
            DialogGetImageList(
                onDismiss = { getImage = false },
                getPhoto = {
                    model.uploadPhoto(it, null)
                    getImage = false
                }
            )
        }
        if (focusDetails) {
            DialogBottomSheet(
                onDismiss = {
                    focusManager.clearFocus()
                    focusDetails = false
                },
                backgroundContent = ThemeApp.colors.background
            ) {
                DialogChoseAlbumItem(
                    onClickAlbum = model::goToAlbum,
                    onClickAddAlbum = model::goToCreateNewAlbum,
                    listAlbum = listAllAlbums,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaScr(
    onClickBack: () -> Unit,
    onClickShowAll: () -> Unit,
    onClickMedia: (MediaUI) -> Unit,
    onClickAlbum: (AlbumUI) -> Unit,

    listMyMedia: List<MediaUI>,
    listMyAlbums: List<AlbumUI>,

    listAllMedia: List<MediaUI>,
    listAllAlbums: List<AlbumUI>,

    listDroidMedia: List<MediaUI>,
    listDroidAlbums: List<AlbumUI>,

    listFavoritesMedia: List<MediaUI>,
    listFavoritesAlbums: List<AlbumUI>,

    onClickChooseMenu: (MenuMediaRibbon) -> Unit,
    onClickFloating: () -> Unit,
    menuMediaRibbon: MenuMediaRibbon,
    screenStatus: ScreenStatusShortContent,
) {
    val itemsMenu = remember { MenuMediaRibbon.values().toList() }
    val pagerState: PagerState = rememberPagerState() { itemsMenu.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        itemsMenu.getOrNull(pagerState.currentPage)?.let(onClickChooseMenu)
    })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .systemBarsPadding()
        ) {
            TopPanel(
                onClickBack = onClickBack,
                menuMediaRibbon = menuMediaRibbon,
                onClickChooseMenu = {
                    scope.launch {
                        val pageIndex = itemsMenu.indexOfFirst { menu -> menu == it }
                        pagerState.scrollToPage(pageIndex)
                    }
                }
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {

                PagerApp(
                    items = itemsMenu,
                    pagerState = pagerState,
                    modifier = Modifier.fillMaxSize()) { menuChoose ->
                    when (menuChoose) {
                        MenuMediaRibbon.MY -> ListContent(
                            onClickShowAll = onClickShowAll,
                            onClickMedia = onClickMedia,
                            onClickAlbum = onClickAlbum,
                            listAlbums = listMyAlbums,
                            listMedias = listMyMedia
                        )

                        MenuMediaRibbon.ALL -> ListContent(
                            onClickShowAll = onClickShowAll,
                            onClickMedia = onClickMedia,
                            onClickAlbum = onClickAlbum,
                            listAlbums = listAllAlbums,
                            listMedias = listAllMedia
                        )

                        MenuMediaRibbon.Droid -> ListContent(
                            onClickShowAll = onClickShowAll,
                            onClickMedia = onClickMedia,
                            onClickAlbum = onClickAlbum,
                            listAlbums = listDroidAlbums,
                            listMedias = listDroidMedia
                        )

                        MenuMediaRibbon.FAVORITES -> ListContent(
                            onClickShowAll = onClickShowAll,
                            onClickMedia = onClickMedia,
                            onClickAlbum = onClickAlbum,
                            listAlbums = listFavoritesAlbums,
                            listMedias = listFavoritesMedia
                        )
                    }
                }
                if (screenStatus == ScreenStatusShortContent.Load) LoadScreen()
            }
        }

        FloatingActionButtonApp(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(DimApp.screenPadding),
            onClick = onClickFloating
        ) {
            IconApp(
                modifier = Modifier.rotate(45f),
                painter = rememberImageRaw(id = R.raw.ic_close)
            )
        }
    }
}

@Composable
private fun ListContent(
    onClickShowAll: () -> Unit,
    onClickMedia: (MediaUI) -> Unit,
    onClickAlbum: (AlbumUI) -> Unit,
    listAlbums: List<AlbumUI>,
    listMedias: List<MediaUI>,
    greedColumn: Int = 3,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(greedColumn),
        horizontalArrangement = Arrangement.spacedBy(
            0.dp,
            Alignment.CenterHorizontally
        ),
        content = {
            if (listAlbums.isNotEmpty()) {
                item(span = { GridItemSpan(greedColumn) }) {
                    Column {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = DimApp.screenPadding)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextBodyLarge(text = TextApp.textAlbums)
                            TextButtonApp(onClick = onClickShowAll, text = TextApp.holderShowAllS)
                        }
                        LazyRow(content = {
                            items(
                                items = listAlbums,
                                key = { it.id }) { item ->
                                ItemAlbum(
                                    onClickAlbum = { onClickAlbum.invoke(item) },
                                    cover = item.cover ?: "",
                                    title = item.name ?: "",
                                    created = item.createdHuman
                                )
                            }
                        })
                    }
                }
            }

            if (listMedias.isNotEmpty()) {
                item(span = { GridItemSpan(greedColumn) }) {
                    Column() {
                        BoxSpacer(.5f)
                        TextBodyLarge(
                            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                            text = TextApp.textAllFiles
                        )
                    }
                }
            }
            itemsIndexed(
                items = listMedias,
                key = { _, it -> it.id },
                contentType = { _, it -> it.isVideo }
            ) { index, item ->

                val padding = when (index % greedColumn) {
                    0 -> PaddingValues(start = DimApp.screenPadding)
                    2 -> PaddingValues(end = DimApp.screenPadding)
                    else -> PaddingValues(horizontal = DimApp.screenPadding * .5f)
                }
                when (item.isVideo) {
                    true -> {}
                    false -> {
                        Column() {
                            BoxSpacer(.5f)
                            BoxImageLoad(
                                modifier = Modifier
                                    .padding(padding)
                                    .aspectRatio(1f)
                                    .clip(ThemeApp.shape.smallAll)
                                    .background(Color.Transparent.copy(.05f))
                                    .clickableRipple { onClickMedia.invoke(item) },
                                drawableError = R.drawable.stub_photo_v2,
                                drawablePlaceholder = R.drawable.stub_photo_v2,
                                image = item.url,
                            )
                        }
                    }
                }
            }
        })
}

@Composable
private fun LoadScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background.copy(.5f))
            .pointerInput(Unit){},
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

@Composable
private fun TopPanel(
    onClickBack: () -> Unit,
    menuMediaRibbon: MenuMediaRibbon,
    onClickChooseMenu: (MenuMediaRibbon) -> Unit,
) {
    val menuList by rememberState { MenuMediaRibbon.values() }

    var offsetTargetDot by rememberState { 0.dp }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")
    val des = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant)
    ) {
        PanelNavBackTop(
            modifier = Modifier,
            onClickBack = onClickBack,
            text = TextApp.titleMedia
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            menuList.forEachIndexed { index, item ->
                TextButtonApp(
                    contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                    modifier = Modifier.onGloballyPositioned {
                        if (menuMediaRibbon == item) {
                            offsetTargetDot =
                                with(des) { it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp() }
                        }
                    },
                    colors = colorsButtonAccentTextApp().copy(
                        contentColor = if (menuMediaRibbon == item) {
                            ThemeApp.colors.primary
                        } else {
                            ThemeApp.colors.textDark
                        }
                    ),
                    onClick = {
                        onClickChooseMenu.invoke(item)
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
    }
}

@Composable
private fun ItemAlbum(
    onClickAlbum: () -> Unit,
    cover: Any,
    title: String,
    created: String,
) {
    Column(modifier = Modifier
        .padding(
            start = DimApp.screenPadding,
            bottom = DimApp.screenPadding
        )
        .size(
            width = DimApp.sizeWidthCardAlbum,
            height = DimApp.sizeHeightCardAlbum
        )
        .shadow(
            elevation = DimApp.shadowElevation,
            shape = ThemeApp.shape.mediumAll
        )
        .background(ThemeApp.colors.backgroundVariant)
        .clickableRipple { onClickAlbum.invoke() }) {

        BoxImageLoad(
            image = cover,
            modifier = Modifier
                .padding(DimApp.screenPadding * .5f)
                .fillMaxWidth()
                .weight(1f)
                .clip(ThemeApp.shape.smallAll)
                .clipToBounds(),
            drawableError = R.drawable.stub_photo,
            contentScale = ContentScale.FillWidth,
        )

        TextTitleSmall(
            modifier = Modifier
                .padding(horizontal = DimApp.screenPadding * .5f),
            text = title,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
        )

        TextCaption(
            modifier = Modifier
                .padding(DimApp.screenPadding * .5f),
            text = created
        )
    }
}


