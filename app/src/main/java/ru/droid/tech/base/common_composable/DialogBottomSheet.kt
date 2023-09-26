package ru.droid.tech.base.common_composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import kotlin.math.roundToInt

@Composable
fun DialogBottomSheet(
    onDismiss: () -> Unit,
    scrimColor: Color = Color.Black.copy(.5f),
    backgroundContent: Color = ThemeApp.colors.backgroundVariant,
    stripForPressingColor: Color = ThemeApp.colors.borderLight.copy(.5f),
    shape: Shape = shapeBottomSheet,
    bottomOffset: Dp = bottomStartOffset.dp,
    content: @Composable BoxScope.(dismiss: () -> Unit) -> Unit,
) {

    val density = LocalDensity.current
    var runDismiss by remember { mutableStateOf(false) }
    var isStartAnimation by remember { mutableStateOf(false) }
    var statusView by remember { mutableStateOf(CollapsingDialogV2.VIEW) }
    var offsetInStaticStart by remember { mutableStateOf(false) }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp / 3
    val heightOffset = remember { with(density) { screenHeightDp.dp.toPx() } }
    val triggerCollapse = remember { heightOffset * triggerRatioCollapse }

    val bottomStartOffset = remember { with(density) { bottomOffset.toPx() } }
    var offsetYDynamics by remember { mutableStateOf(bottomStartOffset) }
    var offsetYStatic by remember { mutableStateOf(bottomStartOffset) }

    val coreDismiss = {
        runDismiss = true
        isStartAnimation = false
    }

    BackPressHandler {
        coreDismiss.invoke()
    }

    LaunchedEffect(Unit) {
        isStartAnimation = true
        delay(spiderCollapsing.toLong())
        offsetYStatic = offsetYDynamics
    }
    LaunchedEffect(runDismiss) {
        if (runDismiss) {
            delay(spiderCollapsing.toLong())
            onDismiss.invoke()
        }
    }

    LaunchedEffect(offsetInStaticStart) {
        val offset = Animatable(offsetYDynamics)
        offset.animateTo(
            targetValue = offsetYStatic,
            animationSpec = tween(
                durationMillis = 100,
                easing = FastOutSlowInEasing
            )
        ) {
            offsetYDynamics = offset.value
        }
        offsetInStaticStart = false
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Popup(
            alignment = Alignment.TopCenter,
            offset = IntOffset(0, 0),
            onDismissRequest = { },
            properties = PopupProperties(
                focusable = true,
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimColor)
                    .clipToBounds()
                    .draggable(
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            if (statusView == CollapsingDialogV2.VIEW) {
                                offsetInStaticStart = true
                            }
                        },
                        state = rememberDraggableState { delta ->
                            val newOffset = offsetYDynamics + delta
                            offsetYDynamics = newOffset.coerceIn(-bottomStartOffset, heightOffset)
                            when {
                                offsetYDynamics > triggerCollapse -> {
                                    statusView = CollapsingDialogV2.DISMISS
                                    coreDismiss.invoke()
                                }

                                else                              -> {
                                    statusView = CollapsingDialogV2.VIEW
                                }
                            }
                        })
                    .clickable(
                        onClick = { coreDismiss.invoke() },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    )
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset { IntOffset(0, offsetYDynamics.roundToInt()) },
                    visible = isStartAnimation && !runDismiss,
                    enter = enterBottomNavigationBar,
                    exit = exitBottomNavigationBar
                ) {
                    Column(
                        modifier = Modifier
                            .clip(shape = shape)
                            .align(Alignment.BottomCenter)
                            .background(backgroundContent),
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(paddingLineTargetInDialog)
                                    .width(weightLineTargetInDialog)
                                    .height(heightLineTargetInDialog)
                                    .background(stripForPressingColor)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = bottomOffset)
                        ) {
                            content.invoke(this, coreDismiss)
                        }
                    }
                }
            }
        }
    }
}

private val paddingLineTargetInDialog: Dp = 16.dp
private val weightLineTargetInDialog: Dp = 24.dp
private val heightLineTargetInDialog: Dp = 4.dp
private const val spiderCollapsing = 300
private val shapeBottomSheet = RoundedCornerShape(6.dp)
private const val bottomStartOffset = 80
private const val triggerRatioCollapse: Double = 0.7
private const val spiderDelayCollapsing = 100
private val enterBottomNavigationBar = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(
        durationMillis = spiderCollapsing,
        delayMillis = spiderDelayCollapsing,
        easing = FastOutSlowInEasing
    )
)

private val exitBottomNavigationBar = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(
        durationMillis = spiderCollapsing,
        delayMillis = spiderDelayCollapsing,
        easing = FastOutSlowInEasing
    )
)

private enum class CollapsingDialogV2 {
    VIEW,
    DISMISS
}
