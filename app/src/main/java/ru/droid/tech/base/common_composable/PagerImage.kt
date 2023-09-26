package ru.droid.tech.base.common_composable

import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.MediaUI
import kotlin.math.absoluteValue
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerImageWithOutDownload(
    images: List<MediaUI?>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isPagerIndicatorString: Boolean = false,
    isIndicatorOff: Boolean = false,
    isEnable: Boolean = false,
    initialPage: Int = 0,
    isPagerIndicatorAlignment: Alignment = Alignment.BottomCenter,
    activeColorIndicator: Color = ThemeApp.colors.primary,
    inactiveColorIndicator: Color = ThemeApp.colors.background,
    paddingIndicator: Dp = DimApp.indicatorPadding,
    contentScale: ContentScale = ContentScale.Fit,
    onClick: (index: Int) -> Unit = {},
    itPageIndex: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { images.size }
    var isCanScroll by rememberState { true }
    PagerApp(
        items = images,
        modifier = modifier,
        contentPadding = contentPadding,
        isHaveIndicator = isIndicatorOff,
        pagerState = pagerState,
        userScrollEnabled =isCanScroll,
        isPagerIndicatorString = isPagerIndicatorString,
        isPagerIndicatorAlignment = isPagerIndicatorAlignment,
        activeColorIndicator = activeColorIndicator,
        inactiveColorIndicator = inactiveColorIndicator,
        itPageIndex = itPageIndex
    ) { image ->

        var scale by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
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
                                isCanScroll = false
                            }
                        } while (event.changes.any { it.pressed })
                        isCanScroll = true
                        if (scale < 1f || scale > 9f) {
                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                }
                .fillMaxSize()
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
                    .wrapContentSize()
                    .fillMaxWidth()
                    .padding(bottom = paddingIndicator)
                    .clipToBounds()
                    .clickable(
                        onClick = { onClick(pagerState.currentPage) },
                        role = Role.Button,
                        enabled = isEnable,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ),
                image = image?.url,
                contentScale = contentScale,
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(DimApp.screenPadding),
            ) {
                TextBodyMedium(
                    text = image?.getDescForPhoto() ?: "",
                    color = ThemeApp.colors.container
                )
                if (image?.address != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconApp(
                            painter = rememberImageRaw(R.raw.ic_location),
                            tint = ThemeApp.colors.container
                        )
                        TextBodyMedium(
                            text = image.address ?: "",
                            color = ThemeApp.colors.container
                        )
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> PagerApp(
    items: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState() { items.size },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    indicatorPadding: PaddingValues = PaddingValues(DimApp.screenPadding),
    isPagerIndicatorString: Boolean = false,
    isHaveIndicator: Boolean = false,
    userScrollEnabled: Boolean = true,
    isPagerIndicatorAlignment: Alignment = Alignment.BottomEnd,
    activeColorIndicator: Color = ThemeApp.colors.primary,
    inactiveColorIndicator: Color = ThemeApp.colors.onPrimary,
    itPage: (T) -> Unit = {},
    itPageIndex: (Int) -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    LaunchedEffect(key1 = pagerState.currentPage, block = {
        val item = items.getOrNull(pagerState.currentPage)
        itPageIndex(pagerState.currentPage)
        if (item != null) itPage(item)
    })

    if (items.isNotEmpty()) Box(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            contentPadding = contentPadding,
            state = pagerState,

            userScrollEnabled = userScrollEnabled
        ) { page ->
            content(items[page])
        }

        if (!isPagerIndicatorString && !isHaveIndicator) HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(isPagerIndicatorAlignment)
                .padding(indicatorPadding),
            activeColor = activeColorIndicator,
            inactiveColor = inactiveColorIndicator,
            pageCount = items.size,
        )

        if (isPagerIndicatorString && !isHaveIndicator) Box(
            modifier = Modifier
                .wrapContentSize()
                .align(isPagerIndicatorAlignment)
                .padding(DimApp.screenPadding)
                .background(color = ThemeApp.colors.primary.copy(.3f), shape = CircleShape)
                .padding(DimApp.starsPadding), contentAlignment = Alignment.Center
        ) {
            TextCaption(text = "${pagerState.currentPage + 1} / ${items.size}")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pageCount: Int,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = Color.Black.copy(0.4f),
    inactiveColor: Color = activeColor.copy(1.0f),
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {

    val indicatorWidthPx = LocalDensity.current.run { indicatorWidth.roundToPx() }
    val spacingPx = LocalDensity.current.run { spacing.roundToPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)

            repeat(pageCount) {
                Box(indicatorModifier)
            }
        }

        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat()
                        )

                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(),
                        y = 0
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .then(
                    if (pageCount > 0) Modifier.background(
                        color = activeColor,
                        shape = indicatorShape,
                    )
                    else Modifier
                )
        )
    }
}