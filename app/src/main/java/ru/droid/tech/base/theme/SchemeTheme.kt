package ru.droid.tech.base.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import ru.droid.tech.base.res.LightPaletteApp
import ru.droid.tech.base.res.ShapesApp
import ru.droid.tech.base.res.TypographyApp


@Immutable
data class ColorsSchemeApp(
    val primary: Color,
    val onPrimary: Color,

    val background: Color,
    val onBackground: Color,

    val container: Color,
    val onContainer: Color,

    val containerVariant: Color,
    val onContainerVariant: Color,

    val backgroundVariant: Color,
    val onBackgroundVariant: Color,

    val borderLight: Color,
    val borderDark: Color,

    val textLight: Color,
    val textTransparent: Color,
    val textDark: Color,

    val attentionContent: Color,
    val goodContent: Color,
)


@Immutable
data class ShapeSchemeApp(
    val smallTop: CornerBasedShape,
    val smallAll: CornerBasedShape,
    val smallRight: CornerBasedShape,
    val mediumTop: CornerBasedShape,
    val mediumBottom: CornerBasedShape,
    val mediumLeft: CornerBasedShape,
    val mediumRight: CornerBasedShape,
    val mediumAll: CornerBasedShape,
)

@Immutable
data class TypographySchemeApp(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val button: TextStyle,
    val bodyMedium: TextStyle,
    val bodyLarge: TextStyle,
    val caption: TextStyle,
    val label: TextStyle,
    val labelSmall: TextStyle,
)

val LocalColorsApp = staticCompositionLocalOf { LightPaletteApp }
val LocalTypographyApp = staticCompositionLocalOf { TypographyApp }
val LocalShapeApp = staticCompositionLocalOf { ShapesApp }


object ThemeApp {
    val colors: ColorsSchemeApp
        @Composable
        get() = LocalColorsApp.current
    val typography: TypographySchemeApp
        @Composable
        get() = LocalTypographyApp.current
    val shape: ShapeSchemeApp
        @Composable
        get() = LocalShapeApp.current
}
