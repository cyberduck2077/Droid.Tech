package ru.droid.tech.screens.module_registration.step_1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.HyperlinkText
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.FieldValidators

class StepOneScreen() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<StepOneModel>()
        BackPressHandler(onBackPressed = model::goBackStack)
        val codeValidation by model.codeValidation.collectAsState()
        val email by model.email.collectAsState()
        val password by model.password.collectAsState()

        codeValidation?.let { code ->
            ValidationCode(
                onClickBack = model::deleteCodeValidation,
                onClickNextStep = model::goToTwoStep,
                code = code,
                email = email,
            )
        } ?: run {
            StepOneScr(
                onClickBack = model::goBackStack,
                onClickCreateNewEmail = model::createNewEmail,
                password = password,
                email = email,
            )
        }
    }
}

@Composable
private fun ValidationCode(
    onClickBack: () -> Unit,
    onClickNextStep: (enterCode: String) -> Unit,
    code: String,
    email: String,
) {

    val enterCode = rememberState { TextFieldValue() }
    val buttonOn = remember(enterCode) {
        enterCode.value.text.isNotBlank()
    }

    val nextStep = remember(buttonOn) {
        {
            if (enterCode.value.text.isNotBlank()) onClickNextStep(enterCode.value.text)
        }
    }

    LaunchedEffect(key1 = enterCode.value, block = {
        if (code == enterCode.value.text) {
            onClickNextStep.invoke(enterCode.value.text)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnValidationEmail(
                enterCode = enterCode,
                onClickNextStep = nextStep,
                email = email,
                onClickBack = onClickBack,
            )
            Box(modifier = Modifier.weight(1f))
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = buttonOn,
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }
    }
}

@Composable
private fun StepOneScr(
    onClickBack: () -> Unit,
    onClickCreateNewEmail: (email: String, password: String) -> Unit,
    password: String,
    email: String,
) {

    val emailAddress = rememberState(email) { TextFieldValue(email) }
    val passwordEnter = rememberState(password) { TextFieldValue(password) }
    val passwordTwoEnter = rememberState(password) { TextFieldValue(password) }

    var isEmailAddressValidate by rememberState { true }
    var isPasswordsRegularValidate by rememberState { true }
    var passwordsSimilar by rememberState { true }

    val emailValidator: () -> Unit = remember(emailAddress) {
        {
            isEmailAddressValidate = emailAddress.value.text.isBlank() || FieldValidators.isValidEmail(
                emailAddress.value.text
            )
        }
    }

    val passwordsSimilarValidator: () -> Unit = remember(passwordTwoEnter.value) {
        {
            passwordsSimilar = passwordEnter.value.text == passwordTwoEnter.value.text
        }
    }
    val passwordsRegularValidator: () -> Unit = remember(passwordEnter.value) {
        {
            isPasswordsRegularValidate = FieldValidators.isValidPassword(passwordEnter.value.text)
        }
    }

    val checkData: Boolean =
        remember(emailAddress.value, passwordEnter.value, passwordTwoEnter.value) {
            emailAddress.value.text.isNotBlank()
                    && passwordEnter.value.text.isNotBlank()
                    && passwordTwoEnter.value.text.isNotBlank()
                    && isEmailAddressValidate
                    && passwordsSimilar
                    && isPasswordsRegularValidate
        }

    val nextStep = remember(checkData) {
        { if (checkData) onClickCreateNewEmail(emailAddress.value.text, passwordEnter.value.text) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        TextButtonStyle(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
            text = TextApp.formatStepFrom(1, 4)
        )
        BoxSpacer(0.5f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentOne(
                emailAddress = emailAddress,
                password = passwordEnter,
                isEmailAddressValidate = isEmailAddressValidate,
                isPasswordsRegularValidate = isPasswordsRegularValidate,
                passwordsSimilar = passwordsSimilar,
                passwordSimilar = passwordTwoEnter,
                onClickNextStep = nextStep,
                onClickToComeIn = onClickBack,
                emailValidator = emailValidator,
                passwordsSimilarValidator = passwordsSimilarValidator,
                passwordsRegularValidator = passwordsRegularValidator,
            )
            Box(modifier = Modifier.weight(1f))
            PanelBottom(
                modifier = Modifier.fillMaxWidth()
            ) {
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

@Composable
private fun ColumnScope.ColumnContentOne(
    emailAddress: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    passwordSimilar: MutableState<TextFieldValue>,
    isEmailAddressValidate: Boolean,
    isPasswordsRegularValidate: Boolean,
    passwordsSimilar: Boolean,
    onClickNextStep: () -> Unit,
    emailValidator: () -> Unit,
    onClickToComeIn: () -> Unit,
    passwordsSimilarValidator: () -> Unit,
    passwordsRegularValidator: () -> Unit,
) {

    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberState { false }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        BoxSpacer()
        TextBodyLarge(text = TextApp.textRegisterWithEmail)
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textEmailAddress)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = emailAddress.value,
            onValueChange = {
                emailAddress.value = it
                emailValidator.invoke()
            },
            singleLine = true,
            isError = !isEmailAddressValidate,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            supportingText = {
                if (!isEmailAddressValidate) {
                    Text(text = TextApp.textEmailNoValide)
                }

            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPassword)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = password.value,
            onValueChange = {
                password.value = it
                passwordsRegularValidator.invoke()
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
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
                onSend = {
                    focusManager.clearFocus()
                    onClickNextStep.invoke()
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPasswordTwo)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = passwordSimilar.value,
            onValueChange = {
                passwordSimilar.value = it
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
                    onClickNextStep.invoke()
                }
            ),
        )
        BoxSpacer()
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToComeIn.invoke()
        }
    }
}

@Composable
private fun ColumnScope.ColumnValidationEmail(
    enterCode: MutableState<TextFieldValue>,
    email: String,
    onClickNextStep: () -> Unit,
    onClickBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textWelcomeInDroidTech)
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatConfirmationCodeSentYourEmail(email),
            hyperLinks = listOf(email)
        ) { item, index -> }
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textMailConfirmationCode)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = enterCode.value,
            onValueChange = { enterCode.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    focusManager.clearFocus()
                    if (enterCode.value.text.isNotBlank()) {
                        onClickNextStep.invoke()
                    }
                }
            ),
        )
        BoxSpacer()
        TextButtonApp(
            onClick = onClickBack,
            contentPadding = PaddingValues(2.dp),
            text = TextApp.textChangeEmailAddress
        )
    }
}

@Preview
@Composable
private fun StepOneScrTest() {
    TechTheme {
        StepOneScr(
            onClickBack = {},
            onClickCreateNewEmail = { s: String, s1: String -> },
            password = "",
            email = ""
        )
    }
}