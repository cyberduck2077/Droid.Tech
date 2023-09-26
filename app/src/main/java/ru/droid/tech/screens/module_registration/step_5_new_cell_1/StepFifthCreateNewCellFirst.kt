package ru.droid.tech.screens.module_registration.step_5_new_cell_1

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogDataPickerddmmyyy
import ru.droid.tech.base.common_composable.DialogGenderItem
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
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.millDateDDMMYYYY

class StepFifthCreateNewCellFirst : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.updatingUser.collectAsState()
        val firstDroidMember by model.firstDroidMember.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(
            onClickBack = model::goBackStack,
            yourGender = userData.gender,
            firstDroidMember = firstDroidMember,
            updateFirstDroidMember = model::updateFirstDroidMember,
            onClickSkip = model::goBackToSplashScreen,
            onClickAdd = model::addSatelliteInCell,
        )
    }
}

@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    onClickSkip: () -> Unit,
    yourGender: Gender?,
    firstDroidMember: DroidMemberUICreating,
    updateFirstDroidMember: (DroidMemberUICreating) -> Unit,
    onClickAdd: () -> Unit,
) {

    var focusBirthDay by rememberState { false }
    var focusGender by rememberState { false }
    var birthDayValidation by rememberState { true }
    var genderValidation by rememberState { true }
    val focusManager = LocalFocusManager.current
    val checkData: Boolean = remember(
        firstDroidMember,
    ) {
        firstDroidMember.isFullForeSend()
    }

    val nextStep = remember(checkData) {
        {
            if (checkData) {
                onClickAdd.invoke()
            }
        }
    }

    val onValidationBirthDay = remember(firstDroidMember.birthdate) {
        {
            birthDayValidation = firstDroidMember.birthdate == null
        }
    }
    val onValidationGender = remember(firstDroidMember.gender) {
        {
            genderValidation = firstDroidMember.gender == null
        }
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
            text = TextApp.formatStepFrom(4, 4)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)
            ) {
                TextTitleLarge(text = TextApp.textCreateACell)
                TextBodyLarge(text = yourGender?.getEnterDataYourSatellite() ?: "")
            }
            BoxSpacer()
            ColumnContentNewCell(
                firstName = firstDroidMember.firstName,
                lastName = firstDroidMember.lastName,
                patronymic = firstDroidMember.patronymic,
                maidenName = firstDroidMember.maidenName,
                gender = firstDroidMember.gender,
                birthdate = firstDroidMember.birthdate,
                isSetGender = false,
                setFirstName = {
                    updateFirstDroidMember.invoke(firstDroidMember.copy(firstName = it))
                },
                setLastName = {
                    updateFirstDroidMember.invoke(firstDroidMember.copy(lastName = it))
                },
                setPatronymic = {
                    updateFirstDroidMember.invoke(firstDroidMember.copy(patronymic = it))
                },
                setMaidenName = {
                    updateFirstDroidMember.invoke(firstDroidMember.copy(maidenName = it))
                },
                isMaidenName = false,
                birthDayValidation = birthDayValidation,
                genderValidation = genderValidation,
                onFocusBirthDay = {
                    focusBirthDay = it
                },
                onFocusGender = {
                    focusGender = it
                },
                roleDroid = firstDroidMember.role,
            )

            BoxFillWeight()
            PanelBottom {
                ButtonWeakApp(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickSkip,
                    text = TextApp.titleSkip
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleAdd
                )
            }
        }
    }

    if (focusBirthDay) {
        DialogDataPickerddmmyyy(
            initTime = firstDroidMember.birthdate,
            onDismissDialog = {
                focusBirthDay = false
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = {
                updateFirstDroidMember.invoke(firstDroidMember.copy(birthdate = it))
                onValidationBirthDay.invoke()
            },
        )
    }

    if (focusGender) {
        DialogBottomSheet(
            onDismiss = {
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
                focusGender = false
            }) { onDismiss ->
            DialogGenderItem(
                genderEnter = firstDroidMember.gender,
                setGender = {
                    updateFirstDroidMember.invoke(firstDroidMember.copy(gender = it))
                    onValidationGender.invoke()
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }

}

@Composable
fun ColumnContentNewCell(
    firstName: String?,
    setFirstName: (String) -> Unit,
    lastName: String?,
    setLastName: (String) -> Unit,
    patronymic: String?,
    birthdate: Long?,
    setPatronymic: (String?) -> Unit,
    maidenName: String?,
    setMaidenName: (String?) -> Unit,
    isSetGender: Boolean = true,
    isMaidenName: Boolean = true,
    birthDayValidation: Boolean,
    genderValidation: Boolean,
    gender: Gender?,
    onFocusBirthDay: (Boolean) -> Unit,
    onFocusGender: (Boolean) -> Unit,
    roleDroid: RoleDroid?,
) {
    val focusManager = LocalFocusManager.current
    var nameValidation by rememberState { true }
    var lastNameValidation by rememberState { true }


    val onValidationName = remember(firstName) {
        {
            nameValidation = firstName.isNullOrEmpty()
        }
    }
    val onValidationLastName = remember(lastName) {
        {
            lastNameValidation = lastName.isNullOrEmpty()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DimApp.screenPadding)
    ) {

        TextBodyMedium(text = TextApp.textSurname)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = lastName ?: "",
            onValueChange = {
                setLastName(it)
                onValidationLastName.invoke()
            },
            singleLine = true,
            isError = !lastNameValidation,
            supportingText = {
                if (!lastNameValidation) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (!lastNameValidation) {
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
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textName)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = firstName ?: "",
            onValueChange = {
                setFirstName(it)
                onValidationName.invoke()
            },
            singleLine = true,
            isError = !nameValidation,
            supportingText = {
                if (!nameValidation) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (!nameValidation) {
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
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textPatronymic)
        BoxSpacer(0.5f)
        TextFieldOutlinesAppStr(
            modifier = Modifier.fillMaxWidth(),
            value = patronymic ?: "",
            onValueChange = { setPatronymic(it) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBirthDayWye)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { onFocusBirthDay.invoke( it.hasFocus) },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(birthdate?.millDateDDMMYYYY() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = !birthDayValidation,
                supportingText = {
                    if (!birthDayValidation) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = {  onFocusBirthDay.invoke( true )}) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        if (isSetGender) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textGender)
            BoxSpacer(0.5f)
            Box(
                modifier = Modifier
                    .onFocusChanged {  onFocusGender.invoke( it.hasFocus) },
            ) {
                TextFieldOutlinesApp(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue(gender?.getGenderText() ?: ""),
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    isError = !genderValidation,
                    supportingText = {
                        if (!genderValidation) {
                            Text(text = TextApp.textObligatoryField)
                        }
                    },
                    placeholder = {
                        Text(text = TextApp.textNotSpecified)
                    },
                    trailingIcon = {
                        IconButton(onClick = { onFocusGender.invoke(true) }) {
                            IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                        }
                    },
                )
            }
        }
        AnimatedVisibility(
            visible = gender == Gender.WOMAN && isMaidenName
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                BoxSpacer()
                TextBodyMedium(text = TextApp.textMaidenName)
                BoxSpacer(0.5f)
                TextFieldOutlinesAppStr(
                    modifier = Modifier.fillMaxWidth(),
                    value = maidenName ?: "",
                    onValueChange = { setMaidenName(it) },
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
    }


}