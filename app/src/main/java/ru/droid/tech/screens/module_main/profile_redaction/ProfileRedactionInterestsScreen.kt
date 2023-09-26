package ru.droid.tech.screens.module_main.profile_redaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.CheckerApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.modifyList
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.InterestUI
import ru.data.common.models.res.TextApp

class ProfileRedactionInterestsScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        val interests by model.interests.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        ProfileRedactionInterestsScr(
            onClickBack = model::goBackStack,
            onClickSave = model::updateInterests,
            listInterest = userData.interests,
            description = userData.description,
            listInterestAll = interests,
        )
    }
}

@Composable
private fun ProfileRedactionInterestsScr(
    onClickBack: () -> Unit,
    onClickSave: (
        description: String?,
        interests: List<String>?
    ) -> Unit,
    listInterest: List<String>,
    description: String?,
    listInterestAll: List<InterestUI>,
) {
    val focusManager = LocalFocusManager.current

    val descriptionEnter = rememberState(description) { TextFieldValue(description ?: "") }
    val listInterestEnter = rememberState(listInterest) { listInterest }
    val checkData: Boolean = remember(
        descriptionEnter.value,
        listInterestEnter.value,
    ) {
        descriptionEnter.value.text != description || listInterestEnter.value.sorted() != listInterest.sorted()
    }
    val onSave = remember(
        checkData
    ) {
        {
            if (checkData) onClickSave.invoke(
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
        PanelNavBackTop(
            modifier = Modifier.shadow(DimApp.shadowElevation),
            onClickBack = onClickBack,
            text = TextApp.textInterests
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding)
                ) {
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
                                focusManager.clearFocus()
                            }
                        ),
                    )
                    BoxSpacer()
                    TextBodyMedium(text = TextApp.textInterests)
                    BoxSpacer(0.5f)
                }
            }
            items(
                items = listInterestAll,
                key = { item -> item.id }) { item ->
                Column() {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CheckerApp(
                            checked = listInterestEnter.value.contains(item.name),
                            onCheckedChange = {
                                listInterestEnter.value = modifyList(
                                    listInterestEnter.value,
                                    item.name
                                )
                            })
                        TextBodyLarge(text = item.name)
                    }
                    BoxSpacer(0.5f)
                }
            }
        }
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