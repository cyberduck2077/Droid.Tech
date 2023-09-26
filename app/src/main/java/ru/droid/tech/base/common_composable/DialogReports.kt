package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.TypeReason
import ru.data.common.models.res.TextApp

@Composable
fun DialogReports(
    onDismiss: () -> Unit,
    onClick: (typeReason: TypeReason, text: String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        var textField by rememberState { TextFieldValue() }
        var chooseReason by rememberState<TypeReason?> { null }
        val enabled = remember(textField, chooseReason) {
            textField.text.isNotEmpty() && chooseReason != null
        }

        val onSend = remember(enabled) {
            {
                val text = textField.text
                val choose = chooseReason
                if (text.isNotEmpty() && choose != null) {
                    onClick.invoke(choose, text)
                    onDismiss.invoke()
                }
            }
        }
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
                TextTitleSmall(text = TextApp.textComplaint)
                IconButtonApp(
                    modifier = Modifier.offset(x = DimApp.screenPadding),
                    onClick = onDismiss
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_close))
                }
            }
            TextBodyMedium(text = TextApp.textStateReasonComplain)
            BoxSpacer(0.5f)
            DropMenuApp(
                items = TypeReason.values().toList(),
                modifier = Modifier.fillMaxWidth(),
                checkItem = { chooseReason = it },
                chooserText = chooseReason?.getDescription() ?: "",
                placeholderText = TextApp.textStateReasonComplain,
                menuItem = { item ->
                    TextBodyLarge(text = item.getDescription())
                    FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                }
            )
            BoxSpacer()
            TextBodyMedium(text = TextApp.textComment)
            BoxSpacer(0.5f)
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = textField,
                onValueChange = { textField = it },
                minLines = 5
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                onClick = onSend,
                text = TextApp.textComplain
            )
            BoxSpacer()
        }
    }
}
