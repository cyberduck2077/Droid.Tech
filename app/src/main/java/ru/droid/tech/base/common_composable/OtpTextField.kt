package ru.droid.tech.base.common_composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState


@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int,
    onOtpTextChange: (String) -> Unit,
    keyboardActionsSend: () -> Unit,
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text)
            }
        },
        keyboardActions = KeyboardActions(onSend = { keyboardActionsSend.invoke() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send,
            keyboardType = KeyboardType.NumberPassword
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CodeFieldBoxChar(
                        index = index,
                        text = otpText
                    )
                    Box(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CodeFieldBoxChar(
    modifier: Modifier = Modifier,
    text: String,
    index: Int,
    background: Color = ThemeApp.colors.backgroundVariant.copy(.7f),
    indicatorUnFocusedColor: Color = ThemeApp.colors.textDark.copy(.8f),
    indicatorFocusedColor: Color = ThemeApp.colors.textDark,
    contentColor: Color = ThemeApp.colors.textDark,
    style: TextStyle = ThemeApp.typography.titleMedium,
    shape: Shape = ThemeApp.shape.smallAll,
    sizeBoxHeight: Dp = 40.dp,
    sizeBoxWidth: Dp = 45.dp,
    border: Dp = DimApp.lineHeight,
) {

    val isFocused = text.length == index
    val char = when {
        index == text.length -> "|"
        index > text.length  -> ""
        else                 -> text.getOrNull(index).toString()
    }

    var alpha by rememberState { 0.5f }

    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            delay(500)
            alpha = if (alpha > 0.1f) 0f else 0.5f
        }
    })

    Box(
        modifier = modifier
            .size(
                width = sizeBoxHeight,
                height = sizeBoxWidth
            )
            .clip(shape)
            .border(
                border = when {
                    isFocused -> BorderStroke(border, indicatorUnFocusedColor)
                    else      -> BorderStroke(border, indicatorFocusedColor)
                },
                shape = shape
            )
            .background(background)
            .padding(border * 3),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = style,
            color = if (isFocused) {
                contentColor.copy(alpha)
            } else {
                contentColor
            },
            textAlign = TextAlign.Center
        )
    }
}
