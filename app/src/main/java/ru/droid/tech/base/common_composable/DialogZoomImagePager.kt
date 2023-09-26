package ru.droid.tech.base.common_composable

import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.droid.tech.base.theme.LocalFixedInsets
import ru.droid.tech.base.theme.ThemeApp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogZoomImagePager(
    images: List<Any>,
    dismiss: () -> Unit,
    initPageNumb: Int = 0,
    offset: IntOffset = IntOffset(0, 0),
) {
    if (images.isNotEmpty()) {
        Popup(
            alignment = Alignment.TopCenter,
            onDismissRequest = dismiss,
            offset = offset,
            properties = PopupProperties(
                focusable = true,
                clippingEnabled = false,
            )
        ) {
            val pagerState = rememberPagerState(initialPage = initPageNumb) { images.size }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                PanelNavBackTop(
                    onClickBack = dismiss,
                    text = "${pagerState.currentPage + 1} из ${images.size}",
                    container = ThemeApp.colors.primary,
                    color = ThemeApp.colors.container
                )
                PagerApp(
                    items = images,
                    pagerState = pagerState,
                    isHaveIndicator = true,
                ) { image ->
                    var scale by remember { mutableFloatStateOf(1f) }
                    var offsetX by remember { mutableFloatStateOf(0f) }
                    var offsetY by remember { mutableFloatStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .pointerInput(pagerState.currentPage) {
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

                                    if (scale < 1f || scale > 12) {
                                        scale = 1f
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            }
                            .fillMaxSize()
                            .background(ThemeApp.colors.background)
                    ) {
                        BoxImageLoad(
                            modifier = Modifier
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offsetX,
                                    translationY = offsetY
                                )
                                .align(Alignment.Center)
                                .wrapContentSize(),
                            image = image,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

