package ru.droid.tech.screens.module_main.wish_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoadSizeWidth
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.FloatingActionButtonApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.network.NetworkModelWish

class WishList(
    private val wishListIdRote: Int?,
    private val wishListTitleRote: String?
) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<WishListModel>()
        val listWishes by model.listWish.collectAsState()

        BackPressHandler(onBackPressed = model::goToBackStackAndClearLists)
        LifeScreen(onStart = {
            model.getWishList(wishListIdRote)
        })

        ListWishScr(
            listWishes = listWishes,
            onClickAddFloat = {
                model.goToNewWish()
            },
            onClickBack = model::goToBackStackAndClearLists,
            title = wishListTitleRote ?: "",
            onClickWish = model::goToDetailWish,
        )
    }
}

@Composable
private fun ListWishScr(
    listWishes: List<WishUI>,
    onClickBack: () -> Unit,
    title: String,
    onClickAddFloat: () -> Unit,
    onClickWish: (WishUI) -> Unit,
) {

    val greedColumn = 2
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopListWish(
            onClickBack = onClickBack,
            title = title,
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DimApp.screenPadding),
                columns = GridCells.Fixed(greedColumn),
                verticalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(
                    DimApp.screenPadding,
                    Alignment.CenterHorizontally
                ),
                content = {
                    itemsIndexed(
                        items = listWishes.take(4),
                        key = { _, it -> it.id },
                    ) { _, wish ->
                        Column(
                            modifier = Modifier
                                .clip(ThemeApp.shape.smallAll)
                                .clickableRipple { onClickWish.invoke(wish) },
                        ) {
                            BoxImageLoadSizeWidth(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(ThemeApp.shape.smallAll),
                                sizeWidth = 0.4f,
                                image = wish.cover,
                            )

                            TextCaption(
                                text = wish.title ?: ""
                            )

                            TextBodyMedium(
                                text = TextApp.formatRub(wish.price?.div(TextApp.divForeRub) ?: 0),
                                color = ThemeApp.colors.textLight
                            )
                            BoxSpacer()
                        }
                    }
                })


            FloatingActionButtonApp(
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.BottomEnd)
                    .padding(DimApp.screenPadding),
                onClick = onClickAddFloat
            ) {
                IconApp(
                    modifier = Modifier.rotate(45f),
                    painter = rememberImageRaw(id = R.raw.ic_close)
                )
            }
        }
    }
}

@Composable
private fun PanelTopListWish(
    onClickBack: () -> Unit,
    title: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButtonApp(
            modifier = Modifier.padding(start = DimApp.screenPadding * 0.5f),
            onClick = onClickBack
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_arrow_back))
        }
        TextTitleMedium(
            modifier = Modifier
                .padding(start = DimApp.screenPadding)
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Start,
            text = title
        )
    }

}