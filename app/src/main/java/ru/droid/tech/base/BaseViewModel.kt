package ru.droid.tech.base

import android.app.Activity
import android.widget.Toast
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.logger.LogCustom.logE


abstract class BaseModel : ScreenModel {
    protected lateinit var navigator: Navigator

    fun goBackStack() {
        goBackStack(navigator)
    }

    private fun goBackStack(nav: Navigator?) {
        nav ?: kotlin.run {
            logE("Error goBackStack -> Navigator == null")
            return
        }
        if (nav.canPop) {
            nav.pop()
            return
        } else {
            goBackStack(nav.parent)
        }
    }

    fun goBackAllStack() {
        goBackAllStack(navigator)
    }

    private fun goBackAllStack(nav: Navigator?) {
        nav ?: return
        if (nav.canPop) {
            nav.popAll()
            return
        } else {
            goBackAllStack(nav.parent)
        }
    }

    fun getNavigationLevel(level: NavLevel): Navigator? {
        return getNavigationLevel(navigator, level)
    }

    private fun getNavigationLevel(nav: Navigator?, level: NavLevel): Navigator? {
        nav ?: kotlin.run {
            logE("Error goBackStack -> Navigator == null")
            return null
        }
        return if (nav.level == level.ordinal) {
            nav
        } else {
            getNavigationLevel(nav.parent, level)
        }
    }


    fun initNavigator(controller: Navigator) {
        navigator = controller
    }


    protected fun toastLong(activity: Activity, text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    protected fun message(message: String) = coroutineScope.launch {
        gDMessage(message)
        delay(1000L)
        gDMessage(null)
    }

    enum class NavLevel {
        MAIN,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH
    }
}