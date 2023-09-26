package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw

@Composable
fun DropMenuInAlbumContains(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAddToFavorite: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_star)
                        )
                        TextButtonStyle(text = TextApp.textAddToFavorite)
                    }
                },
                onClick = {
                    onAddToFavorite.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_edit)
                        )
                        TextButtonStyle(text = TextApp.textRename)
                    }
                },
                onClick = {
                    onRename.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_download)
                        )
                        TextButtonStyle(text = TextApp.textDownload)
                    }
                },
                onClick = {
                    onDownload.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_delete)
                        )
                        TextButtonStyle(text = TextApp.textDelete)
                    }
                },
                onClick = {
                    onDelete.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

@Composable
fun DropMenuInYourPost(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onRemake: () -> Unit,
    onDelete: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {
            DropdownMenuItem(
                text = {
                    TextButtonStyle(text = TextApp.textRemake)
                },
                onClick = {
                    onRemake.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    TextButtonStyle(text = TextApp.textDelete)
                },
                onClick = {
                    onDelete.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

@Composable
fun DropMenuInNotYourPost(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onToComplain: () -> Unit,
    onSaveToYourFiles: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {
            DropdownMenuItem(
                text = {
                    TextButtonStyle(text = TextApp.textToComplain)
                },
                onClick = {
                    onToComplain.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    TextButtonStyle(text = TextApp.textSaveToYourFiles)
                },
                onClick = {
                    onSaveToYourFiles.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

@Composable
fun DropMenuRibbonTop(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onTypePosts: () -> Unit,
    onAllDroid: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {
            DropdownMenuItem(
                text = {
                        TextButtonStyle(
                            modifier = Modifier.padding(end = DimApp.screenPadding),
                            text = TextApp.textPostType
                        )
                },
                leadingIcon = {
                    IconApp(painter = rememberImageRaw(R.raw.ic_setting_post))
                },
                onClick = {
                    onTypePosts.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                        TextButtonStyle(
                            modifier = Modifier.padding(end = DimApp.screenPadding),
                            text = TextApp.textAllCollectives)
                },
                leadingIcon = {
                    IconApp(painter = rememberImageRaw(R.raw.ic_Droid), tint = null)
                },
                onClick = {
                    onAllDroid.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

@Composable
fun DropMenuInMedia(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAddToFavorite: () -> Unit,
    onGoToAlbum: () -> Unit,
    onRename: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss,

            ) {

            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_edit)
                        )
                        TextButtonStyle(text = TextApp.textRemake)
                    }
                },
                onClick = {
                    onRename.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_star)
                        )
                        TextButtonStyle(text = TextApp.textAddToFavorite)
                    }
                },
                onClick = {
                    onAddToFavorite.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_download)
                        )
                        TextButtonStyle(text = TextApp.textDownload)
                    }
                },
                onClick = {
                    onDownload.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_album)
                        )
                        TextButtonStyle(text = TextApp.textGoToAlbum)
                    }
                },
                onClick = {
                    onGoToAlbum.invoke()
                    onDismiss.invoke()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                            painter = rememberImageRaw(R.raw.ic_delete)
                        )
                        TextButtonStyle(text = TextApp.textDelete)
                    }
                },
                onClick = {
                    onDelete.invoke()
                    onDismiss.invoke()
                }
            )
        }
    }
}

