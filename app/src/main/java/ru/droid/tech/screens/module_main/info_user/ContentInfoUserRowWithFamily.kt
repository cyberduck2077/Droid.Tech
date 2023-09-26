package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextLabel
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.data.common.models.local.maps.DroidMemberUI
import ru.data.common.models.res.TextApp

@Composable
fun ContentInfoUserRowWithDroid(
    members: List<DroidMemberUI>,
    onClickMember: (userId: Int?) -> Unit,
    onClickAll: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(horizontal = DimApp.screenPadding)
    ) {

        Row(
            modifier = Modifier
                .height(DimApp.heightButtonInLine)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextBodyLarge(
                text = TextApp.holderDroid
            )
            BoxFillWeight()
            if (members.size > 4) {
                TextButtonApp(
                    onClick = onClickAll,
                    text = TextApp.holderShowAll
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (members.size > 3) DimApp.screenPadding else 0.dp),
            horizontalArrangement = if (members.size > 3) Arrangement.SpaceBetween
            else Arrangement.spacedBy(DimApp.screenPadding * 2f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            members.take(4).forEach { item ->
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickableRipple(
                            bounded = false,
                            radius = DimApp.iconSizeTouchStandard
                        ) {
                            onClickMember.invoke(item.user?.id)
                        },

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(DimApp.iconSizeTouchStandard)
                            .clip(CircleShape),
                        drawableError = R.drawable.stab_avatar,
                        drawablePlaceholder = R.drawable.stab_avatar,
                        image = item.user?.avatar
                    )
                    TextLabel(text = item.firstName ?: "")
                }

            }
        }
        BoxSpacer()
    }
}