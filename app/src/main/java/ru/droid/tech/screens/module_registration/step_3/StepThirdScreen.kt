package ru.droid.tech.screens.module_registration.step_3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.DialogDataPickerddmmyyy
import ru.droid.tech.base.common_composable.DropMenuCities
import ru.droid.tech.base.common_composable.HyperlinkText
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.data_base.City
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.data.common.models.util.millDateDDMMYYYY

class StepThirdScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val location by model.location.collectAsState()
        val userData by model.updatingUser.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)
        StepThirdScr(
            onClickBack = model::goBackStack,
            onClickNext = model::updateMyProfileStepThird,
            locations = location,
            onSearchLocation = model::onSearchLocation,
            onClickToAuth = model::goToAuth,
            birthdateMillis = userData.birthdate,
            locationResidence = userData.location,
            locationBirth = userData.birthLocation,
        )
    }
}

@Composable
private fun StepThirdScr(
    onClickBack: () -> Unit,
    onClickNext: (
        birthdate: Long,
        residenceLocation: City?,
        birthLocation: City?
    ) -> Unit,
    onClickToAuth: () -> Unit,
    birthdateMillis: Long?,
    locationResidence: City?,
    locationBirth: City?,
    onSearchLocation: (String?) -> Unit,
    locations: List<City>,
) {

    val birthdateEnter = rememberState(birthdateMillis) { birthdateMillis }
    val locationResidenceEnter = rememberState(locationResidence) { locationResidence }
    val locationBirthEnter = rememberState(locationBirth) { locationBirth }
    val focusManager = LocalFocusManager.current
    var focusBirthDay by rememberState { false }
    var dataValidator by rememberState() { true }

    val checkData: Boolean = remember(birthdateEnter.value) { birthdateEnter.value != null }

    val onValidateData = remember(birthdateEnter.value) {
        {
            dataValidator = birthdateEnter.value != null
        }
    }

    val nextStep = remember(
        dataValidator,
        locationResidenceEnter.value,
        locationBirthEnter.value
    ) {
        {
            val date = birthdateEnter.value
            if (dataValidator && date != null) onClickNext.invoke(
                date,
                locationResidenceEnter.value,
                locationBirthEnter.value
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
        PanelNavBackTop(onClickBack = onClickBack)
        TextButtonStyle(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding),
            text = TextApp.formatStepFrom(3, 4)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentThird(
                birthdateEnter = birthdateEnter,
                locationResidenceEnter = locationResidenceEnter,
                dataValidator = dataValidator,
                onFocusBirthDay = {focusBirthDay = it},
                locationBirthEnter = locationBirthEnter,
                locations = locations,
                onSearchLocation = onSearchLocation,
                onClickToAuth = onClickToAuth,
            )
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

    if (focusBirthDay) {
        DialogDataPickerddmmyyy(
            initTime = birthdateEnter.value,
            onDismissDialog = {
                focusBirthDay = false
                focusManager.clearFocus()
            },
            onDataMillisSet = {
                birthdateEnter.value = it
                onValidateData.invoke()
            },
        )
    }
}

@Composable
private fun ColumnScope.ColumnContentThird(
    birthdateEnter: MutableState<Long?>,
    locationResidenceEnter: MutableState<City?>,
    locationBirthEnter: MutableState<City?>,
    locations: List<City>,
    dataValidator: Boolean,
    onSearchLocation: (String?) -> Unit,
    onClickToAuth: () -> Unit,
    onFocusBirthDay: (Boolean) -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        TextBodyLarge(text = TextApp.textEnterYourDetails)
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBirthDayWye)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { onFocusBirthDay.invoke(it.hasFocus) },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(birthdateEnter.value?.millDateDDMMYYYY() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = !dataValidator,
                supportingText = {
                    if (!dataValidator) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { onFocusBirthDay.invoke(true) }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfBirth)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            checkItem = { locationBirthEnter.value = it },
            locationChooser = locationBirthEnter.value?.name,
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCityOfResidence)
        BoxSpacer(0.5f)
        DropMenuCities(
            items = locations,
            modifier = Modifier.fillMaxWidth(),
            enterNameLocation = onSearchLocation,
            checkItem = { locationResidenceEnter.value = it },
            locationChooser = locationResidenceEnter.value?.name,
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textForAMorePreciseSearch)
        BoxSpacer(2f)
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToAuth.invoke()
        }
    }


}

