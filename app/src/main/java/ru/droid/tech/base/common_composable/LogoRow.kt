package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun BoxScope.LogoRow(alpha: Float = 1f) {
    Row(
        modifier = Modifier
            .align(Alignment.Center),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BoxImageRowRes(
            modifier = Modifier.size(DimApp.iconSizeOrder),
            image = R.raw.ic_navigation_bar,
            alpha = alpha,
        )
        Box(modifier = Modifier.size(DimApp.screenPadding))
        Text(
            style = ThemeApp.typography.titleLarge.copy(fontSize = DimApp.fontSplashSize),
            text = TextApp.baseTextNameApp,
            color = ThemeApp.colors.onPrimary.copy(alpha = alpha)
        )
    }
}