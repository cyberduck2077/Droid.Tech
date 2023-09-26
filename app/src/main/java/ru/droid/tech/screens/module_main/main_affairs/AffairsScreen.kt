package ru.droid.tech.screens.module_main.main_affairs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.base.common_composable.DialogBackPressExit
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.util.getQualifiedName

class AffairsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {

        DialogBackPressExit()
        Box(modifier = Modifier
            .fillMaxSize()) {
            TextTitleMedium(
                modifier = Modifier.align(Alignment.Center),
                text = "AffairsScreen")
        }
    }
}