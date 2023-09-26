package ru.droid.tech.screens.module_main.media.album

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ColorButtonApp
import ru.droid.tech.base.common_composable.DialogGetImageList
import ru.droid.tech.base.common_composable.DialogRenameAlbum
import ru.droid.tech.base.common_composable.DropMenuInAlbumContains
import ru.droid.tech.base.common_composable.FloatingActionButtonApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.ProgressIndicatorApp
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.common_composable.colorsButtonAccentTextApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.extension.minLinesHeight
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.res.TextApp

class AlbumScreen(private val albumId: Int) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<AlbumModel>()

        val listMedia by model.listMedia.collectAsState()
        val albumInfo by model.albumInfo.collectAsState()

        LifeScreen(onCreate = { model.getMediaByAlbum(albumId) })

        var isViewRenameDialog by rememberState { false }
        var isViewInfo by rememberState { false }
        var getImage by rememberState { false }
        var getImageNew by rememberState { false }

        AlbumContains(
            onClickBack = model::goBackStack,
            onClickInfo = { isViewInfo = true },
            nameAlbum = albumInfo.name ?: "",
            listMedia = listMedia,
            onClickAddPhoto = { getImage = true },
            onAddToFavorite = model::setAlbumIsFavorite,
            onRename = { isViewRenameDialog = true },
            onDownload = model::downloadAlbum,
            onDelete = model::deleteAlbum,
            isMainAlbum = albumInfo.id == 0,
            onClickAddPhotoTwo = { getImageNew = true },
            onClickMedia = { model.goToViewScreen(it, albumInfo) }
        )


        if (isViewInfo) {
            AlbumInfo(
                onClickBack = { isViewInfo = false },
                onCreate = model::changeAlbum,
                nameAlbum = albumInfo.name ?: "",
                nameOwner = albumInfo.owner.getFullName() ?: "",
                nameAddress = albumInfo.location ?: "",
                date = albumInfo.customDateHuman,
                description = albumInfo.description ?: "",
            )
        }

        if (isViewRenameDialog) {
            DialogRenameAlbum(
                onDismiss = { isViewRenameDialog = false },
                text = albumInfo.name ?: "",
                onChangeText = model::renameAlbum
            )
        }

        if (getImage) {
            DialogGetImageList(
                onDismiss = { getImage = false },
                getPhoto = {
                    model.uploadPhoto(it)
                    getImage = false
                }
            )
        }
        if (getImageNew) {
            DialogGetImageList(
                onDismiss = { getImageNew = false },
                getPhoto = {
                    if (it.isNotEmpty()) {
                        model.setImagesForUpload(it)
                        model.goToMetaScreen(albumInfo)
                    }
                    getImageNew = false
                }
            )
        }
    }
}

@Composable
private fun AlbumContains(
    onClickBack: () -> Unit,
    onClickInfo: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    onClickMedia: (MediaUI) -> Unit,
    onClickAddPhoto: () -> Unit,
    onClickAddPhotoTwo: () -> Unit,
    nameAlbum: String,
    listMedia: List<MediaUI>,
    greedColumn: Int = 3,
    isMainAlbum: Boolean
) {
    var expandedMero by rememberState { false }
    var isViewLoad by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = listMedia) {
        isViewLoad = if (listMedia.isNotEmpty()) {
            false
        } else {
            delay(3000L)
            false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopPanel(
                onClickBack = onClickBack,
                onClickInfo = onClickInfo,
                onClickMero = { expandedMero = !expandedMero },
                nameAlbum = nameAlbum,
                isViewMero = expandedMero,
                onAddToFavorite = onAddToFavorite,
                onRename = onRename,
                onDownload = onDownload,
                onDelete = onDelete,
                isMainAlbum = isMainAlbum
            )

            if (isViewLoad) {
                BoxFillWeight()
                ProgressIndicatorApp(
                    modifier = Modifier
                        .size(DimApp.iconSizeTouchStandard)
                        .align(Alignment.CenterHorizontally)
                )
                BoxFillWeight()
            }
            if (listMedia.isEmpty() && !isViewLoad)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextBodyLarge(
                            text = TextApp.textNoPhotoInAlbum,
                            textAlign = TextAlign.Center
                        )
                        BoxSpacer()
                        TextButtonApp(
                            onClick = onClickAddPhoto,
                            text = TextApp.textAddOnePhoto,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DimApp.screenPadding),
                            colors = ColorButtonApp(
                                containerColor = ThemeApp.colors.backgroundVariant,
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
                    }
                }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                columns = GridCells.Fixed(greedColumn),
                contentPadding = PaddingValues(DimApp.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(
                    DimApp.screenPadding,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(
                    DimApp.screenPadding,
                    Alignment.Top
                ),
            ) {
                items(
                    items = listMedia,
                    key = { it.id },
                    contentType = { it.isVideo }
                ) { item ->

                    when (item.isVideo) {
                        true -> {}
                        false -> {
                            BoxImageLoad(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(ThemeApp.shape.smallAll)
                                    .background(Color.Transparent.copy(.05f))
                                    .clickableRipple { onClickMedia.invoke(item) },
                                drawableError = R.drawable.stub_photo,
                                drawablePlaceholder = R.drawable.stub_photo,
                                image = item.url,
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButtonApp(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(DimApp.screenPadding),
            onClick = onClickAddPhotoTwo
        ) {
            IconApp(
                modifier = Modifier.rotate(45f),
                painter = rememberImageRaw(id = R.raw.ic_close)
            )
        }
    }
}

@Composable
private fun TopPanel(
    onClickBack: () -> Unit,
    onClickInfo: () -> Unit,
    onClickMero: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    isViewMero: Boolean,
    nameAlbum: String,
    isMainAlbum: Boolean = false
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
    ) {
        BoxFillWeight()
        if (!isMainAlbum) {
            IconButtonApp(
                modifier = Modifier,
                onClick = onClickInfo
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_info))
            }
            DropMenuInAlbumContains(
                expanded = isViewMero,
                onDismiss = onClickMero,
                onAddToFavorite = onAddToFavorite,
                onRename = onRename,
                onDownload = onDownload,
                onDelete = onDelete,
                content = {
                    IconButtonApp(
                        modifier = Modifier,
                        onClick = onClickMero
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
                    }
                })
        }
    }
}

@Composable
private fun TopAddingPhoto(
    onClickBack: () -> Unit,
    onClickSelect: () -> Unit,
    text: String,
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = text,
        painter = rememberImageRaw(R.raw.ic_close)
    ) {
        IconButtonApp(
            modifier = Modifier,
            onClick = onClickSelect
        ) {
            IconApp(
                painter = rememberImageRaw(R.raw.ic_check),
                tint = ThemeApp.colors.primary
            )
        }
    }
}

@Composable
private fun TopInfo(
    onClickBack: () -> Unit,
    nameAlbum: String,
) {
    PanelNavBackTop(
        modifier = Modifier,
        onClickBack = onClickBack,
        text = nameAlbum,
        painter = rememberImageRaw(R.raw.ic_close)
    )
}


@Composable
private fun AlbumInfo(
    onClickBack: () -> Unit,
    onCreate: (
        desc: String,
    ) -> Unit,
    nameAlbum: String,
    nameOwner: String,
    nameAddress: String,
    date: String,
    description: String,
) {
    var isVisibleEditText by remember { mutableStateOf(false) }
    var descriptionEnter by rememberState { TextFieldValue(description) }
    val countChar by rememberState(descriptionEnter.text) { descriptionEnter.text.length }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .systemBarsPadding()
    ) {
        TopInfo(onClickBack = onClickBack, nameAlbum = nameAlbum)
        BoxSpacer()
        AnimatedVisibility(
            visible = !isVisibleEditText,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ) + fadeOut(),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextBodyMedium(
                        text = TextApp.textDescription,
                        modifier = Modifier
                    )
                    IconButtonApp(
                        modifier = Modifier,
                        onClick = {
                            isVisibleEditText = true
                        }
                    ) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_edit))
                    }
                }
                TextTitleSmall(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    text = descriptionEnter.text
                )
            }
        }
        AnimatedVisibility(
            visible = isVisibleEditText,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            ) + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
            ) {
                TextFieldOutlinesApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .minLinesHeight(8, ThemeApp.typography.bodyLarge),
                    value = descriptionEnter,
                    onValueChange = {
                        descriptionEnter = it
                    },
                    isError = countChar > 150,
                    placeholder = { Text(text = TextApp.textDescription) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            isVisibleEditText = false
                        }
                    ),
                )
                TextCaption(text = "${countChar}/150", modifier = Modifier.align(Alignment.End))
            }
        }
        TextBodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DimApp.screenPadding,
                    end = DimApp.screenPadding,
                    top = DimApp.screenPadding
                ), text = TextApp.textOwner
        )
        TextTitleSmall(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding), text = nameOwner
        )
        TextBodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DimApp.screenPadding,
                    end = DimApp.screenPadding,
                    top = DimApp.screenPadding
                ), text = TextApp.textDate
        )
        TextTitleSmall(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding), text = date
        )

        TextBodyMedium(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DimApp.screenPadding,
                    end = DimApp.screenPadding,
                    top = DimApp.screenPadding
                ), text = TextApp.textAddress
        )
        TextTitleSmall(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding),
            text = nameAddress
        )
        BoxFillWeight()
        PanelBottom(modifier = Modifier.align(Alignment.End)) {
            TextButtonApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                colors = colorsButtonAccentTextApp().copy(contentColor = ThemeApp.colors.textDark),
                onClick = onClickBack,
                text = TextApp.titleCancel
            )
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                enabled = countChar <= 150,
                onClick = {
                    onCreate(descriptionEnter.text)
                    onClickBack.invoke()
                },
                text = TextApp.titleCreate
            )
        }
    }
}

