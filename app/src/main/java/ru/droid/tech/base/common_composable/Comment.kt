package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import ru.droid.tech.R
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.res.TextApp

@Composable
fun Comment(
    name: String,
    text: String,
    date: String,
    avatar: String?,
    isParentExist: Boolean,
    modifier: Modifier,
    onClickAnswer: () -> Unit,
) {
    var mWidth by rememberState { 0 }
    Row(
        modifier = modifier
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(horizontal = DimApp.screenPadding)
            .padding(bottom = DimApp.screenPadding),
        horizontalArrangement = Arrangement.End
    ) {
        if (isParentExist)
            Box(
                modifier = Modifier
                    .fillMaxWidth(.1f)
            )
        BoxImageLoad(
            image = avatar ?: R.drawable.stab_avatar,
            modifier = Modifier
                .padding(end = DimApp.screenPadding / 2)
                .clip(CircleShape)
                .size(DimApp.imageCommentAvatarSize),
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { it ->
                mWidth = it.size.width
            }) {
            TextTitleSmall(
                text = name,
                modifier = Modifier.padding(bottom = DimApp.screenPadding / 2)
            )
            TextBodyMedium(
                text = text,
                modifier = Modifier.padding(bottom = DimApp.screenPadding / 2)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextCaption(text = date)
                TextButtonStyle(
                    text = TextApp.textAnswer,
                    modifier = Modifier.clickableRipple { onClickAnswer.invoke() }
                )
            }
        }
    }
}