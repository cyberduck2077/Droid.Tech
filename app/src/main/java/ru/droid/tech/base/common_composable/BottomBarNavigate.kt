package ru.droid.tech.base.common_composable

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import kotlin.math.roundToInt

@Composable
fun BottomNavBar(
    visible: Boolean = true,
    isTextEnable: Boolean = false,
    animation: Boolean = true,
    selectedDestination: String,
    isColorFilterOn: Boolean = true,
    listMenuBar: List<LevelDestinationBar>,
    onClick: (LevelDestinationBar) -> Unit = {}
) {

    AnimatedVisibility(visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = DurationNav, easing = LinearEasing)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = DurationNav, easing = LinearEasing))) {

        NavBottomBar(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars),
            topLineColor = ThemeApp.colors.borderLight.copy(.0f),
            containerColor = ThemeApp.colors.background,
            contentColors = ContentNavColors(
                contentSelectColor = ThemeApp.colors.primary,
                contentUnselectColor = ThemeApp.colors.onBackground),
            textStyleContent = ThemeApp.typography.labelSmall,
            horizontalPadding = screenPadding,
            height = navBottomBarHeight,
        ) {
            listMenuBar.forEach { replyDestination ->
                val isEnable = selectedDestination == replyDestination.route
                NavBottomBarItem(
                    selected = isEnable,
                    onClick = { onClick(replyDestination) },
                    animation = animation,
                    labelText = if (isTextEnable) replyDestination.iconText else null,
                    isColorFilterOn = isColorFilterOn,
                    paintIcon = replyDestination.getCheckIcon(isCheck = isEnable))
            }
        }
    }
}

@Composable
fun NavBottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColors: ContentNavColors,
    textStyleContent: TextStyle,
    topLineColor: Color,
    horizontalPadding: Dp,
    height: Dp,
    content: @Composable RowScope.() -> Unit
) {

    val windowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    CompositionLocalProvider(
        LocalContentNavColors provides contentColors,
        LocalContentNavTextStyle provides textStyleContent) {
        Box(
            modifier = modifier
                .shadow(elevation = DimApp.screenPadding, )
                .background(color = containerColor)
                .semantics(mergeDescendants = false) {}
                .pointerInput(Unit) {},
            propagateMinConstraints = true
        ) {
            Column {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(lineHeight)
                    .background(topLineColor))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .windowInsetsPadding(windowInsets)
                        .height(height)
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(NavigationBarItemHorizontalPadding),
                    content = content
                )
            }

        }
    }
}

@Composable
fun RowScope.NavBottomBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    paintIcon: Painter,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    animation: Boolean = true,
    isColorFilterOn: Boolean = true,
    labelText: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false,radius = navBottomBarHeight * 0.5f ),
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {


        val animationProgress: Float by if (animation) {
            animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                animationSpec = tween(ItemAnimationDurationMillis)
            )
        } else rememberState { 1f }

        NavBottomBarItemBaselineLayout(
            selected = selected,
            paint = paintIcon,
            isColorFilterOn = isColorFilterOn,
            labelText = labelText,
            animationProgress = animationProgress
        )
    }
}

@Composable
private fun NavBottomBarItemBaselineLayout(
    selected: Boolean,
    paint: Painter,
    isColorFilterOn: Boolean = true,
    labelText: String?,
    animationProgress: Float
) {

    val colorContent = if (selected) NavigateCurrent.colors.contentSelectColor else
        NavigateCurrent.colors.contentUnselectColor

    Layout({
        Box(modifier = Modifier.layoutId(IconLayoutIdTag)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(sizeIconStandard)
                        .toolingGraphicsLayer()
                        .paint(paint,
                            colorFilter = if (isColorFilterOn) ColorFilter.tint(colorContent) else null,
                            contentScale = ContentScale.Fit)
                )
            }
        }
        if (labelText != null) {
            Box(
                Modifier
                    .layoutId(LabelLayoutIdTag)
                    .padding(horizontal = NavigationBarItemHorizontalPadding / 2)
            ) {

                val textStyleInit = NavigateCurrent.textStyle
                val textStyle = remember { mutableStateOf(textStyleInit) }
                val readyToDraw = remember { mutableStateOf(false) }
                Text(
                    style = textStyle.value,
                    modifier = Modifier.drawWithContent {
                        if (readyToDraw.value) drawContent()
                    },
                    softWrap = false,
                    color = colorContent,
                    text = labelText,
                    onTextLayout = { textLayoutResult ->
                        if (textLayoutResult.didOverflowWidth) {
                            textStyle.value = textStyle.value.copy(
                                fontSize = textStyle.value.fontSize * 0.9)
                        } else {
                            readyToDraw.value = true
                        }
                    })

            }
        }
    }) { measurables, constraints ->
        val iconPlaceable =
            measurables.first { it.layoutId == IconLayoutIdTag }.measure(constraints)

        val labelPlaceable =
            labelText?.let {
                measurables
                    .first { it.layoutId == LabelLayoutIdTag }
                    .measure(
                        // Measure with loose constraints for height as we don't want the label to
                        // take up more space than it needs
                        constraints.copy(minHeight = 0)
                    )
            }

        if (labelText == null) {
            placeIcon(
                iconPlaceable,
                constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                constraints,
                animationProgress
            )
        }
    }
}

private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    constraints: Constraints
): MeasureResult {
    val width = constraints.maxWidth
    val height = constraints.maxHeight

    val iconX = (width - iconPlaceable.width) / 2
    val iconY = (height - iconPlaceable.height) / 2

    return layout(width, height) {

        iconPlaceable.placeRelative(iconX, iconY)

    }
}

private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    constraints: Constraints,
    animationProgress: Float,
): MeasureResult {
    val height = constraints.maxHeight

    val labelY = height - labelPlaceable.height - NavigationBarItemVerticalPadding.roundToPx()
    val selectedIconY = NavigationBarItemVerticalPadding.roundToPx()

    val unselectedIconY = (height - iconPlaceable.height) / 2
    val iconDistance = unselectedIconY - selectedIconY

    val offset = (iconDistance * (1 - animationProgress)).roundToInt()
    val containerWidth = constraints.maxWidth

    val labelX = (containerWidth - labelPlaceable.width) / 2
    val iconX = (containerWidth - iconPlaceable.width) / 2


    return layout(containerWidth, height) {

        if (animationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
    }
}

private const val IconLayoutIdTag: String = "icon"
private const val LabelLayoutIdTag: String = "label"

private val sizeIconStandard: Dp = 24.dp
private val lineHeight: Dp = 1.dp
private val screenPadding: Dp = 16.dp
private const val DurationNav: Int = 200
private val navBottomBarHeight: Dp = 80.dp

private const val ItemAnimationDurationMillis: Int = 200
internal val NavigationBarItemHorizontalPadding: Dp = 8.dp
internal val NavigationBarItemVerticalPadding: Dp = 16.dp

private val LocalContentNavColors = compositionLocalOf {
    ContentNavColors(
        contentSelectColor = Color.Black,
        contentUnselectColor = Color.Black.copy(.5F))
}

private val LocalContentNavTextStyle = compositionLocalOf { TextStyle() }
private val NavBottomBarItemShape = RoundedCornerShape(10)
data class ContentNavColors(
    val contentSelectColor: Color,
    val contentUnselectColor: Color
)

object NavigateCurrent {
    val colors: ContentNavColors
        @Composable
        get() = LocalContentNavColors.current
    val textStyle: TextStyle
        @Composable
        get() = LocalContentNavTextStyle.current
}

data class LevelDestinationBar(
    val route: String,
    @RawRes val iconIdOn: Int,
    @RawRes val iconIdOff: Int,
    val iconText: String
) {
    @Composable
    fun getCheckIcon(isCheck: Boolean) = if (isCheck) rememberImageRaw(id = this.iconIdOn) else
        rememberImageRaw(id = this.iconIdOff)


}