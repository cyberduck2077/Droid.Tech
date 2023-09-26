package ru.droid.tech.screens.module_main.profile_redaction

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.DialogCropImage
import ru.droid.tech.base.common_composable.DialogGetImage
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelNavBackTop
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState

class ProfileRedactionScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        var imageUriCamera by rememberState<Uri?> { null }
        var getImage by rememberState { false }
        LifeScreen(onResume = { model.getMe() })

        ProfileRedactionScr(
            onClickBack = model::goBackStack,
            imageUri = userData.avatar,
            onClickNewImage = { getImage = true },
            onClickPersonalData = model::goToPersonalData,
            onClickInterests = model::goToInterestsData,
            onClickDroid = model::goToDroidData,
            onClickContacts = model::goToContactsData,
        )

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
                },
            )
        }
    }
}

@Composable
private fun ProfileRedactionScr(
    onClickBack: () -> Unit,
    onClickNewImage: () -> Unit,
    onClickPersonalData: () -> Unit,
    onClickInterests: () -> Unit,
    onClickDroid: () -> Unit,
    onClickContacts: () -> Unit,
    imageUri: Any?,
) {
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
            text = TextApp.titleEditProfile
        )
        BoxSpacer()
        Column(modifier = Modifier.padding(horizontal = DimApp.screenPadding)) {
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
        }
        BoxSpacer()
        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
        ButtonInRow(
            onClick = onClickPersonalData,
            text = TextApp.titlePersonalData
        )
        ButtonInRow(
            onClick = onClickInterests,
            text = TextApp.textInterests
        )

        ButtonInRow(
            onClick = onClickDroid,
            text = TextApp.holderDroid
        )

        ButtonInRow(
            onClick = onClickContacts,
            text = TextApp.holderContacts
        )
    }
}

@Composable
private fun ButtonInRow(
    onClick: () -> Unit,
    text: String,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightButtonInLine)
            .clickableRipple {
                onClick.invoke()
            }
            .padding(start = DimApp.screenPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        TextBodyMedium(
            maxLines = 1,
            text = text
        )

        BoxFillWeight()

        IconButtonApp(
            modifier = Modifier,
            onClick = onClick
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_chevron_right))
        }
    }
}