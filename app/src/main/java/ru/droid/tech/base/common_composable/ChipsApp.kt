package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.droid.tech.base.extension.clickableNoRipple
import ru.droid.tech.base.extension.clickableRipple
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp


@Composable
fun ChipsApp(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: ColorChipsApp = colorsColorChipsApp(),
) {

    CompositionLocalProvider(
        LocalContentColor provides if (enabled) colors.labelColor else colors.disabledLabelColor,
        LocalTextStyle provides ThemeApp.typography.bodyLarge.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight(400)
        )
    ) {
        Row(modifier = modifier
            .clip(ThemeApp.shape.smallAll)
            .border(DimApp.lineHeight, colors.borderColor, ThemeApp.shape.smallAll)
            .background(if (enabled) colors.containerColor else colors.containerColor)
            .then(if (enabled) Modifier .clickableNoRipple { onClick.invoke() } else Modifier)
            .padding(vertical = DimApp.screenPadding * .2f)
            .padding(end = DimApp.screenPadding * .2f)
            .padding(start = DimApp.screenPadding * .5f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, maxLines = 1)
            trailingIcon?.invoke()
        }
    }
}


@Composable
fun colorsColorChipsApp(
    containerColor: Color = ThemeApp.colors.container,
    labelColor: Color = ThemeApp.colors.onBackground,
    iconContentColor: Color = ThemeApp.colors.container.copy(.0f),
    disabledContainerColor: Color = ThemeApp.colors.container,
    disabledLabelColor: Color = ThemeApp.colors.textLight,
    disabledIconContentColor: Color = ThemeApp.colors.textLight,
    borderColor: Color = ThemeApp.colors.textLight,
    disabledBorderColor: Color = ThemeApp.colors.textLight,
    borderWidth: Dp = DimApp.lineHeight,
) = ColorChipsApp(
    containerColor = containerColor,
    labelColor = labelColor,
    iconContentColor = iconContentColor,
    disabledContainerColor = disabledContainerColor,
    disabledLabelColor = disabledLabelColor,
    disabledIconContentColor = disabledIconContentColor,
    borderColor = borderColor,
    disabledBorderColor = disabledBorderColor,
    borderWidth = borderWidth
)


data class ColorChipsApp(
    val containerColor: Color,
    val labelColor: Color,
    val iconContentColor: Color,
    val disabledContainerColor: Color,
    val disabledLabelColor: Color,
    val disabledIconContentColor: Color,

    val borderColor: Color,
    val disabledBorderColor: Color,
    val borderWidth: Dp,
)


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TestChips() {

    TechTheme() {

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)) {

//            AssistChip(
//                enabled = true,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "AssistChip") }
//            )
//
//            AssistChip(
//                enabled = false,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "AssistChip") }
//            )
//
//            InputChip(
//                selected = true,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "InputChip") }
//
//            )
//
//            InputChip(
//                selected = false,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "InputChip") }
//
//            )
//
//            FilterChip(
//                selected = true,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "FilterChip") })
//
//
//            FilterChip(
//                selected = false,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "FilterChip") })

            ChipsApp(
                enabled = true,
                onClick = { /*TODO*/ },
                text = "SuggestionChip false",
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            )

            BoxSpacer()
            ChipsApp(
                enabled = false,
                onClick = { /*TODO*/ },
                text = "SuggestionChip false",
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            )

//            ElevatedSuggestionChip(
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "ElevatedSuggestionChip") })
//
//            ElevatedAssistChip(
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "ElevatedAssistChip") })
//
//
//            ElevatedFilterChip(
//                selected = true,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "ElevatedFilterChip") })
//
//            ElevatedFilterChip(
//                selected = false,
//                onClick = { /*TODO*/ },
//                label = { TextTitleMedium(text = "ElevatedFilterChip") })


        }

    }
}