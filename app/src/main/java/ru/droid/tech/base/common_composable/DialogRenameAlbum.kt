package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState

@Composable
fun DialogRenameAlbum(
    onDismiss: () -> Unit,
    text: String,
    onChangeText: (String) -> Unit,
) {
    val newName = rememberState(text) { TextFieldValue(text) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.backgroundVariant)
                .padding(DimApp.screenPadding)
        ) {
            TextTitleSmall(text = TextApp.textRenameAlbum)

            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = newName.value,
                onValueChange = { newName.value = it },
                placeholder = { Text(text = TextApp.textIAmSuchAndSuch) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onChangeText(newName.value.text)
                        onDismiss.invoke()
                    }
                ),
            )
            Row(
                modifier = Modifier
                    .padding(DimApp.screenPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        onChangeText(newName.value.text)
                        onDismiss.invoke()
                    },
                    text = TextApp.textWrite
                )
            }
        }
    }
}
