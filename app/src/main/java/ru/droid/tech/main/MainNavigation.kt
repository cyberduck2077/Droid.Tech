package ru.droid.tech.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.droid.tech.base.common_composable.LoadBarWithTimerClose
import ru.droid.tech.base.extension.clickableNoRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.LocalGlobalData
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.screens.splash.SplashScreen

@Composable
fun MainNavigation() {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val globalData = LocalGlobalData.current
    LaunchedEffect(globalData.messageSnack) {
        scope.launch {
            val text = globalData.messageSnack.value
            if (!text.isNullOrEmpty()) {
                snackBarHostState.showSnackbar(
                    message = text,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
    ) {
        Navigator(
            onBackPressed = { false },
            screen = SplashScreen()
        )
//        { nav ->
//            SlideTransition(nav)
//        }

        LoadBarWithTimerClose(isView = globalData.isLoad, isInfinity = globalData.isInfinity)

        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = DimApp.heightNavigationBottomBar)
                .imePadding(),
            hostState = snackBarHostState
        ) { snackBarData ->
            LaunchedEffect(key1 = null, block = {
                delay(2000)
                snackBarData.dismiss()
            })
            Snackbar(
                modifier = Modifier.clickableNoRipple {
                    snackBarData.dismiss()
                },
                snackbarData = snackBarData,
                containerColor = ThemeApp.colors.onBackgroundVariant,
                contentColor = ThemeApp.colors.backgroundVariant,
            )
        }
    }
}