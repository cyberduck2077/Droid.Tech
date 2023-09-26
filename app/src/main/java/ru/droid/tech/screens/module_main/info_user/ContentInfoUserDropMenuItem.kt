package ru.droid.tech.screens.module_main.info_user

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.util.rememberImageRaw

@Composable
fun ContentInfoUserDropMenuItem(
    onClick: () -> Unit,
    @RawRes icon: Int,
    text: String,
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconApp(painter = rememberImageRaw(id = icon))
                BoxSpacer()
                TextButtonStyle(text = text)
            }
        },
        onClick = onClick
    )
}
