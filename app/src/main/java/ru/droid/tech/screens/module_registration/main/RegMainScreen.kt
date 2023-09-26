package ru.droid.tech.screens.module_registration.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.screens.module_registration.step_1.StepOneScreen
import ru.droid.tech.screens.splash.SplashScr

class RegMainScreen(
) : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        Navigator(
            onBackPressed = { false },
            screen = StepOneScreen()
        )
    }
}