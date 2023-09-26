package ru.droid.tech.base.util.crop

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState

data class CropifyOption(
    val frameColor: Color = Color.White,
    val frameAlpha: Float = 0.8f,
    val frameWidth: Dp = 2.dp,
    val frameAspectRatio: AspectRatio? = null,
    val gridColor: Color = Color.White,
    val gridAlpha: Float = 0.6f,
    val gridWidth: Dp = 1.dp,
    val maskColor: Color = Color.Black,
    val maskAlpha: Float = 0.5f,
    val backgroundColor: Color = Color.Black,
)

@Composable
fun rememberCropifyOption(
    gridColor: Color = ThemeApp.colors.primary
) = rememberState {
    CropifyOption().copy(
        frameAspectRatio = AspectRatio(1, 1),
        gridColor = gridColor,
        frameColor = gridColor, )
}