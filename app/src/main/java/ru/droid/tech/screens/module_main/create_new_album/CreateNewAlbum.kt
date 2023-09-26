package ru.droid.tech.screens.module_main.create_new_album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import ru.droid.tech.base.common_composable.DialogDataPickerddmmyyy
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.util.millDateDDMMYYYY

class CreateNewAlbum : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<CreateNewAlbumModel>()

        CreateNewAlbumScr(
            onClickBack = model::goBackStack,
            onClickCreate = model::createAlbum
        )
    }
}

@Composable
private fun CreateNewAlbumScr(
    onClickBack: () -> Unit,
    onClickCreate: (
        titleEnter: String,
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
//        isPrivateEnter: Boolean,
    ) -> Unit,
) {

    val titleEnter = rememberState { TextFieldValue() }
    val descriptionEnter = rememberState { TextFieldValue() }
    val customDateEnter = rememberState<Long?> { null }
    val addressEnter = rememberState { TextFieldValue() }
    val isPrivateEnter = rememberState { false }

    val checkData: Boolean = remember(
        titleEnter.value,
        descriptionEnter.value,
        customDateEnter.value,
    ) {
        descriptionEnter.value.text.isNotEmpty() || titleEnter.value.text.isNotEmpty() || customDateEnter.value != null
    }
    val create = remember(
        checkData
    ) {
        {
            if (checkData) onClickCreate.invoke(
                titleEnter.value.text,
                descriptionEnter.value.text,
                customDateEnter.value,
                addressEnter.value.text,
//                isPrivateEnter.value,
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(
            modifier = Modifier,
            painter = rememberImageRaw(R.raw.ic_close),
            onClickBack = onClickBack,
            text = TextApp.titleNewAlbum
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .verticalScroll(rememberScrollState())) {

            ContentEnterNewAlbumData(
                titleEnter = titleEnter,
                descriptionEnter = descriptionEnter,
                customDateEnter = customDateEnter,
                addressEnter = addressEnter,
//                isPrivateEnter = isPrivateEnter,
            )
            BoxFillWeight()

            PanelBottom {
                ButtonWeakApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickBack,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = create,
                    text = TextApp.titleCreate
                )
            }
        }
    }
}

@Composable
private fun ContentEnterNewAlbumData(
    titleEnter: MutableState<TextFieldValue>,
    descriptionEnter: MutableState<TextFieldValue>,
    customDateEnter: MutableState<Long?>,
    addressEnter: MutableState<TextFieldValue>,
//    isPrivateEnter: MutableState<Boolean>,
    descriptionLight: Int = 150,
) {

    val focusManager = LocalFocusManager.current
    var focusCustomDate by rememberState { false }
    val isErrorTitle = remember(titleEnter.value) { titleEnter.value.text.isEmpty() }
    val isErrorCustomDate = remember(customDateEnter.value) { customDateEnter.value== null}
    val isErrorDescription =
        remember(descriptionEnter.value) { descriptionEnter.value.text.isEmpty() }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyMedium(text = TextApp.textNameTitle)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = titleEnter.value,
            onValueChange = { titleEnter.value = it },
            isError = isErrorTitle,
            supportingText = {
                if (isErrorTitle) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorTitle) {
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
                    if (!focusManager.moveFocus(FocusDirection.Down)){
                        focusManager.clearFocus()
                    }
                }
            ),
        )

        BoxSpacer()
        TextBodyMedium(text = TextApp.textDescription)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = descriptionEnter.value,
            onValueChange = {
                if (descriptionEnter.value.text.length < descriptionLight) {
                    descriptionEnter.value = it
                }
            },
            isError = isErrorDescription,
            supportingText = {
                if (isErrorDescription) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (isErrorDescription) {
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
                    if (!focusManager.moveFocus(FocusDirection.Down)){
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        TextCaption(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${descriptionEnter.value.text.length}/${descriptionLight}",
            color = ThemeApp.colors.textLight,
        )
        BoxSpacer()
        TextBodyMedium(text = TextApp.textCustomDay)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { focusCustomDate = it.hasFocus },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(customDateEnter.value?.millDateDDMMYYYY() ?: ""),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = isErrorCustomDate,
                supportingText = {
                    if (isErrorCustomDate) {
                        Text(text = TextApp.textObligatoryField)
                    }
                },
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = { focusCustomDate = true }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textAddressOrPlace)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = addressEnter.value,
            onValueChange = { addressEnter.value = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!focusManager.moveFocus(FocusDirection.Down)){
                        focusManager.clearFocus()
                    }
                }
            ),
        )
        BoxSpacer()
//        Row(
//            modifier = Modifier,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            CheckerApp(
//                checked = isPrivateEnter.value,
//                onCheckedChange = {
//                    isPrivateEnter.value = !isPrivateEnter.value
//                })
//            TextBodyLarge(text = TextApp.textPrivateAlbum)
//        }
    }

    if (focusCustomDate) {
        DialogDataPickerddmmyyy(
            initTime = customDateEnter.value,
            onDismissDialog = {
                focusCustomDate = false
                if (!focusManager.moveFocus(FocusDirection.Down)){
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = { customDateEnter.value = it },
        )
    }
}