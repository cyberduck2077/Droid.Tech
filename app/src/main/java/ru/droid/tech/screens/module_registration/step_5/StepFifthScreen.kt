package ru.droid.tech.screens.module_registration.step_5

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.screens.module_registration.RegStepsModel

/**Выбор создать ячейку или присоединиться*/
class StepFifthScreen : Screen {

    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthScr(
            onClickBack = model::goBackStack,
            onClickJoinInCell = model::goToJoinInCellFirst,
            onClickCreateNewCell = model::goToCreateNewCellFirst
        )
    }
}

@Composable
private fun StepFifthScr(
    onClickBack: () -> Unit,
    onClickJoinInCell: () -> Unit,
    onClickCreateNewCell: () -> Unit
) {

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
            TextTitleLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = TextApp.textWelcomeDroidTech)

            ActionCard(
                painter = rememberImageRaw(id = R.raw.ic_Droid),
                modifier = Modifier.padding(DimApp.screenPadding),
                titleText = TextApp.textCreateADroidCell,
                bodyText = TextApp.textEnterTheDetailsOfYourDroidMembers,
                onClick = onClickCreateNewCell
            )

            ActionCard(
                painter = rememberImageRaw(id = R.raw.ic_person_add),
                modifier = Modifier.padding(DimApp.screenPadding),
                titleText = TextApp.textJoinADroidUnit,
                bodyText = TextApp.textSignInWithAnInvite,
                onClick = onClickJoinInCell
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DimApp.screenPadding)
            ) {
                TextBodyMedium(text = TextApp.holderSymbolStartDescription)
                TextBodyMedium(text = TextApp.textCreatingADroidDescription)
            }
        }
    }
}

@Composable
private fun ActionCard(
    painter: Painter,
    modifier: Modifier,
    titleText: String,
    bodyText: String,
    onClick: () -> Unit,
) {
    val conf = LocalConfiguration.current
    val height = remember { conf.screenHeightDp * 0.16f }
    Row(
        modifier = modifier
            .heightIn(min = height.dp)
            .shadow(
                elevation = DimApp.screenPadding,
                shape = ThemeApp.shape.smallAll,
            )
            .clip(ThemeApp.shape.smallAll)
            .background(ThemeApp.colors.backgroundVariant)
            .clickableRipple { onClick.invoke() }
            .padding(DimApp.screenPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BoxSpacer()
        IconApp(
            size = DimApp.iconSizeBig,
            painter = painter,
            tint = null
        )
        BoxSpacer()
        Column {
            TextTitleSmall(text = titleText)
            BoxSpacer(.5f)
            TextBodyMedium(text = bodyText)
        }
        BoxSpacer()
    }
}