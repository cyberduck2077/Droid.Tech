package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import ru.droid.tech.R
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.data.common.models.local.maps.AlbumUI

@Composable
fun DialogChoseAlbumItem(
    onClickAlbum: (AlbumUI) -> Unit,
    onClickAddAlbum: () -> Unit,
    greedColumn: Int = 2,
    listAlbum: List<AlbumUI>,
) {
    Column(
        modifier = Modifier
            .padding(top = DimApp.heightTopNavigationPanel)
            .fillMaxWidth()
            .background(ThemeApp.colors.background)
    ) {
        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textChooseAlbum
        )
        BoxSpacer()
        TextButtonApp(
            onClick = onClickAddAlbum,
            text = TextApp.titleNewAlbum,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding),
            colors = ColorButtonApp(
                containerColor = ThemeApp.colors.container,
                contentColor = ThemeApp.colors.primary,
                disabledContainerColor = ThemeApp.colors.container,
                disabledContentColor = ThemeApp.colors.textLight,
            ),
            contentStart = {
                IconApp(
                    modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                    painter = rememberImageRaw(id = R.raw.ic_add)
                )
            })
        BoxSpacer()
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(DimApp.screenPadding),
            columns = GridCells.Fixed(greedColumn),
            verticalArrangement = Arrangement.spacedBy(
                DimApp.screenPadding / 2,
            ),
            horizontalArrangement = Arrangement.spacedBy(
                DimApp.screenPadding / 2,
                Alignment.CenterHorizontally
            )
        ) {
            itemsIndexed(
                items = listAlbum,
                key = { _, it -> it.id },
            ) { index, item ->

                Column(modifier = Modifier
                    .shadow(
                        elevation = DimApp.shadowElevation,
                        shape = ThemeApp.shape.mediumAll
                    )
                    .background(ThemeApp.colors.backgroundVariant)
                    .clickableRipple { onClickAlbum.invoke(item) }) {

                    BoxImageLoad(
                        modifier = Modifier
                            .padding(DimApp.screenPadding * .5f)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(ThemeApp.shape.smallAll),
                        drawableError = R.drawable.stub_photo,
                        contentScale = ContentScale.FillWidth,
                        image = item.cover ?: R.drawable.stub_photo,
                    )

                    TextTitleSmall(
                        modifier = Modifier
                            .padding(horizontal = DimApp.screenPadding * .5f),
                        text = item.description ?: "",
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                    )
                    TextCaption(
                        modifier = Modifier
                            .padding(DimApp.screenPadding * .5f),
                        text = item.createdHuman
                    )
                }
            }
        }
    }
    BoxSpacer()
}
