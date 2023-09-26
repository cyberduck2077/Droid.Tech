package ru.droid.tech.screens.module_main.wish_update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxFillWeight
import ru.droid.tech.base.common_composable.BoxImageLoad
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonWeakApp
import ru.droid.tech.base.common_composable.DialogGetImage
import ru.droid.tech.base.common_composable.DropMenuApp
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.BackPressHandler
import ru.droid.tech.base.util.LifeScreen
import ru.droid.tech.base.util.getQualifiedName
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberModel
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.WishUIShort
import ru.data.common.models.local.maps.WishlistUI
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.onlyDigit

class UpdateWish(private val idWish: Int) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<UpdateWishModel>()
        val id by rememberState { idWish }

        LifeScreen(onStart = {
            model.getWish(id)
        })

        val wish by model.wish.collectAsState()
        val listWishlists by model.listWishlists.collectAsState()

        BackPressHandler(onBackPressed = model::goBackStack)

        UpdateWishScr(
            onClickCancel = model::goBackStack,
            listWishlists = listWishlists,
            onClickSend = model::sendNewWish,
            onSendImage = model::sendImage,
            image = wish?.cover,
            title = wish?.title,
            description = wish?.description,
            price = wish?.price?.div(TextApp.divForeRub)?.toString(),
            uriLink = wish?.link,
            chooseWishlist = wish?.wishlist,
        )
    }
}

@Composable
private fun UpdateWishScr(
    image: String?,
    title: String?,
    description: String?,
    price: String?,
    uriLink: String?,
    chooseWishlist: WishUIShort?,
    listWishlists: List<WishlistUI>,
    onClickCancel: () -> Unit,
    onSendImage: (image: String) -> Unit,
    onClickSend: (
        name: String,
        description: String,
        price: String,
        link: String,
        wishlist: WishlistUI,
    ) -> Unit,
) {

    var imageEnter by rememberState(image) { image }
    var titleEnter by rememberState(title) { TextFieldValue(title ?: "") }
    var descriptionEnter by rememberState(description) { TextFieldValue(description ?: "") }
    var priceEnter by rememberState(price) { TextFieldValue(price ?: "") }
    var uriLinkEnter by rememberState(uriLink) { TextFieldValue(uriLink ?: "") }
    var chooseWishlistEnter by rememberState(
        chooseWishlist,
        listWishlists
    ) { listWishlists.firstOrNull { it.id == chooseWishlist?.id } }
    var isGetImageOpenDialog by rememberState { false }

    val enabledButton = remember(
        titleEnter,
        uriLinkEnter,
        descriptionEnter,
        priceEnter,
        chooseWishlistEnter
    ) {

        val isFull = titleEnter.text.isNotEmpty() &&
                descriptionEnter.text.isNotEmpty() &&
                priceEnter.text.isNotEmpty() &&
                chooseWishlistEnter != null

        val isChange = titleEnter.text != title ||
                descriptionEnter.text != description ||
                priceEnter.text != price ||
                uriLinkEnter.text != uriLink ||
                chooseWishlistEnter?.id != chooseWishlist?.id

        isFull && isChange
    }

    val onSend = remember(enabledButton) {
        {
            val chooseWishlistNotNull = chooseWishlistEnter
            if (enabledButton
                && chooseWishlistNotNull != null) {
                onClickSend.invoke(
                    titleEnter.text,
                    descriptionEnter.text,
                    priceEnter.text,
                    uriLinkEnter.text,
                    chooseWishlistNotNull
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
    ) {
        PanelTopNewWish(
            onClickCancel = onClickCancel,
        )

        ContentEnterNewWish(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(DimApp.screenPadding),
            listWishlists = listWishlists,
            image = imageEnter,
            name = titleEnter,
            description = descriptionEnter,
            price = priceEnter,
            uriLink = uriLinkEnter,
            chooseWishlist = chooseWishlistEnter,
            onClickAddImage = {
                isGetImageOpenDialog = true
            },
            onClickDeleteImage = { imageEnter = null },
            nameField = { titleEnter = it },
            descriptionField = { descriptionEnter = it },
            priceField = {
                val clearText = it.text.onlyDigit()
                if (clearText.length <= 8) {
                    priceEnter = if (clearText != it.text) {
                        TextFieldValue(text = clearText, TextRange(clearText.length))
                    } else {
                        it
                    }
                }
            },
            uriLinkField = { uriLinkEnter = it },
            chooseWishlistField = { chooseWishlistEnter = it },
        )

        BoxFillWeight()
        PanelBottom {
            ButtonWeakApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                onClick = onClickCancel,
                text = TextApp.titleCancel
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                enabled = enabledButton,
                onClick = onSend,
                text = TextApp.holderSave
            )
        }
    }

    if (isGetImageOpenDialog) {
        DialogGetImage(
            onDismiss = { isGetImageOpenDialog = false },
            uploadPhoto = { onSendImage.invoke(it.toString()) })
    }
}

@Composable
private fun ColumnScope.ContentEnterNewWish(
    modifier: Modifier,
    listWishlists: List<WishlistUI>,
    image: String?,
    onClickAddImage: () -> Unit,
    onClickDeleteImage: () -> Unit,
    name: TextFieldValue,
    nameField: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    descriptionField: (TextFieldValue) -> Unit,
    price: TextFieldValue,
    priceField: (TextFieldValue) -> Unit,
    uriLink: TextFieldValue,
    uriLinkField: (TextFieldValue) -> Unit,
    chooseWishlist: WishlistUI?,
    chooseWishlistField: (WishlistUI?) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier

    ) {

        BoxImageLoad(
            modifier = Modifier
                .size(DimApp.sizeAddImageInWish)
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    onClickAddImage.invoke()
                },
            contentScale = ContentScale.Crop,
            drawableError = R.drawable.stub_photo_v2,
            drawablePlaceholder = R.drawable.stub_photo_v2,
            image = image,
        ) {
            if (image != null) {
                IconButtonApp(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onClickDeleteImage
                ) {
                    Icon(
                        painter = rememberImageRaw(id = R.raw.ic_close),
                        contentDescription = "",
                        tint = ThemeApp.colors.onPrimary
                    )
                }
            }
        }

        BoxSpacer()
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = nameField,
            placeholder = { Text(text = TextApp.textNameTitle) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
        )

        BoxSpacer()
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = descriptionField,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            placeholder = { Text(text = TextApp.textDescription) },
        )

        BoxSpacer()
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = price,
            onValueChange = priceField,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            placeholder = { Text(text = TextApp.textPriceRub) },
            suffix = { Text(text = TextApp.textRub) }
        )

        BoxSpacer()
        TextFieldOutlinesApp(
            modifier = Modifier.fillMaxWidth(),
            value = uriLink,
            onValueChange = uriLinkField,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            placeholder = { Text(text = TextApp.textAddALink) },
        )

        BoxSpacer()
        DropMenuApp(
            items = listWishlists,
            modifier = Modifier.fillMaxWidth(),
            checkItem = chooseWishlistField,
            chooserText = chooseWishlist?.title ?: "",
            placeholderText = TextApp.textListWish,
            menuItem = { item ->
                TextBodyLarge(text = item.title ?: "")
                FillLineHorizontal(modifier = Modifier.fillMaxWidth())
            }
        )
        BoxSpacer()
    }
}

@Composable
private fun PanelTopNewWish(
    onClickCancel: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButtonApp(
            modifier = Modifier
                .padding(horizontal = DimApp.screenPadding * .5f),
            onClick = onClickCancel
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_close))
        }

        TextBodyLarge(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Start,
            text = TextApp.textChangeDesire
        )
    }
}