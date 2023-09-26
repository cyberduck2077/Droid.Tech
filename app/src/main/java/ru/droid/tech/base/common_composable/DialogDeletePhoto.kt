package ru.droid.tech.base.common_composable

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp


@Composable
fun DialogDeletePhoto(
    namePhoto: String,
    onDismiss: () -> Unit,
    onYes: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
                .padding(DimApp.screenPadding)
        ) {
            TextTitleSmall(text = TextApp.textTitleDeletePhoto)
            BoxSpacer()
            TextBodyMedium(text = TextApp.formatDelete(namePhoto))
            BoxSpacer()
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BoxSpacer()
                TextButtonApp(
                    modifier = Modifier,
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                TextButtonApp(
                    modifier = Modifier,
                    onClick = onYes,
                    text = TextApp.textDelete
                )
            }


        }
    }
}