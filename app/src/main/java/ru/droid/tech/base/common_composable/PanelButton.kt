package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun PanelBottom(
    modifier: Modifier = Modifier,
    contentColor: Color = ThemeApp.colors.backgroundVariant,
    topLineColor: Color = contentColor,
    height: Dp = DimApp.heightBottomNavigationPanel,
    content: @Composable RowScope.()->Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(DimApp.shadowElevation)
            .height(height)
            .background(contentColor),
    ) {

        FillLineHorizontal(
            modifier = Modifier.fillMaxWidth(),
            color = topLineColor
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            content.invoke(this)
        }
    }
}