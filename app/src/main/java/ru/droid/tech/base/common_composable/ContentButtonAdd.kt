package ru.droid.tech.base.common_composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.droid.tech.R
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState

@Composable
fun BoxScope.ContentButtonAdd(
    onClickAddPhoto: () -> Unit,
    onClickAddAlbum: () -> Unit,
) {
    val state = remember { MutableTransitionState(false).apply { targetState = false } }

    val des = LocalDensity.current
    var widthDpFloating by rememberState { 0.dp }
    var heightDpFloating by rememberState { 0.dp }
    val tweenMillis = remember { 500 }

    val paddingEndButton by rememberState(widthDpFloating) {
        ((widthDpFloating * 0.5f) - (DimApp.iconSizeTouchBig * 0.5f) + DimApp.screenPadding).coerceAtLeast(
            0.dp
        )
    }

    val paddingBottomForeButton: (Int) -> Dp = remember(heightDpFloating) {
        { position ->
            heightDpFloating + DimApp.screenPadding + (position * DimApp.iconSizeTouchBig.value * 0.7f).dp
        }
    }


    var rotateFloating by rememberState { 45f }
    val rotateFloatingAnimate by animateFloatAsState(
        targetValue = rotateFloating,
        animationSpec = remember { tween(durationMillis = tweenMillis) })

    var shadowElevationButton by rememberState { 1.dp }
    val shadowElevationButtonAnimate = animateDpAsState(
        targetValue = shadowElevationButton,
        animationSpec = remember(state.targetState) {
            tween(durationMillis = if (state.targetState) tweenMillis / 3 else 0)
        },
    )

    var paddingBottomAlbum by rememberState { 0.dp }
    val paddingBottomAlbumAnimate by animateDpAsState(
        targetValue = paddingBottomAlbum,
        animationSpec = remember { tween(durationMillis = tweenMillis) },
        finishedListener = {
            if (state.targetState) {
                shadowElevationButton = DimApp.shadowElevation
            }
        }
    )

    var paddingBottomPhoto by rememberState { 0.dp }
    val paddingBottomPhotoAnimate by animateDpAsState(
        targetValue = paddingBottomPhoto,
        animationSpec = remember { tween(durationMillis = tweenMillis) })

    LaunchedEffect(key1 = state.targetState, block = {
        if (state.targetState) {
            rotateFloating = 0f
            paddingBottomAlbum = paddingBottomForeButton(1)
            paddingBottomPhoto = paddingBottomForeButton(0)
        }
        else {
            rotateFloating = 45f
            paddingBottomAlbum = 0.dp
            paddingBottomPhoto = 0.dp
            shadowElevationButton = .5f.dp
        }
    })


    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visibleState = state,
        enter = fadeIn(animationSpec = remember {
            tween(
                durationMillis = tweenMillis
            )
        }),
        exit = fadeOut(animationSpec = remember {
            tween(
                durationMillis = tweenMillis
            )
        })
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = DimApp.screenPadding)
            .pointerInput(Unit) {}
            .background(ThemeApp.colors.backgroundVariant.copy(.8f)))
        BackPressHandler() {
            state.targetState = false
        }
    }


    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .systemBarsPadding()
            .padding(bottom = paddingBottomAlbumAnimate)
    ) {
        AnimatedVisibility(
            visibleState = state,
            enter = fadeIn(
                tween(durationMillis = tweenMillis, delayMillis = 0)
            ),
            exit = fadeOut(
                tween(durationMillis = tweenMillis, delayMillis = 0)
            )
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                TextButtonStyle(text = TextApp.textAlbum)
                BoxSpacer(.5f)

                Box(
                    modifier = Modifier
                        .padding(end = paddingEndButton)
                        .size(DimApp.iconSizeTouchBig)
                        .clip(CircleShape)
                        .clickableRipple { onClickAddAlbum.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    IconApp(
                        modifier = Modifier
                            .shadow(
                                elevation = shadowElevationButtonAnimate.value,
                                shape = CircleShape
                            )
                            .background(ThemeApp.colors.backgroundVariant)
                            .padding(DimApp.screenPadding * .3f),
                        tint = ThemeApp.colors.primary,
                        painter = rememberImageRaw(id = R.raw.ic_create_new_folder)
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(bottom = paddingBottomPhotoAnimate)
            .align(Alignment.BottomEnd)
            .systemBarsPadding()
    ) {
        AnimatedVisibility(
            visibleState = state,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                TextButtonStyle(text = TextApp.textPhotos)
                BoxSpacer(.5f)

                Box(
                    modifier = Modifier
                        .padding(end = paddingEndButton)
                        .size(DimApp.iconSizeTouchBig)
                        .clip(CircleShape)
                        .clickableRipple { onClickAddPhoto.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    IconApp(
                        modifier = Modifier
                            .shadow(
                                elevation = shadowElevationButtonAnimate.value,
                                shape = CircleShape
                            )
                            .background(ThemeApp.colors.backgroundVariant)
                            .padding(DimApp.screenPadding * .3f),
                        tint = ThemeApp.colors.primary,
                        painter = rememberImageRaw(id = R.raw.ic_add_photo)
                    )
                }
            }
        }
    }

    FloatingActionButtonApp(
        modifier = Modifier
            .systemBarsPadding()
            .align(Alignment.BottomEnd)
            .padding(DimApp.screenPadding)
            .onGloballyPositioned {
                widthDpFloating = with(des) { it.size.width.toDp() }
                heightDpFloating = with(des) { it.size.height.toDp() }
            },
        onClick = {
            state.targetState = !state.targetState
        }) {
        IconApp(
            modifier = Modifier.rotate(rotateFloatingAnimate),
            painter = rememberImageRaw(id = R.raw.ic_close)
        )
    }
}

@Composable
fun BoxScope.ContentButtonAddNew(
    onClickAdd: () -> Unit,
) {
    val state = remember { MutableTransitionState(false).apply { targetState = false } }

    val des = LocalDensity.current
    var widthDpFloating by rememberState { 0.dp }
    var heightDpFloating by rememberState { 0.dp }
    val tweenMillis = remember { 500 }

    var rotateFloating by rememberState { 45f }
    val rotateFloatingAnimate by animateFloatAsState(
        targetValue = rotateFloating,
        animationSpec = remember { tween(durationMillis = tweenMillis) })

    var shadowElevationButton by rememberState { 1.dp }

    LaunchedEffect(key1 = state.targetState, block = {
        if (state.targetState) {
            rotateFloating = 0f
        }
        else {
            rotateFloating = 45f
            shadowElevationButton = .5f.dp
        }
    })


    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visibleState = state,
        enter = fadeIn(animationSpec = remember {
            tween(
                durationMillis = tweenMillis
            )
        }),
        exit = fadeOut(animationSpec = remember {
            tween(
                durationMillis = tweenMillis
            )
        })
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = DimApp.screenPadding)
            .pointerInput(Unit) {}
            .background(ThemeApp.colors.backgroundVariant.copy(.8f)))
        BackPressHandler() {
            state.targetState = false
        }
    }
    FloatingActionButtonApp(
        modifier = Modifier
            .systemBarsPadding()
            .align(Alignment.BottomEnd)
            .padding(DimApp.screenPadding)
            .onGloballyPositioned {
                widthDpFloating = with(des) { it.size.width.toDp() }
                heightDpFloating = with(des) { it.size.height.toDp() }
            },
        onClick = {
            onClickAdd.invoke()
        }) {
        IconApp(
            modifier = Modifier.rotate(rotateFloatingAnimate),
            painter = rememberImageRaw(id = R.raw.ic_close)
        )
    }
}