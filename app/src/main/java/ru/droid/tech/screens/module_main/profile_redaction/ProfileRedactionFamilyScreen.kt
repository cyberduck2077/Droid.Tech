package ru.droid.tech.screens.module_main.profile_redaction

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.droid.tech.screens.module_registration.step_5_new_cell_1.ColumnContentNewCell
import ru.droid.tech.screens.module_registration.step_5_new_cell_2.updateNewDataMens
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.RoleDroid
import ru.data.common.models.res.TextApp

class ProfileRedactionDroidScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val DroidMembers by model.DroidMembers.collectAsState()
        val DroidError by model.DroidError.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        LaunchedEffect(key1 = Unit, block = {
            model.getDroid()
        })
        ProfileRedactionDroidScr(
            onClickBack = model::goBackStack,
            onClickSave = model::saveDroid,
            onClickDeleteMember = model::deleteMember,
            DroidError = DroidError,
            listDataMans = DroidMembers,
        )
    }
}

@Composable
private fun ProfileRedactionDroidScr(
    onClickBack: () -> Unit,
    onClickDeleteMember: (DroidMemberUICreating) -> Unit,
    DroidError: Boolean,
    onClickSave: (List<DroidMemberUICreating>) -> Unit,
    listDataMans: List<DroidMemberUICreating>,
) {

    var listNewDataMans by rememberState(listDataMans) { listDataMans }
    val focusManager = LocalFocusManager.current
    var focusBirthDay: Pair<Int, DroidMemberUICreating>? by rememberState { null }
    var focusGender: Pair<Int, DroidMemberUICreating>? by rememberState { null }

    val onClickAdd: (RoleDroid) -> Unit = remember(listNewDataMans) {
        {
            listNewDataMans = listNewDataMans + DroidMemberUICreating(role = it)
        }
    }

    val checkData: Boolean = remember(
        listNewDataMans, listDataMans
    ) {
        val isFull = listNewDataMans.all { it.isFullForeSend() }
        val isChange = listDataMans != listNewDataMans
        isFull && isChange
    }

    val onSave = remember(checkData) {
        {
            if (checkData) {
                onClickSave.invoke(listNewDataMans.subtract(listDataMans.toSet()).toList())
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
        PanelNavBackTop(
            modifier = Modifier.shadow(DimApp.shadowElevation),
            onClickBack = onClickBack,
            text = TextApp.holderDroid
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
                BoxSpacer()
                if (DroidError && listNewDataMans.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextBodyMedium(
                            color = ThemeApp.colors.attentionContent,
                            text = TextApp.textDidNotChooseADroid
                        )
                    }
                }
                listNewDataMans.forEachIndexed { index, item ->
                    val birthDayValidation: Boolean = remember(item.birthdate) { item.birthdate == null }
                    val genderValidation: Boolean = remember(item.gender) { item.gender == null }

                    Row(
                        modifier = Modifier
                            .padding(start = DimApp.screenPadding)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextBodyLarge(text = item.getEnterDataYourSatellite() ?: "")
                        BoxFillWeight()
                        if (item.role != RoleDroid.SPOUSE
                            && item.role != RoleDroid.SELF) {
                            IconButtonApp(modifier = Modifier, onClick = {
                                onClickDeleteMember.invoke(item)
                            }) {
                                IconApp(
                                    painter = rememberImageRaw(id = R.raw.ic_delete),
                                    tint = ThemeApp.colors.attentionContent
                                )
                            }
                        }
                    }
                    BoxSpacer(.5f)
                    ColumnContentNewCell(
                        firstName = item.firstName,
                        lastName = item.lastName,
                        patronymic = item.patronymic,
                        birthdate = item.birthdate,
                        maidenName = item.maidenName,
                        roleDroid = item.role,
                        gender = item.gender,
                        setFirstName = {
                            listNewDataMans = updateNewDataMens(
                                index = index,
                                oldList = listNewDataMans,
                                newUpdate = item.copy(firstName = it)
                            )
                        },
                        setLastName = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(lastName = it)
                                )

                        },
                        setPatronymic = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(patronymic = it)
                                )

                        },
                        setMaidenName = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(maidenName = it)
                                )

                        },
                        birthDayValidation = birthDayValidation,
                        genderValidation = genderValidation,
                        onFocusBirthDay = {
                            focusBirthDay = if (it) Pair(index, item) else null
                        },
                        onFocusGender = {
                            focusGender = if (it) Pair(index, item) else null
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
                onClick = { onClickAdd.invoke(RoleDroid.CHILD) },
                text = RoleDroid.CHILD.getTextAddMembers(),
                contentStart = {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
                })

//            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
//                onClick = { onClickAdd.invoke(RoleDroid.PARENT) },
//                text = RoleDroid.PARENT.getTextAddMembers(),
//                contentStart = {
//                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
//                })
//
//            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
//                onClick = { onClickAdd.invoke(RoleDroid.SIBLING) },
//                text = RoleDroid.SIBLING.getTextAddMembers(),
//                contentStart = {
//                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
//                })

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

    focusBirthDay?.let { focusBirthDayItem ->
        DialogDataPickerddmmyyy(
            initTime = focusBirthDayItem.second.birthdate,
            onDismissDialog = {
                focusBirthDay = null
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
            },
            onDataMillisSet = {
                listNewDataMans =
                    updateNewDataMens(
                        index = focusBirthDayItem.first,
                        oldList = listNewDataMans,
                        newUpdate = focusBirthDayItem.second.copy(birthdate = it)
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
                        listNewDataMans =
                            updateNewDataMens(
                                index = focusGenderItem.first,
                                oldList = listNewDataMans,
                                newUpdate = focusGenderItem.second.copy(gender = it)
                            )
                    }
                    focusGender = null
                    onDismiss.invoke()
                },
                onDismiss = onDismiss
            )
        }
    }
}