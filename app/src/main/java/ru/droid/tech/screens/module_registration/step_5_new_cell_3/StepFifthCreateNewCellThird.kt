package ru.droid.tech.screens.module_registration.step_5_new_cell_3

import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberModel
import ru.data.common.models.network.CreatingDroidMember
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.data.common.models.local.maps.DroidMemberUICreating
import ru.data.common.models.local.maps.Gender

class StepFifthCreateNewCellThird : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.updatingUser.collectAsState()
        val firstDroidMember by model.firstDroidMember.collectAsState()
        val DroidMembers by model.DroidMembers.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(
            onClickBack = model::goBackStack,
            onClickFinish = model::finishAddNewDroidCell,
            userGender = userData.gender,
            nameUser = userData.getFullName(),
            birthdayUser = userData.birthdateHuman(),
            nameFirstDroidMember = firstDroidMember.getFullName(),
            birthdayFirstDroidMemberName = firstDroidMember.birthdateHuman,
            DroidMembers = DroidMembers,
        )
    }
}

@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    onClickFinish: () -> Unit,
    userGender: Gender?,
    nameUser: String,
    birthdayUser: String,
    nameFirstDroidMember: String,
    birthdayFirstDroidMemberName: String,
    DroidMembers: List<DroidMemberUICreating>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)) {
                TextTitleLarge(text = TextApp.textYourDroidUnit)
                TextBodyLarge(text = TextApp.textCheckYourDroidDetails)
            }

            CompletedMapDroid(
                title = TextApp.formatSomethingYou(userGender?.getGenderTextShort()),
                name = nameUser,
                gender = null,
                birthday = birthdayUser
            )
            FillLineHorizontal(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding))

            CompletedMapDroid(
                title = userGender?.getGenderYourSatellite()?.getGenderTextShort(),
                name = nameFirstDroidMember,
                gender = null,
                birthday = birthdayFirstDroidMemberName
            )
            FillLineHorizontal(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding))

            BoxSpacer(.5f)
            TextBodyMedium(
                modifier = Modifier
                    .padding(horizontal = DimApp.screenPadding),
                text = TextApp.textChildren)
            DroidMembers.forEachIndexed { index, member ->
                CompletedMapDroid(
                    title = null,
                    name = member.getFullName(),
                    gender = member.gender?.getGenderText(),
                    birthday = member.birthdateHuman
                )
                if ((DroidMembers.size - 1) > index) {
                    FillLineHorizontal(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding))
                }
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickFinish,
                    text = TextApp.titleAdd)
            }
        }
    }
}

@Composable
private fun CompletedMapDroid(
    title: String?,
    name: String,
    gender: String?,
    birthday: String,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        title?.let {
            BoxSpacer(.5f)
            TextBodyMedium(text = title)
        }

        BoxSpacer(.5f)
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBodyMedium(
                modifier = Modifier.fillMaxWidth(.35f),
                text = TextApp.holderName,
                color = ThemeApp.colors.textLight)
            TextBodyLarge(text = name)

        }

        gender?.let {
            BoxSpacer(.5f)
            Row(modifier = Modifier.fillMaxWidth()) {
                TextBodyMedium(
                    modifier = Modifier.fillMaxWidth(.35f),
                    text = TextApp.textGender_,
                    color = ThemeApp.colors.textLight)
                TextBodyLarge(text = gender)
            }
        }
        BoxSpacer(.5f)
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBodyMedium(
                modifier = Modifier.fillMaxWidth(.35f),
                text = TextApp.textBirthDay,
                color = ThemeApp.colors.textLight)
            TextBodyLarge(text = birthday)
        }
    }
}