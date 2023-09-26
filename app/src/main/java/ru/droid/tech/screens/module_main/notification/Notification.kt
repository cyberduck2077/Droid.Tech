package ru.droid.tech.screens.module_main.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextCaption
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.network.NetworkModelNotification
import ru.data.common.models.util.secDataDDMMYYYHHMM

class Notification() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<NotificationModel>()
        val listNotification by model.listNotification.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)
        LifeScreen(onStart = {
            model.getNotification()
        })
        PostScr(
            onClickBack = model::goBackStack,
            listNotification = listNotification
        )
    }
}

@Composable
fun PostScr(
    onClickBack: () -> Unit,
    listNotification: List<NetworkModelNotification>,
) {
    var textCommentValue by rememberState { TextFieldValue("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .imePadding()) {
        PanelNavBackTop(
            modifier = Modifier,
            onClickBack = onClickBack,
            text = TextApp.textPost,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            if (listNotification.isEmpty()) item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DimApp.screenPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    TextTitleSmall(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DimApp.starsPadding),
                        textAlign = TextAlign.Center,
                        text = TextApp.textNotUnreadNotifications)
                }
            }

            items(items = listNotification, key = { item -> item.id }) { item ->

                val painter =
                    rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                        .data(item.icon).crossfade(true).build())

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = DimApp.screenPadding / 2)) {


                    TextCaption(modifier = Modifier
                        .padding(start = DimApp.screenPadding / 3)
                        .padding(end = DimApp.screenPadding),
                        text = item.created?.secDataDDMMYYYHHMM() ?: "")

                    TextBodyLarge(modifier = Modifier
                        .padding(start = DimApp.screenPadding / 3)
                        .padding(end = DimApp.screenPadding),
                        text = item.title ?: "")

                    TextBodyMedium(modifier = Modifier
                        .padding(top = DimApp.screenPadding / 3)
                        .padding(start = DimApp.screenPadding / 3)
                        .padding(end = DimApp.screenPadding),
                        text = item.body ?: "")

                    Box(modifier = Modifier
                        .padding(vertical = DimApp.screenPadding / 2)
                        .fillMaxWidth()
                        .background(ThemeApp.colors.textLight)
                        .height(DimApp.lineHeight))
                }
            }

        }
    }
}