package ru.droid.tech.screens.module_registration.step_2

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.DialogBottomSheet
import ru.droid.tech.base.common_composable.DialogCropImage
import ru.droid.tech.base.common_composable.DialogGenderItem
import ru.droid.tech.base.common_composable.DialogGetImage
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
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberOpenIntentUrl
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.data.common.models.local.maps.Gender


class StepTwoScreen() : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.updatingUser.collectAsState()
        val image by model.image.collectAsState()
        var imageUriCamera by rememberState<Uri?> { null }
        var getImage by rememberState { false }
        BackPressHandler(onBackPressed = model::goBackStack)
        val openPolicy = rememberOpenIntentUrl()

        LifeScreen(onResume = {
            model.getUser()
        })

        StepTwoScr(
            firstName = userData.firstName,
            lastName = userData.lastName,
            patronymic = userData.patronymic,
            maidenName = userData.maidenName,
            gender = userData.gender,
            imageUri = image,
            onClickBack = model::goBackStack,
            onClickNext = model::updateMyProfileStepTwo,
            onClickToAuth = model::goToAuth,
            onClickNewImage = {
                getImage = true
            },
            onClickPrivacyPolicy = {
                openPolicy.invoke(TextApp.linkTech)
            })

        imageUriCamera?.let {
            DialogCropImage(
                onDismiss = { imageUriCamera = null },
                uriImage = it,
                onImageCroppedUri = model::uploadPhoto,
            )
        }

        if (getImage) {
            DialogGetImage(
                onDismiss = { getImage = false },
                uploadPhoto = {
                    imageUriCamera = it
                    getImage = false
                }
            )
        }
    }
}

@Composable
private fun StepTwoScr(
    firstName: String?,
    lastName: String?,
    patronymic: String?,
    maidenName: String?,
    gender: Gender?,
    imageUri: Any?,
    onClickBack: () -> Unit,
    onClickToAuth: () -> Unit,
    onClickNext: (
        firstName: String,
        lastName: String,
        patronymic: String?,
        maidenName: String?,
        gender: Gender,
    ) -> Unit,
    onClickNewImage: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
) {

    val firstNameEnter = rememberState(firstName) { TextFieldValue(firstName ?: "") }
    val lastNameEnter = rememberState(lastName) { TextFieldValue(lastName ?: "") }
    val patronymicEnter = rememberState(patronymic) { TextFieldValue(patronymic ?: "") }
    val maidenNameEnter = rememberState(maidenName) { TextFieldValue(maidenName ?: "") }
    val genderEnter = rememberState(gender) { gender ?: Gender.MAN }
    val focusManager = LocalFocusManager.current
    var focusGender by rememberState { false }
    var firstNameValidate by rememberState { true }
    var lastNameValidate by rememberState { true }

    val onValidateFirstName = remember(firstNameEnter.value) {
        {
            firstNameValidate = firstNameEnter.value.text.isNotEmpty()
        }
    }
    val onValidateLastName = remember(lastNameEnter.value) {
        {
            lastNameValidate = lastNameEnter.value.text.isNotEmpty()
        }
    }

    val checkData: Boolean = remember(
        firstNameEnter.value,
        lastNameEnter.value,
        genderEnter.value
    ) {
        firstNameEnter.value.text.isNotEmpty() &&
                lastNameEnter.value.text.isNotEmpty()
    }

    val nextStep = remember(checkData) {
        {
            if (checkData) onClickNext(
                firstNameEnter.value.text,
                lastNameEnter.value.text,
                patronymicEnter.value.text.ifEmpty { null },
                maidenNameEnter.value.text.ifEmpty { null },
                genderEnter.value,
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
            text = TextApp.formatStepFrom(2, 4)
        )
        BoxSpacer(0.5f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ColumnContentTwo(
                firstNameEnter = firstNameEnter,
                lastNameEnter = lastNameEnter,
                patronymicEnter = patronymicEnter,
                maidenNameEnter = maidenNameEnter,
                genderEnter = genderEnter,
                firstNameValidate = firstNameValidate,
                lastNameValidate = lastNameValidate,
                imageUri = imageUri,
                onClickNextStep = nextStep,
                onClickPrivacyPolicy = onClickPrivacyPolicy,
                onClickToAuth = onClickToAuth,
                onValidateFirstName = onValidateFirstName,
                onValidateLastName = onValidateLastName,
                onClickNewImage = onClickNewImage,
                onFocusGender = {focusGender = it},
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

    if (focusGender) {
        DialogBottomSheet(
            onDismiss = {
                focusManager.clearFocus()
                focusGender = false
            }) { onDismiss ->
            DialogGenderItem(
                genderEnter = null,
                setGender = {
                    genderEnter.value = it
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }
}


@Composable
private fun ColumnScope.ColumnContentTwo(
    firstNameEnter: MutableState<TextFieldValue>,
    lastNameEnter: MutableState<TextFieldValue>,
    patronymicEnter: MutableState<TextFieldValue>,
    maidenNameEnter: MutableState<TextFieldValue>,
    genderEnter: MutableState<Gender>,
    imageUri: Any?,
    firstNameValidate: Boolean,
    lastNameValidate: Boolean,
    onClickNewImage: () -> Unit,
    onValidateFirstName: () -> Unit,
    onValidateLastName: () -> Unit,
    onClickNextStep: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickToAuth: () -> Unit,
    onFocusGender: (Boolean) -> Unit
) {


    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textCreateAnAccount)
        BoxSpacer()
        TextBodyLarge(text = TextApp.textEnterYourDetails)
        BoxSpacer(2f)
        TextBodyMedium(text = TextApp.textPhoto)
        BoxSpacer(0.5f)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoxImageLoad(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(DimApp.imageAvatarSize),
                image = imageUri,
                drawableError = R.drawable.stab_avatar,
                drawablePlaceholder = R.drawable.stab_avatar
            )
            BoxSpacer()
            ButtonWeakApp(onClick = onClickNewImage, text = TextApp.titleChange)
        }
        BoxSpacer()
        TextBodyMedium(text = TextApp.textName)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = firstNameEnter.value,
            onValueChange = {
                firstNameEnter.value = it
                onValidateFirstName.invoke()
            },
            singleLine = true,
            isError = !firstNameValidate,
            supportingText = {
                if (!firstNameValidate) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (!firstNameValidate) {
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
        BoxSpacer(2f)
        TextBodyMedium(text = TextApp.textSurname)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = lastNameEnter.value,
            onValueChange = {
                lastNameEnter.value = it
                onValidateLastName.invoke()
            },
            singleLine = true,
            isError = !lastNameValidate,
            supportingText = {
                if (!lastNameValidate) {
                    Text(text = TextApp.textObligatoryField)
                }
            },
            trailingIcon = {
                if (!lastNameValidate) {
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
        BoxSpacer(2f)
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
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )

        BoxSpacer()
        TextBodyMedium(text = TextApp.textGender)
        BoxSpacer(0.5f)
        Box(
            modifier = Modifier
                .onFocusChanged { onFocusGender.invoke(it.hasFocus) },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier.fillMaxWidth(),
                value = TextFieldValue(genderEnter.value.getGenderText()),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
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
        BoxSpacer()
        AnimatedVisibility(
            visible = genderEnter.value == Gender.WOMAN
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                            focusManager.clearFocus()
                            onClickNextStep.invoke()
                        }
                    ),
                )
                BoxSpacer()
            }
        }
        BoxSpacer()
        HyperlinkText(
            fullText = TextApp.formatYouAgreeTo(TextApp.textPrivacyPolicy),
            hyperLinks = listOf(TextApp.textPrivacyPolicy)
        ) { _, _ ->
            onClickPrivacyPolicy.invoke()
        }
        BoxSpacer(2f)
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { _, _ ->
            onClickToAuth.invoke()
        }
    }


}

@Preview
@Composable
private fun StepTwoScrTest() {
    TechTheme {
        StepTwoScr(
            imageUri = null,
            onClickBack = {},
            onClickToAuth = {},
            onClickNext = { _, _, _, _, _ -> },
            onClickNewImage = {},
            onClickPrivacyPolicy = {},
            firstName = "firstName",
            lastName = "lastName",
            patronymic = "patronymic",
            maidenName = "maidenName",
            gender = null,
        )
    }
}