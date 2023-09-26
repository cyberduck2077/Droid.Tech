package ru.droid.tech.base.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontDroidResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontDroid
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.res.DimApp
import kotlin.math.ceil
import kotlin.math.roundToInt

fun Modifier.minLinesHeight(
    minLines: Int,
    textStyle: TextStyle
) = composed {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val resolvedStyle = remember(textStyle, layoutDirection) {
        resolveDefaults(textStyle, layoutDirection)
    }
    val resourceLoader = LocalFontDroidResolver.current

    val heightOfTextLines = remember(
        density,
        textStyle,
        layoutDirection
    ) {
        val lines = (EmptyTextReplacement + "\n").repeat(minLines - 1)
        computeSizeForDefaultText(
            style = resolvedStyle,
            density = density,
            text = lines,
            maxLines = minLines,
            resourceLoader
        ).height
    }
    val heightInDp: Dp = with(density) { heightOfTextLines.toDp() }
    Modifier.defaultMinSize(minHeight = heightInDp)
}

private fun computeSizeForDefaultText(
    style: TextStyle,
    density: Density,
    text: String = EmptyTextReplacement,
    maxLines: Int = 1,
    fontDroidResolver: FontDroid.Resolver
): IntSize {
    val paragraph = Paragraph(
        paragraphIntrinsics = ParagraphIntrinsics(
            text = text,
            style = style,
            density = density,
            fontDroidResolver = fontDroidResolver
        ),
        maxLines = maxLines,
        constraints = Constraints(maxWidth = ceil(Float.POSITIVE_INFINITY).toInt()),
    )

    return IntSize(paragraph.minIntrinsicWidth.ceilToIntPx(), paragraph.height.ceilToIntPx())
}

internal const val DefaultWidthCharCount = 5
internal val EmptyTextReplacement = "H".repeat(DefaultWidthCharCount)
internal fun Float.ceilToIntPx(): Int = ceil(this).roundToInt()

fun Modifier.clickableRipple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
    onClick: () -> Unit
) = composed {
    clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
            bounded = bounded, radius = radius, color = color

        )
    )
}

fun Modifier.clickableNoRipple(
    onClick: () -> Unit
) = composed {
    clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    )
}


fun Modifier.drawColoredShadow(
    color: Color = Color.Black,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = DimApp.screenPadding * .5f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    roundedRect: Boolean = true,
    driveShadow: DriveShadow = DriveShadow.ALL
) = this.drawBehind {
    /** Convert color to Argb's int type */
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = .0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    /** Call Canvas Draw */
    this.drawIntoCanvas { canvas ->
        canvas.withSave {
            val paint = Paint()
            paint.color = Color.Transparent
            /** Call the underlying Fragment Paint Draw */
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparentColor
            /** Draw a shadow */
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            /** Draw */
            val halfHeight = this.size.height / 2
            val cornerRadius = if (roundedRect) this.size.height / 2 else borderRadius.toPx()
            when (driveShadow) {
                DriveShadow.LOWER -> canvas.drawRoundRect(
                    0f,
                    halfHeight,
                    this.size.width,
                    this.size.height,
                    cornerRadius,
                    cornerRadius,
                    paint
                )

                DriveShadow.UPPER -> canvas.drawRoundRect(
                    0f,
                    0f,
                    this.size.width,
                    halfHeight,
                    cornerRadius,
                    cornerRadius,
                    paint
                )

                DriveShadow.ALL -> canvas.drawRoundRect(
                    0f,
                    0f,
                    this.size.width,
                    this.size.height,
                    cornerRadius,
                    cornerRadius,
                    paint
                )
            }
        }
    }
}
//
//fun Modifier.drawColoredShadowRounded(
//    color: Color = Color.Black,
//    alpha: Float = 0.3f,
//    borderRadius: Dp = 0.dp,
//    shadowRadius: Dp = 10.dp,
//    offsetX: Dp = 0.dp,
//    offsetY: Dp = 0.dp,
//    roundedRect: Boolean = true,
//    driveShadow: DriveShadow = DriveShadow.ALL
//) = this.drawBehind {
//    /** Convert color to Argb's int type */
//    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = .0f).value.toLong())
//    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
//    /** Call Canvas Draw */
//    this.drawIntoCanvas {
//        val paint = Paint()
//        paint.color = Color.Transparent
//        /** Call the underlying Fragment Paint Draw */
//        val frameworkPaint = paint.asFrameworkPaint()
//        frameworkPaint.color = transparentColor
//        /** Draw a shadow */
//        frameworkPaint.setShadowLayer(
//            shadowRadius.toPx(),
//            offsetX.toPx(),
//            offsetY.toPx(),
//            shadowColor
//        )
//        /** Draw */
//        val halfHeight = this.size.height / 2
//        val cornerRadius = if (roundedRect) this.size.height / 2 else borderRadius.toPx()
//
//        val path = Path()
//        path.addRoundRect(
//            RectF(0f, 0f, this.size.width, this.size.height),
//            cornerRadius,
//            cornerRadius,
//            Path.Direction.CW
//        )
//
//        when (driveShadow) {
//            DriveShadow.LOWER -> {
//                path.addRect(
//                    RectF(0f, halfHeight, this.size.width, this.size.height),
//                    Path.Direction.CCW
//                )
//            }
//            DriveShadow.UPPER -> {
//                path.addRect(
//                    RectF(0f, 0f, this.size.width, halfHeight),
//                    Path.Direction.CCW
//                )
//            }
//
//            DriveShadow.ALL ->  it.drawPath(path, paint)
//        }
//
//
//    }
//}

enum class DriveShadow {
    LOWER,
    UPPER,
    ALL;

}
