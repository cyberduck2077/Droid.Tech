package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
@NonRestartableComposable
fun BoxSpacer(
    ratio: Float = 1f,
    size: Dp = DimApp.screenPadding
) {
    Box(modifier = Modifier.size(size * ratio))
}

@Composable
@NonRestartableComposable
fun ColumnScope.BoxFillWeight() {
    Box(modifier = Modifier.weight(1f))
}

@Composable
@NonRestartableComposable
fun FillLineHorizontal(
    modifier:Modifier = Modifier,
    color: Color = ThemeApp.colors.borderLight.copy(.5f)
) {
    Box(modifier = modifier.fillMaxWidth().height(DimApp.lineHeight).background(color))
}


@Composable
@NonRestartableComposable
fun RowScope.BoxFillWeight() {
    Box(modifier = Modifier.weight(1f))
}