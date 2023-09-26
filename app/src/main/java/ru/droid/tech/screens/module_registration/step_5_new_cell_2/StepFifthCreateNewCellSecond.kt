package ru.droid.tech.screens.module_registration.step_5_new_cell_2

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
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
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.droid.tech.screens.module_registration.step_5_new_cell_1.ColumnContentNewCell
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.res.TextApp

class StepFifthCreateNewCellSecond : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val DroidMembers by model.DroidMembers.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(
            onClickBack = model::goBackStack,
            onClickAdd = model::addDroidMember,
            listNewDataMans = DroidMembers,
            updateDroidMembers = model::updateListDroidMembers,
            onClickSkip = model::goBackToSplashScreen,
            onClickNextScreen = model::goToNewCellThird
        )
    }
}


@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    onClickAdd: () -> Unit,
    onClickSkip: () -> Unit,
    onClickNextScreen: () -> Unit,
    listNewDataMans: List<DroidMemberUICreating>,
    updateDroidMembers: (List<DroidMemberUICreating>) -> Unit,
) {

    val checkData: Boolean = remember(
        listNewDataMans,
    ) { listNewDataMans.all { it.isFullForeSend() } }
    val focusManager = LocalFocusManager.current
    var focusBirthDay: Pair<Int, DroidMemberUICreating>? by rememberState { null }
    var focusGender: Pair<Int, DroidMemberUICreating>? by rememberState { null }

    val nextStep = remember(checkData) {
        {
            if (checkData) {
                onClickNextScreen.invoke()
            }
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
                    .animateContentSize(animationSpec = tween(durationMillis = 300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding)
                ) {
                    TextTitleLarge(text = TextApp.textCreateACell)
                    TextBodyLarge(text = TextApp.textEnterChildren)
                }
                BoxSpacer()

                listNewDataMans.forEachIndexed { index, item ->
                    val birthDayValidation: Boolean = remember(item.birthdate) { item.birthdate == null }
                    val genderValidation: Boolean = remember(item.gender) { item.gender == null }

                    IconButtonApp(modifier = Modifier.align(Alignment.End), onClick = {
                        updateDroidMembers.invoke(listNewDataMans.filterIndexed { indexData, _ ->
                            indexData != index
                        })
                    }) {
                        IconApp(
                            painter = rememberImageRaw(id = R.raw.ic_delete),
                            tint = ThemeApp.colors.attentionContent
                        )
                    }
                    ColumnContentNewCell(
                        firstName = item.firstName,
                        lastName = item.lastName,
                        patronymic = item.patronymic,
                        birthdate = item.birthdate,
                        maidenName = item.maidenName,
                        roleDroid = item.role,
                        gender = item.gender,
                        setFirstName = {
                            updateDroidMembers.invoke(
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(firstName = it)
                                )
                            )
                        },
                        setLastName = {
                            updateDroidMembers.invoke(
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(lastName = it)
                                )
                            )
                        },
                        setPatronymic = {
                            updateDroidMembers.invoke(
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(patronymic = it)
                                )
                            )
                        },
                        setMaidenName = {
                            updateDroidMembers.invoke(
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(maidenName = it)
                                )
                            )
                        },
                        birthDayValidation = birthDayValidation,
                        genderValidation = genderValidation,
                        onFocusBirthDay = {
                            focusBirthDay = if (it)Pair(index, item) else null
                        },
                        onFocusGender = {
                            focusGender = if (it)Pair(index, item) else null
                        },
                    )
                    FillLineHorizontal(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DimApp.screenPadding)
                    )
                }
            }

            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
                onClick = onClickAdd,
                text = TextApp.titleAdd,
                contentStart = {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
                })

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

    focusBirthDay?.let { focusBirthDayItem ->
        DialogDataPickerddmmyyy(
            initTime = focusBirthDayItem.second.birthdate,
            onDismissDialog = {
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
                focusBirthDay = null
            },
            onDataMillisSet = {
                updateDroidMembers.invoke(
                    updateNewDataMens(
                        index = focusBirthDayItem.first,
                        oldList = listNewDataMans,
                        newUpdate = focusBirthDayItem.second.copy(birthdate = it)
                    )
                )
                focusBirthDay = null
            },
        )
    }

    focusGender?.let { focusGenderItem ->
        DialogBottomSheet(
            onDismiss = {
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
                focusGender = null
            }) { onDismiss ->
            DialogGenderItem(
                genderEnter = focusGenderItem.second.gender,
                setGender = {
                    if (focusGenderItem.second.role != RoleDroid.SPOUSE
                        && focusGenderItem.second.role != RoleDroid.SELF) {
                        updateDroidMembers.invoke(
                            updateNewDataMens(
                                index = focusGenderItem.first,
                                oldList = listNewDataMans,
                                newUpdate = focusGenderItem.second.copy(gender = it)
                            )
                        )
                    }
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }
}

fun updateNewDataMens(
    index: Int, oldList: List<DroidMemberUICreating>, newUpdate: DroidMemberUICreating
): List<DroidMemberUICreating> {
    val oldListMut = oldList.toMutableList()
    return try {
        oldListMut[index] = newUpdate
        oldListMut
    } catch (e: Exception) {
        e.printStackTrace()
        oldListMut
    }
}