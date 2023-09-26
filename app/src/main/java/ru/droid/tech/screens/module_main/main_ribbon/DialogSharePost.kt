package ru.droid.tech.screens.module_main.main_ribbon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import ru.droid.tech.R
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ChipsApp
import ru.droid.tech.base.common_composable.DropMenuUser
import ru.droid.tech.base.common_composable.IconApp
import ru.droid.tech.base.common_composable.IconButtonApp
import ru.droid.tech.base.common_composable.PanelBottom
import ru.droid.tech.base.common_composable.RadioButtonApp
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextFieldOutlinesApp
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.screens.TypeLink
import ru.data.common.models.res.TextApp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogSharePost(
    onClickShare: (comment: String, typeLink: TypeLink, listUser: List<UserUI>) -> Unit,
    onDismiss: () -> Unit,
    onSearchUser: (String?) -> Unit,
    listUser: List<UserUI>,
) {
    var typeLink by rememberState { TypeLink.IN_MY_RIBBON }
    var comment by rememberState { TextFieldValue() }
    var listUserChoose by rememberState { listOf<UserUI>() }
    val focus = LocalFocusManager.current

    val isEnableButton = remember(typeLink, listUserChoose) {
        when (typeLink) {
            TypeLink.IN_MY_RIBBON -> {
                true
            }

            TypeLink.IN_MESSAGE   -> {
                listUserChoose.isNotEmpty()
            }
        }
    }

    val onShare = remember {
        {
            if (isEnableButton) {
                onClickShare.invoke(comment.text, typeLink, listUserChoose)
            }
        }
    }

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .background(ThemeApp.colors.background)
    ) {
        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textShare
        )
        BoxSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    typeLink = TypeLink.IN_MY_RIBBON
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonApp(
                checked = typeLink == TypeLink.IN_MY_RIBBON,
                onCheckedChange = { typeLink = TypeLink.IN_MY_RIBBON })
            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = TypeLink.IN_MY_RIBBON.getText()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    typeLink = TypeLink.IN_MESSAGE
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonApp(
                checked = typeLink == TypeLink.IN_MESSAGE,
                onCheckedChange = { typeLink = TypeLink.IN_MESSAGE })
            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = TypeLink.IN_MESSAGE.getText()
            )
        }
        BoxSpacer()
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding),
            visible = typeLink == TypeLink.IN_MESSAGE
        ) {
            Column {
                DropMenuUser(
                    items = listUser,
                    modifier = Modifier.fillMaxWidth(),
                    enterNameUser = onSearchUser,
                    checkItem = {
                        it?.let { listUserChoose = (listUserChoose + listOf(it)) }
                        onSearchUser.invoke(null)
                    },
                    locationChooser = ""
                )

                FlowRow {
                    listUserChoose.forEach { userItem ->
                        ChipsApp(
                            modifier = Modifier.padding(DimApp.screenPadding * .3f),
                            onClick = {
                                listUserChoose = listUserChoose.filter { userItem.id != it.id }
                            },
                            text = userItem.getNameAndLastName(),
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null)
                            }
                        )
                    }
                }
                BoxSpacer()
            }
        }

        TextFieldOutlinesApp(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding),
            value = comment,
            onValueChange = { comment = it },
            trailingIcon = {
                if (comment.text.isNotEmpty()) {
                    IconButtonApp(onClick = { comment = TextFieldValue() }) {
                        IconApp(painter = rememberImageRaw(R.raw.ic_close))
                    }
                }
            },
            placeholder = {
                Text(text = TextApp.textComment)
            },
            keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        PanelBottom {
            ButtonAccentApp(
                modifier = Modifier
                    .padding(horizontal = DimApp.screenPadding)
                    .fillMaxWidth(),
                enabled = isEnableButton,
                onClick = {
                    onShare.invoke()
                    onDismiss.invoke()
                },
                text = TextApp.textSend
            )
        }
    }
}
