package ru.droid.tech.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.LogoRow
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberModel

class SplashScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<SplashScreenModel>()
        SplashScr()
        LifeScreen(onStart = { model.startApp() })
    }
}

@Composable
fun SplashScr() {
    var isAnimatedStart by remember { mutableStateOf(true) }
    val durationMillis = remember { 2000 }

    val imageAlpha: Float by animateFloatAsState(
        targetValue = if (isAnimatedStart) .6f else 1f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing,
        ), label = "SplashScr"
    )
    LaunchedEffect(Unit) {
        while (true) {
            isAnimatedStart = !isAnimatedStart
            delay(durationMillis.toLong())
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(ThemeApp.colors.primary)) {
        LogoRow(alpha = imageAlpha)
    }
}

