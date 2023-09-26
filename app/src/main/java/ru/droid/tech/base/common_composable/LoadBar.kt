package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun LoadBarWithTimerClose(
    isView: Boolean,
    modifier: Modifier = Modifier,
    isInfinity: Boolean = false
) {
    val stateIsView = remember(isView) { mutableStateOf(isView) }
    if (stateIsView.value) {
        if (!isInfinity)
            LaunchedEffect(Unit) {
                delay(20_000)
                stateIsView.value = false
            }

        Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(20))
                        .background(ThemeApp.colors.background)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(DimApp.screenPadding),
                        color = ThemeApp.colors.primary
                    )
                }
            }
        }
    }
}