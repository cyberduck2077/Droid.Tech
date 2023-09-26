package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageRowRes
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonWeakSquareApp
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.FriendshipStatuses
import ru.data.common.models.res.TextApp

@Composable
fun ColumnScope.ContentInfoUserSomeone(
    avatar: String?,
    firstAndLastName: String,
    locationCity: String?,
    statusButton: FriendshipStatuses,
    onClickDescriptions: () -> Unit,
    onClickStatusButton: () -> Unit,
    onClickComplain: () -> Unit,
    onClickBlockUser: () -> Unit,
) {
    var expandedMero by rememberState { false }
    val onDismissDropMenu = remember {
        {
            expandedMero = false
        }
    }
    ContentInfoUserProfileCart(
        avatar = avatar,
        name = firstAndLastName,
        locationCity = locationCity,
        onClickDescriptions = onClickDescriptions,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier.weight(1f),
                onClick = onClickStatusButton,
                enabled = statusButton.isEnabledButton(),
                text = statusButton.textButton()
            )
            BoxSpacer()
            Box(modifier = Modifier.wrapContentSize()) {
                ButtonWeakSquareApp(
                    onClick = { expandedMero = !expandedMero },
                    icon = R.raw.ic_more
                )
                DropdownMenu(
                    modifier = Modifier
                        .background(color = ThemeApp.colors.backgroundVariant),
                    expanded = expandedMero,
                    onDismissRequest = onDismissDropMenu,
                ) {
                    ContentInfoUserDropMenuItem(
                        onClick = {
                            onClickBlockUser.invoke()
                            onDismissDropMenu.invoke()
                        },
                        icon = R.raw.ic_block,
                        text = TextApp.textBlockUser
                    )
                    ContentInfoUserDropMenuItem(
                        onClick = {
                            onClickComplain.invoke()
                            onDismissDropMenu.invoke()
                        },
                        icon = R.raw.ic_info,
                        text = TextApp.textComplain
                    )
                }
            }
            BoxSpacer()
        }
    }
    BoxSpacer()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(horizontal = DimApp.screenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxSpacer()
        TextTitleLarge(text = TextApp.textThisIsAClosedAccount)
        BoxSpacer()
        TextBodyLarge(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding * 2f),
            textAlign = TextAlign.Center,
            text = TextApp.textMakeFriendsWithDroid
        )
        BoxSpacer()
        BoxSpacer()
        BoxImageRowRes(
            image = R.raw.ic_lock,
            modifierImage = Modifier
                .size(DimApp.sizeUserLock),
            colorFilter = ColorFilter.tint(ThemeApp.colors.containerVariant),
        )
        BoxSpacer()
    }
}
