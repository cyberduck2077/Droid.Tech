package ru.droid.tech.screens.module_main.chat_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberModel

class ChatUser(private val userId: Int) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ChatUserModel>()
        BackPressHandler(onBackPressed = model::goBackStack)
        LifeScreen(onCreate = {model.getMessage(userId)})
        ChatUserScr(
            onClickBack = model::goBackStack,
        )
    }

    @Composable
    private fun ChatUserScr(
        onClickBack: () -> Unit,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.background)
                .systemBarsPadding()
        ) {
            PanelNavBackTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = DimApp.shadowElevation)
                    .background(ThemeApp.colors.backgroundVariant),
                onClickBack = onClickBack,
                container = ThemeApp.colors.backgroundVariant,
                text = TextApp.titleChat,
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
                TextTitleMedium(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Stub")
            }

        }

    }
}