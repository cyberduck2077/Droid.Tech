package ru.droid.tech.screens.module_main.wish_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.DialogYesOrNo
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.common_composable.TextButtonApp
import ru.droid.tech.base.common_composable.TextButtonStyle
import ru.droid.tech.base.common_composable.TextTitleMedium
import ru.droid.tech.base.common_composable.TextTitleSmall
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberOpenIntentUrl
import ru.droid.tech.base.util.rememberState
import ru.data.common.domain.memory.gDMessage
import ru.data.common.models.local.maps.StatusFulfilled
import ru.data.common.models.res.TextApp

class DetailWish(private val idWish: Int) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<DetailWishModel>()
        val wish by model.wish.collectAsState()
        val helpUri = rememberOpenIntentUrl()
        LifecycleEffect(onStarted = { model.getWish(idWish) })

        BackPressHandler(onBackPressed = model::goBackStack)

        DetailWishScr(
            onClickBack = model::goBackStack,
            image = wish?.cover,
            onClickReedit = model::goToReedit,
            onClickShare = {
                //TODO("DetailWish onClickShare")
                gDMessage("Stub")
            },
            onClickDelete = model::deleteWish,
            onClickLink = { link ->
                helpUri.invoke(link)
            },
            onClickNextStatus = model::sendNewStatus,
            avatar = wish?.user?.avatar,
            name = wish?.user?.getNameAndLastName() ?: "",
            dataCreate = wish?.createdHuman ?: "",
            title = wish?.title ?: "",
            description = wish?.description ?: "",
            price = TextApp.formatRub(wish?.price?.div(TextApp.divForeRub) ?: 0),
            link = wish?.link,
            status = wish?.isFulfilled
        )
    }
}

@Composable
private fun DetailWishScr(
    onClickBack: () -> Unit,
    image: String?,
    onClickReedit: () -> Unit,
    onClickShare: () -> Unit,
    onClickDelete: () -> Unit,
    onClickLink: (String) -> Unit,
    onClickNextStatus: () -> Unit,
    avatar: String?,
    name: String,
    dataCreate: String,
    title: String,
    description: String,
    price: String,
    link: String?,
    status: StatusFulfilled?
) {
    var dialogDelete by rememberState {
        false
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .background(ThemeApp.colors.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(if (image == null) DimApp.heightTopNavigationPanel else 0.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            image?.let {
                BoxImageLoad(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = LocalConfiguration.current.screenWidthDp.dp)
                        .clipToBounds(),
                    modifierImage = Modifier.fillMaxWidth(),
                    image = image,
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = DimApp.screenPadding)
                    .fillMaxWidth()
            ) {
                BoxSpacer()
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(DimApp.iconSizeBig)
                            .clip(CircleShape),
                        drawableError = R.drawable.stab_avatar,
                        drawablePlaceholder = R.drawable.stab_avatar,
                        image = avatar
                    )
                    BoxSpacer()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        TextTitleSmall(text = name, maxLines = 1)
                        TextBodyMedium(text = dataCreate)

                    }
                }
                BoxSpacer()
                TextBodyMedium(text = title)

                BoxSpacer()
                TextBodyMedium(
                    text = TextApp.textComment,
                    color = ThemeApp.colors.textLight
                )

                TextBodyLarge(text = description)

                BoxSpacer()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column() {
                        TextBodyMedium(
                            text = TextApp.textPrice,
                            color = ThemeApp.colors.textLight
                        )

                        TextTitleMedium(text = price)

                    }
                    BoxFillWeight()
                    link?.ifEmpty { null }?.let { link ->
                        val regex = Regex("https?://(?:www\\.|)([\\w.-]+).*")
                        val matchResult = regex.find(link)
                        val domain = matchResult?.groupValues?.getOrNull(1)
                        TextButtonApp(
                            onClick = { onClickLink.invoke(link) },
                            text = domain ?: link.take(8),
                            contentStart = {
                                IconApp(
                                    painter = rememberImageRaw(R.raw.ic_link),
                                    tint = ThemeApp.colors.primary
                                )
                                BoxSpacer()
                            })
                    }

                }
            }

            BoxFillWeight()

            status?.let {
                PanelBottom {
                    ButtonAccentApp(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DimApp.screenPadding),
                        enabled = status.enabledButtonDetailWish(),
                        onClick = onClickNextStatus,
                        text = status.getTextButtonForeWish()
                    )
                }
            }
        }

        PanelTopDetailWish(
            onClickBack = onClickBack,
            onClickReedit = onClickReedit,
            onClickShare = onClickShare,
            onClickDelete = { dialogDelete = true }
        )
    }

    if (dialogDelete) {
        DialogYesOrNo(
            onDismiss = { dialogDelete = false },
            onClick = onClickDelete,
            title = TextApp.textDeleteWishes,
            body = TextApp.textReallyWantToRemoveDesire
        )
    }
}


@Composable
private fun PanelTopDetailWish(
    onClickBack: () -> Unit,
    onClickReedit: () -> Unit,
    onClickShare: () -> Unit,
    onClickDelete: () -> Unit,
) {
    var isDropMenu by rememberState { false }
    val brushCircleShape = Brush.radialGradient(
        colorStops = arrayOf(
            Pair(0.7F, ThemeApp.colors.borderDark.copy(0.3F)),
            Pair(0.8F, ThemeApp.colors.primary.copy(0.0F))
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButtonApp(
            modifier = Modifier
                .padding(horizontal = DimApp.screenPadding * .5f)
                .background(brush = brushCircleShape)
                .clip(CircleShape),
            onClick = onClickBack
        ) {
            IconApp(
                painter = rememberImageRaw(R.raw.ic_arrow_back),
                tint = ThemeApp.colors.onPrimary
            )
        }
        BoxFillWeight()

        DropMenuWish(
            content = {
                IconButtonApp(
                    modifier = Modifier
                        .padding(horizontal = DimApp.screenPadding * .5f)
                        .background(brush = brushCircleShape)
                        .clip(CircleShape),
                    onClick = { isDropMenu = !isDropMenu }
                ) {
                    IconApp(
                        painter = rememberImageRaw(R.raw.ic_mero_vert),
                        tint = ThemeApp.colors.onPrimary
                    )
                }
            },
            expanded = isDropMenu,
            onDismiss = { isDropMenu = false },
            onClickReedit = onClickReedit,
            onClickShare = onClickShare,
            onClickDelete = onClickDelete,
        )


    }
}


@Composable
private fun DropMenuWish(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onClickReedit: () -> Unit,
    onClickShare: () -> Unit,
    onClickDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .clip(ThemeApp.shape.smallAll)
                .background(color = ThemeApp.colors.backgroundVariant),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            var dropMenuExpanded by rememberState { false }

            DropdownMenuItem(
                modifier = Modifier
                    .align(Alignment.Start),
                onClick = {
                    dropMenuExpanded = false
                    onClickReedit.invoke()
                },
                text = {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconApp(
                            painter = rememberImageRaw(R.raw.ic_edit),
                            tint = ThemeApp.colors.textDark
                        )
                        BoxSpacer()
                        TextButtonStyle(text = TextApp.textRemake)
                    }
                })

            DropdownMenuItem(
                modifier = Modifier
                    .align(Alignment.Start),
                onClick = {
                    dropMenuExpanded = false
                    onClickShare.invoke()
                },
                text = {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconApp(
                            painter = rememberImageRaw(R.raw.ic_send_on),
                            tint = ThemeApp.colors.textDark
                        )
                        BoxSpacer()
                        TextButtonStyle(text = TextApp.textShare)
                    }
                })

            DropdownMenuItem(
                modifier = Modifier
                    .align(Alignment.Start),
                onClick = {
                    dropMenuExpanded = false
                    onClickDelete.invoke()
                },
                text = {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconApp(
                            painter = rememberImageRaw(R.raw.ic_delete),
                            tint = ThemeApp.colors.textDark
                        )
                        BoxSpacer()
                        TextButtonStyle(text = TextApp.textDelete)
                    }
                })

        }
    }

}
