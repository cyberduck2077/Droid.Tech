package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw

@Composable
fun DialogYesOrNo(
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    title: String,
    body: String,
    buttonCancel: String = TextApp.titleCancel,
    buttonOk: String = TextApp.titleNext,
) {

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
                .padding(horizontal = DimApp.screenPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextTitleSmall(text = title)
                IconButtonApp(
                    modifier = Modifier.offset(x = DimApp.screenPadding),
                    onClick = onDismiss
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_close))
                }
            }

            TextBodyLarge(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = body
            )
            BoxSpacer()
            BoxSpacer()
            Row(modifier = Modifier.fillMaxWidth()) {
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = buttonCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        onClick.invoke()
                        onDismiss.invoke()
                    },
                    text = buttonOk
                )
            }
            BoxSpacer()
        }
    }
}