package ru.droid.tech.screens.module_main.profile_redaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogDataPickerddmmyyy
import ru.droid.tech.base.common_composable.DialogGenderItem
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.millDateDDMMYYYYg

class ProfileRedactionPersonalDataScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)

        ProfileRedactionPersonalDataScr(
            onClickBack = model::goBackStack,
            firstName = userData.firstName,
            lastName = userData.lastName,
            patronymic = userData.patronymic,
            maidenName = userData.maidenName,
            gender = userData.gender,
            birthdateInMillis = userData.birthdate,
            onClickSave = model::updateMyPersonData
        )
    }
}

@Composable
private fun ProfileRedactionPersonalDataScr(
    onClickBack: () -> Unit,
    onClickSave: (
        firstName: String?,
        lastName: String?,
        patronymic: String?,
        maidenName: String?,
        gender: Gender?,
        birthdateInMillis: Long?,
    ) -> Unit,
    firstName: String?,
    lastName: String?,
    patronymic: String?,
    maidenName: String?,
    gender: Gender?,
    birthdateInMillis: Long?,
) {

    val firstNameEnter = rememberState(firstName) { TextFieldValue(firstName ?: "") }
    val lastNameEnter = rememberState(lastName) { TextFieldValue(lastName ?: "") }
    val patronymicEnter = rememberState(patronymic) { TextFieldValue(patronymic ?: "") }
    val maidenNameEnter = rememberState(maidenName) { TextFieldValue(maidenName ?: "") }
    val genderEnter = rememberState(gender) { gender }
    val birthdateEnter = rememberState(birthdateInMillis) { birthdateInMillis }
    var focusGender by rememberState { false }
    var focusBirthDay by rememberState { false }
    val focusManager = LocalFocusManager.current

    val checkData: Boolean = remember(
        firstNameEnter.value,
        lastNameEnter.value,
        genderEnter.value,
        birthdateEnter.value,
        patronymicEnter.value,
        maidenNameEnter.value,

        ) {
        val isDefaultData = (firstNameEnter.value.text.isNotEmpty() &&
                lastNameEnter.value.text.isNotEmpty() &&
                genderEnter.value != null &&
                birthdateEnter.value != null)
        val isChange = (firstNameEnter.value.text != firstName ||
                lastNameEnter.value.text != lastName ||
                patronymicEnter.value.text != patronymic ||
                maidenNameEnter.value.text != maidenName ||
                genderEnter.value != gender ||
                birthdateEnter.value != birthdateInMillis)
        isDefaultData && isChange
    }

    val onSave = remember(checkData) {
        {
            if (checkData) onClickSave.invoke(
                firstNameEnter.value.text,
                lastNameEnter.value.text,
                patronymicEnter.value.text,
                maidenNameEnter.value.text,
                genderEnter.value,
                birthdateEnter.value,
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(
            modifier = Modifier.shadow(DimApp.shadowElevation),
            onClickBack = onClickBack,
            text = TextApp.titlePersonalData
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnPersonalData(
                firstNameEnter = firstNameEnter,
                lastNameEnter = lastNameEnter,
                patronymicEnter = patronymicEnter,
                maidenNameEnter = maidenNameEnter,
                genderEnter = genderEnter,
                birthdateEnter = birthdateEnter,
                setFocusBirthDay = {focusBirthDay = it},
                setFocusGender = {focusGender = it},
            )
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = onSave,
                    text = TextApp.holderSave
                )
            }
        }
    }

    if (focusGender) {
        DialogBottomSheet(
            onDismiss = {
                focusManager.clearFocus()
                focusGender = false
            }) { onDismiss ->
            DialogGenderItem(
                genderEnter = genderEnter.value,
                setGender = {
                    genderEnter.value = it
                    onDismiss.invoke()
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                },
                onDismiss = onDismiss
            )
        }
    }

    if (focusBirthDay) {
        DialogDataPickerddmmyyy(
            initTime = birthdateEnter.value,
            onDismissDialog = {
                focusBirthDay = false
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = { birthdateEnter.value = it },
        )
    }
}

@Composable
private fun ColumnPersonalData(
    firstNameEnter: MutableState<TextFieldValue>,
    lastNameEnter: MutableState<TextFieldValue>,
    patronymicEnter: MutableState<TextFieldValue>,
    maidenNameEnter: MutableState<TextFieldValue>,
    genderEnter: MutableState<Gender?>,
    birthdateEnter: MutableState<Long?>,
    setFocusBirthDay:(Boolean) -> Unit,
    setFocusGender:(Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isErrorBirthDay = remember(birthdateEnter.value) { birthdateEnter.value == null }
    val isErrorName = remember(firstNameEnter.value) { firstNameEnter.value.text.isEmpty() }
    val isErrorLastName = remember(lastNameEnter.value) { lastNameEnter.value.text.isEmpty() }
    val isErrorGender = remember(genderEnter.value) { genderEnter.value == null }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyMedium(text = TextApp.textSurname)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = lastNameEnter.value,
            onValueChange = { lastNameEnter.value = it },
            singleLine = true,
            isError = isErrorLastName,
            supportingText = {
                if (isErrorLastName) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorLastName) {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_error))
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textName)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = firstNameEnter.value,
            onValueChange = { firstNameEnter.value = it },
            singleLine = true,
            isError = isErrorName,
            supportingText = {
                if (isErrorName) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorName) {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_error))
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPatronymic)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = patronymicEnter.value,
            onValueChange = { patronymicEnter.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)) {
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        AnimatedVisibility(
            visible = genderEnter.value == Gender.WOMAN
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                BoxSpacer()
                TextBodyMedium(text = TextApp.textMaidenName)
                BoxSpacer(0.5f)
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = maidenNameEnter.value,
                    onValueChange = { maidenNameEnter.value = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (!focusManager.moveFocus(FocusDirection.Down)) {
                                focusManager.clearFocus()
                            }
                        }
                    ),
                )
            }
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBirthDayWye)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { setFocusBirthDay.invoke( it.hasFocus) },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(birthdateEnter.value?.millDateDDMMYYYYg() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = isErrorBirthDay,
                supportingText = {
                    if (isErrorBirthDay) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { setFocusBirthDay.invoke( true) }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textGender)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { setFocusGender.invoke( it.hasFocus) },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(genderEnter.value?.getGenderText() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = isErrorGender,
                supportingText = {
                    if (isErrorGender) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { setFocusGender.invoke(true) }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
    }
}