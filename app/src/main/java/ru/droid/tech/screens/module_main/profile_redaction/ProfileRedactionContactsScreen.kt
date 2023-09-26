package ru.droid.tech.screens.module_main.profile_redaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonAccentTextApp
import ru.droid.tech.base.common_composable.DropMenuCities
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.OtpTextField
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.extension.formattedNumberPhoneRuNoSeven
import ru.droid.tech.base.extension.getFormattedNumberRuNoSeven
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.data_base.City
import ru.data.common.models.util.onlyDigit

class ProfileRedactionContactsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        val locationBirth by model.locationBirth.collectAsState()
        val locationResidence by model.locationResidence.collectAsState()
        val location by model.location.collectAsState()
        val newPhone by model.newPhone.collectAsState()
        val codeVerificationPhone by model.codeVerificationPhone.collectAsState()


        BackPressHandler(onBackPressed = model::goBackStack)
        ProfileRedactionContactsScr(
            onClickBack = model::goBackStack,
            locationBirth = locationBirth,
            locationResidence = locationResidence,
            tg = userData.tg,
            tel = userData.tel,
            onClickSave = model::saveContacts,
            locations = location,
            onSearchLocation = model::onSearchLocation,
        )

        codeVerificationPhone?.let { code ->
            Dialog(onDismissRequest = model::dismissDialogEnterCode) {
                var enterCode by rememberState { "" }
                val focusManager = LocalFocusManager.current
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(key1 = enterCode, block = {
                    if (enterCode.length == 4) {
                        model.sendNewPhone(
                            tel = newPhone ?: "", code = code
                        )
                    }
                })
                LaunchedEffect(key1 = Unit, block = {
                    focusRequester.requestFocus()
                })
                Column(
                    modifier = Modifier
                        .clip(ThemeApp.shape.mediumAll)
                        .background(ThemeApp.colors.backgroundVariant)
                        .padding(horizontal = DimApp.screenPadding)
                ) {
                    BoxSpacer()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextTitleSmall(text = TextApp.textPhoneConfirmationCode)
                        IconButtonApp(
                            modifier = Modifier.offset(x = DimApp.screenPadding),
                            onClick = model::dismissDialogEnterCode
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_close))
                        }
                    }
                    TextBodyMedium(text = TextApp.formatTextPhoneSend(newPhone?.formattedNumberPhoneRuNoSeven()))
                    BoxSpacer()
                    OtpTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .align(Alignment.CenterHorizontally),
                        otpCount = 4,
                        otpText = enterCode,
                        onOtpTextChange = {
                            enterCode = it
                        },
                        keyboardActionsSend = {
                            model.sendNewPhone(
                                tel = newPhone ?: "", code = code
                            )
                        }
                    )

                    BoxSpacer()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ButtonAccentTextApp(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = model::dismissDialogEnterCode,
                            text = TextApp.titleCancel
                        )
                    }
                    BoxSpacer()
                }

            }
        }
    }
}

@Composable
private fun ProfileRedactionContactsScr(
    onClickBack: () -> Unit,
    onClickSave: (
        locationBirth: City?,
        locationResidence: City?,
        tg: String?,
        tel: String?,
    ) -> Unit,
    onSearchLocation: (String?) -> Unit,
    locationBirth: City?,
    locationResidence: City?,
    tg: String?,
    tel: String?,
    locations: List<City>,
) {

    val locationResidenceEnter = rememberState(locationResidence) { locationResidence }
    val locationBirthEnter = rememberState(locationBirth) { locationBirth }
    val tgEnter = rememberState(tg) { TextFieldValue(tg ?: "") }
    val telEnter = rememberState(tel) { TextFieldValue(tel?.formattedNumberPhoneRuNoSeven() ?: "") }

    val checkData: Boolean = remember(
        locationResidenceEnter.value,
        locationBirthEnter.value,
        tgEnter.value,
        telEnter.value,
    ) {
        locationResidenceEnter.value?.id != locationResidence?.id ||
                locationBirthEnter.value?.id != locationBirth?.id ||
                tgEnter.value.text.ifEmpty { null } != tg ||
                telEnter.value.text.ifEmpty { null } != tel
    }

    val onSave = remember(checkData) {
        {
            if (checkData) onClickSave.invoke(
                locationBirthEnter.value,
                locationResidenceEnter.value,
                tgEnter.value.text,
                telEnter.value.text.onlyDigit(),
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
            text = TextApp.holderContacts
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContactsData(
                locationBirthEnter = locationBirthEnter,
                locationResidenceEnter = locationResidenceEnter,
                tgEnter = tgEnter,
                telEnter = telEnter,
                locations = locations,
                onSearchLocation = onSearchLocation
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
}

@Composable
private fun ColumnContactsData(
    locationBirthEnter: MutableState<City?>,
    locationResidenceEnter: MutableState<City?>,
    tgEnter: MutableState<TextFieldValue>,
    telEnter: MutableState<TextFieldValue>,
    locations: List<City>,
    onSearchLocation: (String?) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfBirth)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            checkItem = {
                locationBirthEnter.value = it
                onSearchLocation.invoke(null)
            },
            locationChooser = locationBirthEnter.value?.name
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfResidence)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            checkItem = {
                locationResidenceEnter.value = it
                onSearchLocation.invoke(null)
            },
            locationChooser = locationResidenceEnter.value?.name
        )

        BoxSpacer()
        TextBodyMedium(text = TextApp.textMobilePhone)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = telEnter.value,
            onValueChange = { telEnter.value = it.getFormattedNumberRuNoSeven() },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
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
        TextBodyMedium(text = TextApp.textTelegram)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = tgEnter.value,
            onValueChange = { tgEnter.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
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
    }
}