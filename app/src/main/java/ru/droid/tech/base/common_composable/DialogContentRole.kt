package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.res.TextApp


@Composable
fun DialogContentRole(
    roleEnter: RoleDroid?,
    setRoleDroid: (RoleDroid) -> Unit,
    onDismiss: () -> Unit,

    ) {
    var roleSet by rememberState { roleEnter }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(horizontal = DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textSpecifyGender
        )
        BoxSpacer(2f)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    roleSet = RoleDroid.SPOUSE
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButtonApp(
                checked = roleSet == RoleDroid.SPOUSE,
                onCheckedChange = { roleSet = RoleDroid.SPOUSE },
            )

            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = Gender.MAN.getGenderText()
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    roleSet = RoleDroid.CHILD
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonApp(
                checked = roleSet == RoleDroid.CHILD,
                onCheckedChange = { roleSet = RoleDroid.CHILD },
            )
            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = Gender.WOMAN.getGenderText()
            )
        }
        BoxSpacer()
        Row(modifier = Modifier.fillMaxWidth()) {
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
                enabled = roleSet != null,
                onClick = {
                    roleSet?.let { genderChoose ->
                        setRoleDroid.invoke(genderChoose)
                        onDismiss.invoke()
                    }
                },
                text = TextApp.titleNext
            )
        }
        BoxSpacer()
    }
}
