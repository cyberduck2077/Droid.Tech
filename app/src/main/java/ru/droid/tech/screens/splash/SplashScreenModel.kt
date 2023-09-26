package ru.droid.tech.screens.splash

import android.content.Context
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.droid.tech.R
import ru.droid.tech.base.BaseModel
import ru.droid.tech.base.util.readJsonFromRaw
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.core_main.HomeMainScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetScreenMain
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.models.local.maps.StepNotEnoughReg
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.screens.ScreensHome
import ru.data.common.models.logger.LogCustom.logE
import ru.data.common.models.logger.LogCustom.logI

class SplashScreenModel(
    private val apiUser: UseCaseUser,
    private val apiLocations: UseCaseLocations,
    private val context: Context
) : BaseModel() {

    companion object {
        private const val delayMillis = 1200L
    }

    fun startApp() = coroutineScope.launch {
        gDLoaderStart()
        val firstJob = launch {
            apiLocations.initDdCities { readJsonFromRaw(context, R.raw.cities) }
        }
        firstJob.join()
        gDLoaderStop()
        apiUser.getMe(
            flowStart = {},
            flowSuccess = ::chooseScreen,
            flowStop = {},
            flowMessage = {},
            flowUnauthorized = {
                chooseScreen(null)
            },
            flowError = {
                chooseScreen(null)
            }
        )
    }

    private fun chooseScreen(user: UserUI?) {
        when (user?.stepRegStatus()) {
            StepNotEnoughReg.STEP_SUCCESS -> {
                goToMain()
            }

            else                          -> goToAuth()
        }
    }

    private fun goToAuth() = coroutineScope.launch {
        delay(delayMillis)
        logE("goToAuth")
        navigator.push(AuthScreen())
    }

    private fun goToMain() = coroutineScope.launch {
        delay(delayMillis)
        logE("goToMain")
        gDSetScreenMain(ScreensHome.RIBBON_SCREEN)
        navigator.push(HomeMainScreen())
    }
}


