package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun CheckerApp(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = checkboxColorsApp(),
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    )
}


@Composable
fun SwitchApp(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = switchColorsApp(),
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = thumbContent,
        enabled = enabled,
        colors = colors
    )
}


@Composable
fun RadioButtonApp(
    checked: Boolean,
    onCheckedChange: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = radioButtonColorsApp(),
) {
    RadioButton(
        selected = checked,
        onClick = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    )
}

@Composable
fun radioButtonColorsApp() = RadioButtonDefaults.colors(
    selectedColor = ThemeApp.colors.primary,
    unselectedColor = ThemeApp.colors.onBackground,
    disabledSelectedColor = ThemeApp.colors.textLight,
    disabledUnselectedColor = ThemeApp.colors.textLight
)


@Composable
fun switchColorsApp() = SwitchDefaults.colors(
    checkedThumbColor = ThemeApp.colors.primary,
    checkedTrackColor = ThemeApp.colors.backgroundVariant.copy(.0f),
    checkedBorderColor = ThemeApp.colors.primary,
    checkedIconColor = ThemeApp.colors.primary,
    uncheckedThumbColor = ThemeApp.colors.onBackground,
    uncheckedTrackColor = ThemeApp.colors.backgroundVariant.copy(.0f),
    uncheckedBorderColor = ThemeApp.colors.onBackground,
    uncheckedIconColor = ThemeApp.colors.onBackground,
    disabledCheckedThumbColor = ThemeApp.colors.textLight,
    disabledCheckedTrackColor = ThemeApp.colors.backgroundVariant.copy(.0f),
    disabledCheckedBorderColor = ThemeApp.colors.textLight,
    disabledCheckedIconColor = ThemeApp.colors.textLight,
    disabledUncheckedThumbColor = ThemeApp.colors.textLight,
    disabledUncheckedTrackColor = ThemeApp.colors.textDark.copy(.0f),
    disabledUncheckedBorderColor = ThemeApp.colors.textLight,
    disabledUncheckedIconColor = ThemeApp.colors.textLight
)

@Composable
fun checkboxColorsApp() = CheckboxDefaults.colors(
    checkedColor = ThemeApp.colors.primary,
    uncheckedColor = ThemeApp.colors.onBackground,
    checkmarkColor = ThemeApp.colors.backgroundVariant,
    disabledCheckedColor = ThemeApp.colors.textLight,
    disabledUncheckedColor = ThemeApp.colors.textLight,
    disabledIndeterminateColor = ThemeApp.colors.textLight
)


@Preview
@Composable
private fun SwitchAppTest() {
    TechTheme {
        Column {
            Row() {
                CheckerApp(
                    checked = true,

                    onCheckedChange = {}
                )
                CheckerApp(
                    checked = false,
                    onCheckedChange = {}
                )
                CheckerApp(
                    checked = true,
                    enabled = false,
                    onCheckedChange = {}
                )
                CheckerApp(
                    checked = false,
                    enabled = false,
                    onCheckedChange = {}
                )
            }
            Row() {
                SwitchApp(
                    checked = true,
                    onCheckedChange = {}
                )
                SwitchApp(
                    checked = false,
                    onCheckedChange = {}
                )

                SwitchApp(
                    checked = true,
                    enabled = false,
                    onCheckedChange = {}
                )
                SwitchApp(
                    checked = false,
                    enabled = false,
                    onCheckedChange = {}
                )
            }
            Row() {
                RadioButtonApp(
                    checked = true,
                    onCheckedChange = {}
                )
                RadioButtonApp(
                    checked = false,
                    onCheckedChange = {}
                )
                RadioButtonApp(
                    checked = true,
                    enabled = false,
                    onCheckedChange = {}
                )
                RadioButtonApp(
                    checked = false,
                    enabled = false,
                    onCheckedChange = {}
                )
            }
        }
    }
}