package ru.droid.tech.screens.module_main.info_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun ContentInfoUserStatusLoad() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(20))
                .background(ThemeApp.colors.background)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(DimApp.screenPadding),
                color = ThemeApp.colors.primary
            )
        }
    }
}
