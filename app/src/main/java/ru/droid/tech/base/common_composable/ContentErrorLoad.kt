package ru.droid.tech.base.common_composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun ContentErrorLoad(
    visible: Boolean = false
){
    Column() {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThemeApp.colors.attentionContent),
            visible = visible) {
            Box(modifier = Modifier
                .padding(horizontal = DimApp.screenPadding)
                .padding(vertical = DimApp.textPaddingMin)) {
                TextBodyLarge(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = TextApp.textErrorPagingContent,
                    color = ThemeApp.colors.background)
            }
        }
    }
}