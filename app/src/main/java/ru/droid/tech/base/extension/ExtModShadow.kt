package ru.droid.tech.base.extension

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp


fun Modifier.softLayerShadow(
    radius: Dp = ShadowsPlusDefaults.ShadowRadius,
    color: Color = ShadowsPlusDefaults.ShadowColor,
    shape: Shape = ShadowsPlusDefaults.ShadowShape,
    spread: Dp = ShadowsPlusDefaults.ShadowSpread,
    offset: DpOffset = ShadowsPlusDefaults.ShadowOffset,
): Modifier = this.drawBehind {
    val radiusPx = radius.toPx()
    val isRadiusValid = radiusPx > 0.0F

    val paint = Paint().apply {
        this.color = if (isRadiusValid) {
            Color.Transparent
        } else {
            color
        }

        asFrameworkPaint().apply {
            isDither = true
            isAntiAlias = true

            if (isRadiusValid) {
                setShadowLayer(
                    radiusPx,
                    offset.x.toPx(),
                    offset.y.toPx(),
                    color.toArgb()
                )
            }
        }
    }

    drawIntoCanvas { canvas ->
        canvas.withSave {
            if (isRadiusValid.not()) {
                canvas.translate(
                    dx = offset.x.toPx(),
                    dy = offset.y.toPx()
                )
            }

            if (spread.value != 0.0F) {
                canvas.scale(
                    sx = spreadScale(
                        spread = spread.toPx(),
                        size = size.width
                    ),
                    sy = spreadScale(
                        spread = spread.toPx(),
                        size = size.height
                    ),
                    pivotX = center.x,
                    pivotY = center.y
                )
            }

            val outline = shape.createOutline(
                size = size,
                layoutDirection = layoutDirection,
                density = this
            )


            canvas.drawOutline(
                outline = outline,
                paint = paint
            )

        }
    }
}


object ShadowsPlusDefaults {

    /** Default shadow radius. */
    val ShadowRadius = 8.dp

    /** Default shadow color. */
    val ShadowColor = Color.Black.copy(alpha = 0.23F)

    /** Default shadow shape. */
    val ShadowShape = RectangleShape

    /** Default shadow spread. */
    val ShadowSpread = 0.dp

    /** Default shadow offset. */
    val ShadowOffset = DpOffset(
        x = 0.dp,
        y = 2.dp
    )
}


internal fun spreadScale(
    spread: Float,
    size: Float
): Float = 1.0F + ((spread / size) * 2.0F)