package ru.droid.tech.screens.module_main.media_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.DialogDeletePhoto
import ru.droid.tech.base.common_composable.DropMenuInMedia
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PagerImageWithOutDownload
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.ProgressIndicatorApp
import ru.droid.tech.base.common_composable.colorsIconButtonApp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.MediaUI

class MediaListScreen(
    private val albumId: Int = 0,
    private val mediaId: Int
) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<MediaListModel>()
        var isViewDelete by rememberState { false }
        val media by model.listMedia.collectAsState()
        var mediaNameDelete by rememberState { "" }
        var mediaIdDelete by rememberState { -1 }

        LifecycleEffect(onStarted = {
            model.getMedia(albumId)
        })

        BackPressHandler { model.goBackStack() }

        ImageViewPager(
            onClickBack = model::goBackStack,
            onEdit = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)
                mMedia?.id?.let { model.goToEditScreen(it) }
            },
            onAddToFavorite = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)
                mMedia?.let { model.addToFavoriteMedia(it) }
            },
            onDownload = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)//todo()
            },
            onDelete = { currentIndex ->
                val mMedia = media.getOrNull(currentIndex)

                mMedia?.id?.let { mediaIdDelete = it }
                mMedia?.name?.let { mediaNameDelete = it }
                isViewDelete = true
            },
            imageList = media,
            currentIndexContent = media.indexOfFirst { it.id == mediaId },
            onGoToAlbum = { currentIndex ->
                model.getAlbumAndGo()
            },
        )
        if (isViewDelete)
            DialogDeletePhoto(
                namePhoto = mediaNameDelete,
                onDismiss = { isViewDelete = false },
                onYes = {
                    model.deleteMedia(mediaIdDelete)
                })
    }
}

@Composable
fun ImageViewPager(
    onClickBack: () -> Unit,
    onAddToFavorite: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onDownload: (Int) -> Unit,
    onGoToAlbum: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    imageList: List<MediaUI?>,
    currentIndexContent: Int
) {
    var expandedMero by rememberState { false }
    var currentIndex by rememberState(currentIndexContent) { currentIndexContent }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Black)
    ) {
        TopImageView(
            modifier = Modifier,
            onClickBack = onClickBack,
            onClickMero = { expandedMero = !expandedMero },
            nameAlbum = "${currentIndex + 1} из ${imageList.size}",
            isViewMero = expandedMero,
            onAddToFavorite = { onAddToFavorite(currentIndex) },
            onRename = { onEdit(currentIndex) },
            onDownload = { onDownload(currentIndex) },
            onDelete = { onDelete(currentIndex) },
            onGoToAlbum = { onGoToAlbum(currentIndex) },

            )
        BoxSpacer()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (currentIndex == -1)
                ProgressIndicatorApp(
                    modifier = Modifier
                        .size(DimApp.iconSizeTouchStandard)
                )
            else
                PagerImageWithOutDownload(
                    modifier = Modifier
                        .fillMaxWidth(),
                    images = imageList,
                    isIndicatorOff = true,
                    itPageIndex = {
                        currentIndex = it
                    },
                    initialPage = currentIndexContent,
                    onClick = {

                    }
                )
        }
        BoxSpacer()
    }
}

@Composable
private fun TopImageView(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    onClickMero: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onGoToAlbum: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    isViewMero: Boolean,
    nameAlbum: String,
) {
    PanelNavBackTop(
        modifier = modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
        container = Color.Black,
        color = ThemeApp.colors.container
    ) {
        BoxFillWeight()
        DropMenuInMedia(
            expanded = isViewMero,
            onDismiss = onClickMero,
            onAddToFavorite = onAddToFavorite,
            onGoToAlbum = onGoToAlbum,
            onRename = onRename,
            onDownload = onDownload,
            onDelete = onDelete,
            content = {
                IconButtonApp(
                    modifier = Modifier,
                    onClick = onClickMero,
                    colors = colorsIconButtonApp().copy(contentColor = ThemeApp.colors.container)
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
                }
            })
    }
}

@Composable
fun SwiperImage() {

}
