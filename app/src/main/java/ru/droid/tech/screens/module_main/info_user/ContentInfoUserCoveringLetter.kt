package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextFieldApp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.res.TextApp

@Composable
fun ContentInfoUserCoveringLetter(
    onDismiss: () -> Unit,
    onClickSendLetter: (String) -> Unit,
) {
    var text by rememberState { TextFieldValue() }
    val focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .imePadding()
        ) {
            PanelNavBackTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = DimApp.shadowElevation)
                    .background(ThemeApp.colors.backgroundVariant),
                onClickBack = onDismiss,
                container = ThemeApp.colors.backgroundVariant,
                text = TextApp.textCoveringLetter,
            )
            TextFieldApp(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                value = text,
                isIndicatorOn = false,
                onValueChange = { text = it },
                placeholder = { Text(text = TextApp.textComment) },
                keyboardActions = KeyboardActions(onSend = {
                    if (text.text.isNotEmpty()) {
                        onClickSendLetter.invoke(text.text)
                        onDismiss.invoke()
                    }
                    focus.clearFocus()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                )
            )
        }

        PanelBottom {
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                enabled = text.text.isNotEmpty(),
                onClick = {
                    onClickSendLetter.invoke(text.text)
                    onDismiss.invoke()
                },
                text = TextApp.textSend
            )
        }
    }
}
