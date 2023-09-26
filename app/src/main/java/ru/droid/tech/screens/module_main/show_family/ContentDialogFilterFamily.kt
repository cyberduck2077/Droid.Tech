package ru.droid.tech.screens.module_main.show_Droid

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
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonAccentTextApp
import ru.droid.tech.base.common_composable.DropMenuApp
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.RoleDroid

@Composable
fun ContentDialogFilterDroid(
    roleDroid: RoleDroid?,
    onFilterDroid: (roleDroid: RoleDroid?) -> Unit,
    onDismiss: () -> Unit,
) {

    var roleDroidChoose by rememberState(roleDroid) { roleDroid }
    val listRoleDroid = remember { RoleDroid.values().toList() }

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
            TextTitleSmall(text = TextApp.textInvite)
            IconButtonApp(
                modifier = Modifier.offset(x = DimApp.screenPadding),
                onClick = onDismiss
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_close))
            }
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textSort)
        BoxSpacer(0.5f)
        DropMenuApp(
            items = listRoleDroid,
            modifier = Modifier
                .fillMaxWidth(),
            checkItem = { roleDroidChoose = it },
            chooserText = roleDroidChoose?.getTextRoleMembers() ?: "",
            placeholderText = TextApp.textMyDroid,
            menuItem = { item ->
                TextBodyLarge(text = item.getTextRoleMembers())
                FillLineHorizontal(modifier = Modifier.fillMaxWidth())
            }
        )
        BoxSpacer()

        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonAccentTextApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    onFilterDroid.invoke(null)
                    onDismiss.invoke()
                },
                text = TextApp.titleCancel
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    onFilterDroid.invoke(roleDroidChoose)
                    onDismiss.invoke()
                },
                text = TextApp.titleNext
            )
        }
        BoxSpacer()
    }
}