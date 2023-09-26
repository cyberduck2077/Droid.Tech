package ru.droid.tech.screens.module_authorization

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetScreenMain
import ru.data.common.models.local.maps.StepNotEnoughReg
import ru.droid.tech.screens.forgot_password.ForgotPassword
import ru.droid.tech.screens.module_main.core_main.HomeMainScreen
import ru.data.common.models.local.screens.ScreensHome
import ru.droid.tech.screens.module_registration.main.RegMainScreen
import ru.droid.tech.screens.module_registration.step_2.StepTwoScreen
import ru.data.common.domain.use_case.UseCaseSignIn
import ru.data.common.models.local.maps.UserUI

class AuthScreenModel(
    private val apiSignIn: UseCaseSignIn
) : BaseModel() {

    fun authorization(email: String, password: String) = coroutineScope.launch(Dispatchers.IO) {
        apiSignIn.postAuthorization(
            email = email,
            password = password,
            flowMessage = ::message,
            flowStart = ::gDLoaderStart,
            flowSuccess = { user->
                chooseScreen(user)
            },
            flowUnauthorized = {

            },
            flowStop = ::gDLoaderStop,
        )
    }

    private fun chooseScreen(user: UserUI?) {
        when (user?.stepRegStatus()) {
            StepNotEnoughReg.STEP_SUCCESS -> {
                goToMain()
            }
            StepNotEnoughReg.STEP_TWO_NOT_ENOUGH -> {
                goToRegDataUser()
            }
            else -> {
                goToReg()
            }
        }
    }

    private fun goToMain(){
        gDSetScreenMain(ScreensHome.RIBBON_SCREEN)
        navigator.push(HomeMainScreen())
    }


    fun goToReg() {
        navigator.push(RegMainScreen())
    }

    fun goToForgotPassword() {
        navigator.push(ForgotPassword())
    }

    private fun goToRegDataUser() {
        navigator.push(StepTwoScreen())
    }
}