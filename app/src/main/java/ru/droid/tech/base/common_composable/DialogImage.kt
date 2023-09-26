package ru.droid.tech.base.common_composable

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.PermissionsModule
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.res.TextApp


@Composable
fun DialogGetImage(
    onDismiss: () -> Unit,
    uploadPhoto: (Uri) -> Unit,
) {
    var isViewDialog by remember { mutableStateOf(false) }
    val permission = PermissionsModule.launchPermissionCameraAndGallery { permition ->
        if (permition) {
            isViewDialog = true
        } else {
            isViewDialog = false
            onDismiss.invoke()
            gDMessage(TextApp.textMissingPermission)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        permission.invoke()
    })


    if (isViewDialog) {
        Dialog(onDismissRequest = onDismiss) {
            val gallerySecondStep =
                PermissionsModule.galleryLauncher { uri ->
                    uploadPhoto(uri)
                    onDismiss()
                }
            val cameraLauncherStep =
                PermissionsModule.cameraLauncher { uri ->
                    uploadPhoto(uri)
                    onDismiss()
                }
            Column(
                modifier = Modifier
                    .clip(ThemeApp.shape.mediumAll)
                    .background(ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.screenPadding)
            ) {

                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = cameraLauncherStep::invoke,
                    text = TextApp.textOpenTheCamera
                )
                BoxSpacer()
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = gallerySecondStep::invoke,
                    text = TextApp.textOpenGallery
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
            }
        }
    }
}

@Composable
fun DialogGetImageList(
    onDismiss: () -> Unit,
    getPhoto: (List<Uri>) -> Unit,
) {
    var isViewDialog by remember { mutableStateOf(false) }
    val permission = PermissionsModule.launchPermissionCameraAndGallery { permition ->
        if (permition) {
            isViewDialog = true
        } else {
            isViewDialog = false
            onDismiss.invoke()
            gDMessage(TextApp.textMissingPermission)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        permission.invoke()
    })


    if (isViewDialog) {
        Dialog(onDismissRequest = onDismiss) {
            val gallerySecondStep =
                PermissionsModule.getListImage { uris ->
                    getPhoto(uris)
                    onDismiss()
                }
            val cameraLauncherStep =
                PermissionsModule.cameraLauncher { uri ->
                    getPhoto(listOf(uri))
                    onDismiss()
                }
            Column(
                modifier = Modifier
                    .clip(ThemeApp.shape.mediumAll)
                    .background(ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.screenPadding)
            ) {

                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = cameraLauncherStep::invoke,
                    text = TextApp.textOpenTheCamera
                )
                BoxSpacer()
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = gallerySecondStep::invoke,
                    text = TextApp.textOpenGallery
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
            }
        }
    }
}


