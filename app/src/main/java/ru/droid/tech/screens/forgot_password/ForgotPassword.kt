package ru.droid.tech.screens.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextFieldOutlinesAppStr
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.screens.ScreenStepForgotPassword
import ru.data.common.models.util.FieldValidators

class ForgotPassword : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ForgotPasswordModule>()
        BackPressHandler(onBackPressed = model::goBackStack)
        val stepScreen by model.stepScreen.collectAsState()
        val codeValidation by model.codeValidation.collectAsState()
        val email by model.email.collectAsState()

        when (stepScreen) {
            ScreenStepForgotPassword.ENTER_EMAIL -> EnterEmailScr(
                onClickBack = model::goBackStack,
                emailAddress = email,
                setEmailAddress = model::setEmail,
                onClickNextStep = model::onEmailSend

            )

            ScreenStepForgotPassword.ENTER_CODE -> EnterCodeScr(
                onClickBack = model::goToEnterEmail,
                emailAddress = email,
                code = codeValidation,
                onClickNextStep = model::onCodeEnter
            )

            ScreenStepForgotPassword.ENTER_PASSWORD -> EnterPasswordScr(
                onClickBack = model::goToEnterCode,
                onClickNextStep = model::onPasswordEnter
            )
        }
    }

    @Composable
    private fun EnterEmailScr(
        emailAddress: String,
        setEmailAddress: (String) -> Unit,
        onClickBack: () -> Unit,
        onClickNextStep: () -> Unit,
    ) {
        val focusRegister = remember { FocusRequester() }
        var isValidateEmailAddress by rememberState { true }
        val focusManager = LocalFocusManager.current
        val emailSetText: (String) -> Unit = remember(emailAddress) {
            {
                setEmailAddress.invoke(it)
                isValidateEmailAddress = emailAddress.isNotBlank()
                        && FieldValidators.isValidEmail(it)
            }
        }
        val nextStep = remember(emailAddress, isValidateEmailAddress) {
            {
                if (isValidateEmailAddress) onClickNextStep.invoke()
            }
        }

        LaunchedEffect(key1 = Unit, block = {
            focusRegister.requestFocus()
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.backgroundVariant)
                .systemBarsPadding()
                .imePadding()
        ) {
            PanelNavBackTop(onClickBack = onClickBack)
            Column(modifier = Modifier
                .padding(horizontal = DimApp.screenPadding)) {
                TextButtonStyle(text = TextApp.formatStepFrom(1, 3))
                BoxSpacer(0.5f)
                TextTitleLarge(text = TextApp.textEnterYourEmail)
                BoxSpacer()
                TextBodyLarge(text = TextApp.textEnterYourEmailDescription)
                BoxSpacer(2f)
                TextBodyMedium(text = TextApp.textEmailAddress)
                BoxSpacer(0.5f)
                TextFieldOutlinesAppStr(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRegister),
                    value = emailAddress,
                    onValueChange = emailSetText,
                    singleLine = true,
                    isError = !isValidateEmailAddress,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Send
                    ),
                    supportingText = {
                        if (!isValidateEmailAddress) {
                            Text(text = TextApp.textEmailNoValide)
                        }

                    },
                    keyboardActions = KeyboardActions(
                        onSend = {
                            focusManager.clearFocus()
                            if (isValidateEmailAddress) {
                                nextStep.invoke()
                            }
                        }
                    ),
                )
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = isValidateEmailAddress && emailAddress.isNotBlank(),
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }
    }

    @Composable
    private fun EnterCodeScr(
        emailAddress: String,
        code: String,
        onClickBack: () -> Unit,
        onClickNextStep: (String) -> Unit,
    ) {
        var enterCode by rememberState { TextFieldValue() }
        val focusRegister = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        LaunchedEffect(key1 = enterCode, block = {
            if (enterCode.text.isNotBlank() && code == enterCode.text) {
                onClickNextStep.invoke(enterCode.text)
            }
        })
        LaunchedEffect(key1 = Unit, block = {
            focusRegister.requestFocus()
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.backgroundVariant)
                .systemBarsPadding()
                .imePadding()
        ) {
            PanelNavBackTop(onClickBack = onClickBack)
            Column(modifier = Modifier
                .padding(horizontal = DimApp.screenPadding)) {
                TextButtonStyle(
                    text = TextApp.formatStepFrom(2, 3)
                )
                BoxSpacer(0.5f)
                TextTitleLarge(text = TextApp.textEnterConfirmationCode)
                BoxSpacer()
                TextBodyLarge(text = TextApp.formatConfirmationCodeSentYourEmail(emailAddress))
                BoxSpacer(2f)
                TextBodyMedium(text = TextApp.textConfirmationCode)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRegister),
                    value = enterCode,
                    onValueChange = { enterCode = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            focusManager.clearFocus()
                            if (enterCode.text.isNotBlank()) {
                                onClickNextStep.invoke(enterCode.text)
                            }
                        }
                    ),
                )
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = enterCode.text.isNotBlank(),
                    onClick = { onClickNextStep.invoke(enterCode.text) },
                    text = TextApp.titleNext
                )
            }
        }
    }


    @Composable
    private fun EnterPasswordScr(
        onClickBack: () -> Unit,
        onClickNextStep: (String) -> Unit,
    ) {
        var passwordEnter by rememberState() { TextFieldValue() }
        var passwordTwoEnter by rememberState() { TextFieldValue() }

        var isPasswordsRegularValidate by rememberState { true }
        var passwordsSimilar by rememberState { true }

        val focusManager = LocalFocusManager.current
        var passwordVisible by rememberState { false }

        val passwordsSimilarValidator: () -> Unit = remember(passwordTwoEnter) {
            {
                passwordsSimilar = passwordEnter.text == passwordTwoEnter.text
            }
        }
        val passwordsRegularValidator: () -> Unit = remember(passwordEnter) {
            {
                isPasswordsRegularValidate = FieldValidators.isValidPassword(passwordEnter.text)
            }
        }

        val checkData: Boolean =
            remember(passwordEnter, passwordTwoEnter) {
                passwordEnter.text.isNotBlank()
                        && passwordTwoEnter.text.isNotBlank()
                        && passwordsSimilar
                        && isPasswordsRegularValidate
            }

        val nextStep = remember(checkData) {
            { if (checkData) onClickNextStep.invoke(passwordEnter.text) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeApp.colors.backgroundVariant)
                .systemBarsPadding()
                .imePadding()
        ) {
            PanelNavBackTop(onClickBack = onClickBack)

            Column(modifier = Modifier
                .padding(horizontal = DimApp.screenPadding)) {
                TextButtonStyle(text = TextApp.formatStepFrom(3, 3))
                BoxSpacer(0.5f)

                TextBodyMedium(text = TextApp.textPassword)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordEnter,
                    onValueChange = {
                        passwordEnter = it
                        passwordsRegularValidator.invoke()
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    isError = !isPasswordsRegularValidate,
                    supportingText = {
                        if (!isPasswordsRegularValidate) {
                            Text(text = TextApp.textPasswordNoRegular)
                        }
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) {
                            rememberImageRaw(id = R.raw.ic_visibility)
                        } else {
                            rememberImageRaw(id = R.raw.ic_visibility_off)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            IconApp(painter = image)
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )
                BoxSpacer()
                BoxSpacer()
                TextBodyMedium(text = TextApp.textPasswordTwo)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordTwoEnter,
                    onValueChange = {
                        passwordTwoEnter = it
                        passwordsSimilarValidator.invoke()
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Send
                    ),
                    isError = !passwordsSimilar,
                    supportingText = {
                        if (!passwordsSimilar) {
                            Text(text = TextApp.textPasswordNoSimilar)
                        }
                    },
                    trailingIcon = {
                        val image = if (passwordVisible)
                            rememberImageRaw(id = R.raw.ic_visibility)
                        else rememberImageRaw(id = R.raw.ic_visibility_off)
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            IconApp(painter = image)
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onSend = {
                            focusManager.clearFocus()
                            nextStep.invoke()
                        }
                    ),
                )
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }

    }
}