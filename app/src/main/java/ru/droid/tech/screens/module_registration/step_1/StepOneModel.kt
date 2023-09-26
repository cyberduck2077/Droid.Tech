package ru.droid.tech.screens.module_registration.step_1

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.data.common.models.res.TextApp
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.droid.tech.screens.module_registration.step_2.StepTwoScreen
import ru.data.common.domain.use_case.UseCaseSignIn

class StepOneModel(
    private val apiSignIn: UseCaseSignIn,
) : BaseModel() {

    private val _codeValidation = MutableStateFlow<String?>(null)
    val codeValidation = _codeValidation.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun deleteCodeValidation() {
        _codeValidation.value = null
    }

    fun goToTwoStep(enterCode: String) {
        if (_codeValidation.value != enterCode) {
            message(TextApp.errorInvalidCodeEntered)
            return
        }
        coroutineScope.launch {
            apiSignIn.postReg(
                email = email.value,
                password = password.value,
                code = enterCode,
                flowStart = ::gDLoaderStart,
                flowSuccess = { user ->
                    navigator.push(StepTwoScreen())
                },
                flowStop = {
                    deleteCodeValidation()
                    gDLoaderStop()
                },
                flowMessage = ::message
            )
        }
    }

    fun createNewEmail(email: String, password: String) = coroutineScope.launch {
        _email.value = email
        _password.value = password
        apiSignIn.postEmail(
            email = email,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                _codeValidation.value = it.code
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }
}