package ru.droid.tech.screens.module_main.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.extension.DriveShadow
import ru.droid.tech.base.extension.drawColoredShadow
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.data.common.models.res.TextApp

@Composable
fun ContentCardWithAvatar(
    avatar: Any?,
    name: String,
    locationCity: String?,
    onClickDescriptions: () -> Unit,
    onClickRedactionProfile: () -> Unit,
    isVisibilityDescriptions: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(DimApp.avatarProfileSize + DimApp.screenPadding)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .align(Alignment.BottomCenter)
                    .drawColoredShadow(
                        driveShadow = DriveShadow.UPPER
                    )
                    .clip(ThemeApp.shape.mediumTop)
                    .background(color = ThemeApp.colors.backgroundVariant)
            )
            Box(
                modifier = Modifier
                    .padding(bottom = DimApp.screenPadding)
                    .align(Alignment.Center)
                    .shadow(
                        elevation = DimApp.shadowElevation,
                        shape = CircleShape
                    )
                    .size(DimApp.avatarProfileSize)
                    .border(
                        width = DimApp.lineWidthBorderProfile,
                        color = ThemeApp.colors.backgroundVariant,
                        shape = CircleShape
                    )
                    .background(color = ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.lineWidthBorderProfile)
            ) {

                BoxImageLoad(
                    modifier = Modifier.fillMaxSize(),
                    drawableError = R.drawable.stab_avatar,
                    drawablePlaceholder = R.drawable.stab_avatar,
                    image = avatar
                )
            }
        }

        AnimatedVisibility(visible = isVisibilityDescriptions) {
            Column(
                modifier = Modifier
                    .background(color = ThemeApp.colors.backgroundVariant)
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                ButtonWeakApp(
                    onClick = onClickRedactionProfile,
                    text = TextApp.holderEditProfile
                )
                BoxSpacer(.5f)
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(DimApp.screenPadding)
                .drawColoredShadow(
                    offsetY = 5.dp,
                    driveShadow = DriveShadow.LOWER
                )
                .clip(ThemeApp.shape.mediumBottom)
                .background(color = ThemeApp.colors.backgroundVariant)
        )
    }
}