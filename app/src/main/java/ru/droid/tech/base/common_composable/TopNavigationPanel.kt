package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw

@Composable
fun PanelNavBackTop(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    text: String = "",
    painter: Painter = rememberImageRaw(R.raw.ic_arrow_back),
    textAlign: TextAlign = TextAlign.Start,
    container: Color = ThemeApp.colors.backgroundVariant,
    color: Color = ThemeApp.colors.textDark,
    content: @Composable RowScope.() -> Unit = {},
) {
    val colorButton = colorsIconButtonApp().copy(contentColor = color)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .background(container),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButtonApp(
            modifier = Modifier,
            onClick = onClickBack,
            colors = colorButton
        ) {
            IconApp(painter = painter)
        }
        TextBodyLarge(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = textAlign,
            text = text,
            color = color,
        )
        content.invoke(this)
    }
}