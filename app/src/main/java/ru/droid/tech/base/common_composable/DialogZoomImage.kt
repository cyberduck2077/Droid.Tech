package ru.droid.tech.base.common_composable

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp


@Composable
fun DialogZoomImage(
    image: String,
    dismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(0, 0),
        onDismissRequest = dismiss,
        properties = PopupProperties(focusable = true)
    ) {
        val context = LocalContext.current
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context).data(image).crossfade(true).build()
        )
        val brush = Brush.radialGradient(
            colorStops = arrayOf(
                Pair(0.5F, ThemeApp.colors.onBackground),
                Pair(0.6F, ThemeApp.colors.background.copy(0.0F))
            )
        )

        var scale by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    awaitEachGesture {
                        MotionEvent.ACTION_UP
                        do {
                            val event = awaitPointerEvent()
                            if (event.calculateCentroidSize() > 0) {
                                scale *= event.calculateZoom()
                                val offset = event.calculatePan()
                                offsetX += offset.x
                                offsetY += offset.y
                            }
                        } while (event.changes.any { it.pressed })

                        if (scale < 0.2 || scale > 12) {
                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                }
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .systemBarsPadding()
        ) {

            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .align(Alignment.Center)
                    .wrapContentSize()
                    .paint(
                        painter = painter,
                        contentScale = ContentScale.FillWidth
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(DimApp.iconSizeTouchStandard)
                    .background(brush = brush)
                    .clip(CircleShape)
                    .clickable(
                        onClick = dismiss,
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(DimApp.iconSizeStandard)
                        .paint(
                            painter = rememberVectorPainter(image = Icons.Filled.Close),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(ThemeApp.colors.background)
                        )
                )
            }
        }
    }
}

