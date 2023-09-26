package ru.droid.tech.screens.module_main.media.meta

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.data.common.models.local.DataForPostingMedia
import ru.droid.tech.screens.module_main.media_and_albums_all.MediaAndAlbumsAllModel

class MetaScreen(private val albumId: Int) : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaAndAlbumsAllModel>()

        val listMedia by model.imagesUpload.collectAsState()
        val albumInMetaScreen by model.albumInMetaScreen.collectAsState()

        LifecycleEffect(onStarted = {
            model.getListImageForUpload()
            model.getAlbum(albumId)
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            ViewMetaScreen(
                onClickBack = model::goBackStack,
                onClickMedia = {},
                listMedia = listMedia,
            )
            PanelBottom {
                ButtonWeakApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    onClick = model::goBackStack,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = true,
                    onClick = {
                        listMedia?.map { it.uri.toUri() }?.let { uri ->
                                model.uploadPhoto(uri,albumInMetaScreen)
                        }
                    },
                    text = TextApp.titleAdd
                )
            }
        }
    }
}

@Composable
fun ColumnScope.ViewMetaScreen(
    onClickBack: () -> Unit,
    onClickMedia: (Uri) -> Unit,
    listMedia: List<DataForPostingMedia>?,
    greedColumn: Int = 2,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)

    ) {
        PanelNavBackTop(
            modifier = Modifier,
            onClickBack = onClickBack,
            text = TextApp.textAddPhoto
        )
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
                items = listMedia ?: listOf(),
            ) { index, item ->

                Column(modifier = Modifier
                    .shadow(
                        elevation = DimApp.shadowElevation,
                        shape = ThemeApp.shape.mediumAll
                    )
                    .background(ThemeApp.colors.backgroundVariant)
                    .clickableRipple { onClickMedia.invoke(item.uri.toUri()) }) {

                    BoxImageLoad(
                        modifier = Modifier
                            .padding(DimApp.screenPadding * .5f)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(ThemeApp.shape.smallAll),
                        drawableError = R.drawable.stub_photo,
                        contentScale = ContentScale.FillWidth,
                        image = item.uri,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconApp(
                            modifier = Modifier
                                .background(ThemeApp.colors.backgroundVariant)
                                .padding(DimApp.screenPadding * .3f),
                            tint = ThemeApp.colors.primary,
                            painter = rememberImageRaw(id = R.raw.ic_location)
                        )
                        TextCaption(
                            modifier = Modifier
                                .padding(DimApp.screenPadding * .5f),
                            text = item.address ?: ""
                        )
                    }

                }
            }
        }
    }
}