package ru.droid.tech.screens.module_main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.common_composable.BoxImageLoadSizeWidth
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.network.NetworkModelWish


@Composable
fun ContentProfileWishList(
    listWishes: List<WishUI>,
    onClickAddWish: () -> Unit,
    onClickWish: (WishUI) -> Unit,
    onClickViewAllWishes: () -> Unit,
) {

    val greedColumn = 2
    val ratioModifier: Modifier = remember(listWishes){
        when{
            listWishes.isNullOrEmpty() -> Modifier.height(0.dp)
            listWishes.size <=2 -> Modifier.aspectRatio(.4f)
            listWishes.size >2 -> Modifier.aspectRatio(.8f)
            else                 -> Modifier.height(0.dp)
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = DimApp.screenPadding),
    ) {
        BoxSpacer(.5f)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .then(ratioModifier),
            columns = GridCells.Fixed(greedColumn),
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

        BoxSpacer(.5f)
        Row {
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickAddWish,
                text = TextApp.titleAdd
            )
            BoxSpacer(.5f)
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickViewAllWishes,
                text = TextApp.holderViewAll
            )
        }
    }
}