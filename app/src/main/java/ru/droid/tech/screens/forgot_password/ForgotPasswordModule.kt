package ru.droid.tech.screens.forgot_password

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.data.common.models.res.TextApp
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCaseSignIn
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.models.local.screens.ScreenStepForgotPassword

class ForgotPasswordModule(
    private val apiUsers: UseCaseUsers,
    private val apiSignIn: UseCaseSignIn,
    private val apiUser: UseCaseUser,
) : BaseModel() {

    private val _stepScreen = MutableStateFlow(ScreenStepForgotPassword.ENTER_EMAIL)
    val stepScreen = _stepScreen.asStateFlow()

    private val _codeValidation = MutableStateFlow("")
    val codeValidation = _codeValidation.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val token = MutableStateFlow("")

    fun setEmail(email: String) {
        _email.update { email }
    }

    fun onEmailSend() = coroutineScope.launch(Dispatchers.IO) {
        val isExist = apiUsers.getUserExistsEmail(
            email = email.value,
            flowStart = ::gDLoaderStart,
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
        if (!isExist) {
            message(TextApp.textMailNotRegistered)
            goBackAllStack()
            return@launch
        }
        apiSignIn.postEmail(
            email = email.value,
            flowStart = ::gDLoaderStart,
            flowStop = ::gDLoaderStop,
            flowSuccess = {
                it.code?.let { code ->
                    _codeValidation.value = code
                    goToEnterCode()
                }
            },
            flowMessage = ::message)
    }

    fun onCodeEnter(enterCode: String) = coroutineScope.launch(Dispatchers.IO) {
        if (enterCode.isEmpty() && _codeValidation.value != enterCode) {
            message(TextApp.errorInvalidCodeEntered)
            return@launch
        }
        apiSignIn.postRegApi(
            email = email.value,
            code = enterCode,
            flowStart = ::gDLoaderStart,
            flowSuccess = { response ->
                token.update { response }
                goToEnterPassword()
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun onPasswordEnter(password: String) = coroutineScope.launch(Dispatchers.IO) {
        apiUser.setTokenUser(token.value)
        apiUser.putPassword(
            password = password,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                apiUser.setTokenUser("")
                goBackAllStack()
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun goToEnterEmail() {
        _stepScreen.update { ScreenStepForgotPassword.ENTER_EMAIL }
    }

    fun goToEnterCode() {
        _stepScreen.update { ScreenStepForgotPassword.ENTER_CODE }
    }

    private fun goToEnterPassword() {
        _stepScreen.update { ScreenStepForgotPassword.ENTER_PASSWORD }
    }


}

