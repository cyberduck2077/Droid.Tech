package ru.droid.tech.screens.module_main.core_main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BottomNavBar
import ru.droid.tech.base.common_composable.LevelDestinationBar
import ru.droid.tech.base.theme.LocalGlobalData
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.screens.module_main.main_affairs.AffairsScreen
import ru.droid.tech.screens.module_main.main_calendar.CalendarScreen
import ru.droid.tech.screens.module_main.main_chats.ChatsScreen
import ru.droid.tech.screens.module_main.main_gifts.Gifts
import ru.droid.tech.screens.module_main.main_ribbon.Ribbon
import ru.data.common.domain.memory.gDSetScreenMain
import ru.data.common.models.local.screens.ScreensHome

class HomeMainScreen() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
        fun getScreenEnum(rote: String?) = when (rote) {
            Ribbon.keyName        -> ScreensHome.RIBBON_SCREEN
            AffairsScreen.keyName -> ScreensHome.AFFAIRS_SCREEN
            ChatsScreen.keyName    -> ScreensHome.CHATS_SCREEN
            CalendarScreen.keyName -> ScreensHome.CALENDAR_SCREEN
            Gifts.keyName          -> ScreensHome.GIFTS_SCREEN
            else                   -> ScreensHome.RIBBON_SCREEN
        }
        fun getScreen(scr: ScreensHome) = when (scr) {
            ScreensHome.RIBBON_SCREEN   -> Ribbon()
            ScreensHome.AFFAIRS_SCREEN  -> AffairsScreen()
            ScreensHome.CHATS_SCREEN    -> ChatsScreen()
            ScreensHome.CALENDAR_SCREEN -> CalendarScreen()
            ScreensHome.GIFTS_SCREEN    -> Gifts()
        }

        fun getRote(scr: ScreensHome) = when (scr) {
            ScreensHome.RIBBON_SCREEN   -> Ribbon.keyName
            ScreensHome.AFFAIRS_SCREEN  -> AffairsScreen.keyName
            ScreensHome.CHATS_SCREEN    -> ChatsScreen.keyName
            ScreensHome.CALENDAR_SCREEN -> CalendarScreen.keyName
            ScreensHome.GIFTS_SCREEN    -> Gifts.keyName
        }
    }



    @Composable
    override fun Content() {
        val screen = LocalGlobalData.current.screen
        Navigator(
            onBackPressed = { false },
            disposeBehavior = NavigatorDisposeBehavior(
                disposeNestedNavigators = true,
                disposeSteps = false
            ),
            screen = getScreen(screen)
        ) { nav ->
            LaunchedEffect(key1 = Unit, key2 = screen, block = {
                nav.replaceAll(getScreen(screen))
            })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .background(ThemeApp.colors.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .weight(1f)
                ) {
                    CurrentScreen()
                }
                BottomNavigationBar()
            }
        }
    }
}

@Composable
private fun BottomNavigationBar() {
    val navigator: Navigator? = LocalNavigator.current
    val isCheck by rememberState(navigator?.lastItem?.key) {
        LIST_BAR_HOME.any {
            it.route == navigator?.lastItem?.key
        }
    }
    BottomNavBar(
        visible = isCheck,
        isTextEnable = true,
        animation = false,
        selectedDestination = navigator?.lastItem?.key ?: "",
        listMenuBar = LIST_BAR_HOME
    ) { replyDestination ->
        val enumScr = HomeMainScreen.getScreenEnum(replyDestination.route)
        gDSetScreenMain(enumScr)
    }
}

val LIST_BAR_HOME = listOf(
    LevelDestinationBar(
        route = Ribbon.keyName,
        iconIdOn = R.raw.ic_tape,
        iconIdOff = R.raw.ic_tape,
        iconText = TextApp.titleRibbon
    ),
    LevelDestinationBar(
        route = AffairsScreen.keyName,
        iconIdOn = R.raw.ic_case,
        iconIdOff = R.raw.ic_case,
        iconText = TextApp.titleAffairs
    ),
    LevelDestinationBar(
        route = ChatsScreen.keyName,
        iconIdOn = R.raw.ic_messages,
        iconIdOff = R.raw.ic_messages,
        iconText = TextApp.titleChats
    ),
    LevelDestinationBar(
        route = CalendarScreen.keyName,
        iconIdOn = R.raw.ic_calendar,
        iconIdOff = R.raw.ic_calendar,
        iconText = TextApp.titleCalendar
    ),
    LevelDestinationBar(
        route = Gifts.keyName,
        iconIdOn = R.raw.ic_gifts,
        iconIdOff = R.raw.ic_gifts,
        iconText = TextApp.titleGifts
    ),
)

