package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.data.common.models.res.TextApp

@Composable
fun ContentInfoUserProfileCart(
    avatar: String?,
    name: String,
    locationCity: String?,
    onClickDescriptions: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxSpacer()
        BoxImageLoad(
            modifier = Modifier
                .size(DimApp.avatarProfileSize)
                .border(
                    width = DimApp.lineWidthBorderProfile,
                    color = ThemeApp.colors.backgroundVariant,
                    shape = CircleShape
                )
                .background(color = ThemeApp.colors.backgroundVariant)
                .clip(shape = CircleShape),
            drawableError = R.drawable.stab_avatar,
            drawablePlaceholder = R.drawable.stab_avatar,
            image = avatar
        )
        BoxSpacer()
        TextTitleMedium(text = name)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            locationCity?.let {
                IconApp(
                    modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                    painter = rememberImageRaw(id = R.raw.ic_location)
                )
                TextBodyMedium(text = locationCity)
            }

            TextButtonApp(
                onClick = onClickDescriptions,
                text = TextApp.holderMore,
                contentStart = {
                    IconApp(
                        modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                        painter = rememberImageRaw(id = R.raw.ic_info)
                    )
                })
        }
        BoxSpacer(.5f)
        this.content()
        BoxSpacer(.5f)
    }
}
