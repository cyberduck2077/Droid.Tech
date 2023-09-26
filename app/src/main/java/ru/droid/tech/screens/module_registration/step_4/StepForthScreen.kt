package ru.droid.tech.screens.module_registration.step_4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
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
import ru.droid.tech.base.common_composable.ChipsApp
import ru.droid.tech.base.common_composable.DropMenuInterestChips
import ru.droid.tech.base.common_composable.HyperlinkText
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.modifyList
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.network.NetworkModelInterest
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.data.common.models.local.maps.InterestUI

class StepForthScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.updatingUser.collectAsState()
        val interests by model.interests.collectAsState()
        LifeScreen(onStart = model::onSearchInterests)
        BackPressHandler(onBackPressed = model::goBackStack)
        StepForthScr(
            onClickBack = model::goBackStack,
            onClickNext = model::goToFifthStep,
            onClickNextIfEmpty = model::goToFifthStep,
            onSearchInterests = model::onSearchInterests,
            onClickToAuth = model::goToAuth,
            listInterest = userData.interests ?: listOf(),
            description = userData.description,
            listInterestSearch = interests
        )
    }
}

@Composable
private fun StepForthScr(
    onClickBack: () -> Unit,
    onSearchInterests: (String) -> Unit,
    onClickNext: (
        description: String?,
        interests: List<String>?
    ) -> Unit,
    onClickToAuth: () -> Unit,
    listInterest: List<String>,
    description: String?,
    listInterestSearch: List<InterestUI>,
    onClickNextIfEmpty: () -> Unit
) {

    val listInterestEnter = rememberState(listInterest) { listInterest }
    val descriptionEnter = rememberState(description) { TextFieldValue(description ?: "") }
    val checkData: Boolean = remember(
        descriptionEnter.value,
        listInterestEnter.value,
    ) {
        descriptionEnter.value.text.isNotEmpty() || listInterestEnter.value.isNotEmpty()
    }
    val nextStep = remember(
        checkData
    ) {
        {
            if (checkData) onClickNext.invoke(
                descriptionEnter.value.text.ifEmpty { null },
                listInterestEnter.value.ifEmpty { null })
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
            ColumnContentForth(
                listInterestEnter = listInterestEnter,
                listInterestSearch = listInterestSearch,
                onClickToAuth = onClickToAuth,
                onSearchInterests = onSearchInterests,
                descriptionEnter = descriptionEnter
            )
            BoxFillWeight()
            PanelBottom {
                ButtonWeakApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickNextIfEmpty,
                    text = TextApp.titleFillInLater
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleNext
                )
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnContentForth(
    listInterestEnter: MutableState<List<String>>,
    listInterestSearch: List<InterestUI>,
    descriptionEnter: MutableState<TextFieldValue>,
    onSearchInterests: (String) -> Unit,
    onClickToAuth: () -> Unit,
) {

    val paintChips = rememberImageRaw(id = R.raw.ic_close)

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        TextTitleLarge(text = TextApp.textTellUsAboutYourInterests)
        BoxSpacer()
        BoxSpacer()
        TextBodyMedium(text = TextApp.textBiography)
        BoxSpacer(0.5f)
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = descriptionEnter.value,
            onValueChange = { descriptionEnter.value = it },
            placeholder = { Text(text = TextApp.textIAmSuchAndSuch) },
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
        TextBodyMedium(text = TextApp.textInterests)
        BoxSpacer(0.5f)
        DropMenuInterestChips(
            modifier = Modifier,
            items = listInterestSearch,
            itemsHave = listInterestEnter,
            onEnterText = onSearchInterests
        )
        BoxSpacer()
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            listInterestEnter.value.forEach { interest ->
                ChipsApp(
                    modifier = Modifier
                        .padding(
                            bottom = DimApp.screenPadding * .5f,
                            end = DimApp.screenPadding * .5f,
                    ),
                    text = interest,
                    trailingIcon = {
                        IconApp(
                            modifier = Modifier
                                .clip(ThemeApp.shape.smallAll)
                                .clickableRipple {
                                    listInterestEnter.value =
                                        modifyList(listInterestEnter.value, interest)
                                },

                            painter = paintChips)
                    }
                )
                BoxSpacer(.5f)
            }
        }
        BoxSpacer(2f)
        HyperlinkText(
            fullText = TextApp.formatAlreadyHaveAnAccount(TextApp.textToComeIn),
            hyperLinks = listOf(TextApp.textToComeIn)
        ) { item, index ->
            onClickToAuth.invoke()
        }
    }

}

