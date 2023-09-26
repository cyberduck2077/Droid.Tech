package ru.droid.tech.screens.module_registration.step_5_join_cell_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.HyperlinkText
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextTitleLarge
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.screens.module_registration.RegStepsModel

class StepFifthJoinInCellSecond : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val DroidRequest by model.DroidRequest.collectAsState()
        BackPressHandler(onBackPressed = model::goBackToSplashScreen)
        StepFifthJoinInCellSecondScr(
            onClickBack = model::goBackToSplashScreen,
            idCell = "${DroidRequest?.id ?: "-"}",
            headOfTheDroid = DroidRequest?.user?.lastName ?: "-",
        )
    }
}


@Composable
private fun StepFifthJoinInCellSecondScr(
    onClickBack: () -> Unit,
    idCell: String,
    headOfTheDroid: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = DimApp.screenPadding)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                TextTitleLarge(text = TextApp.textWelcomeDroidTech)
                HyperlinkText(
                    fullText = TextApp.formatSentARequestToJoin(headOfTheDroid, idCell),
                    hyperLinks = listOf(headOfTheDroid, idCell)
                ) { item, index ->

                }
            }
            IconApp(
                modifier = Modifier.align(Alignment.Center),
                painter = rememberImageRaw(id = R.raw.ic_time),
                size = DimApp.iconStubBig,
                tint = ThemeApp.colors.containerVariant
            )


        }
    }

}