package ru.droid.tech.screens.module_main.info_user

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.screens.MenuProfile
import ru.data.common.models.res.TextApp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentInfoUserRibbonPager(
    onClickChooseMenu: (MenuProfile) -> Unit,
    menuRibbon: MenuProfile,
    listMedia: List<MediaUI>,
    listWishes: List<WishUI>,
    onClickAllMedia: () -> Unit,
    onClickViewAllWishes: () -> Unit,
    onClickWish: (id: Int) -> Unit,
    onClickMedia: (media: MediaUI) -> Unit
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
                    MenuProfile.MEDIA    -> ContentMediaMenu(
                        listMedia = listMedia,
                        onClickAllMedia = onClickAllMedia,
                        onClickMedia = onClickMedia
                    )

                    MenuProfile.WISHLIST -> {
                        ContentInfoUserWishList(
                            listWishes = listWishes,
                            onClickViewAllWishes = onClickViewAllWishes,
                            onClickWish = onClickWish
                        )
                    }

                    MenuProfile.AFFAIRS  -> {
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

                    MenuProfile.AWARDS   -> {
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
    onClickMedia: (media: MediaUI) -> Unit,
) {

    val configuration = LocalConfiguration.current
    val widthImage by rememberState { (configuration.screenWidthDp * 0.3f).dp.coerceAtMost(DimApp.sizeImageInPager) }

    val listMediaRemember by rememberState(listMedia) { listMedia.chunked(3) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                            .clickableRipple { onClickMedia.invoke(it) },
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
        }
    }
}

