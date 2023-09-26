package ru.droid.tech.base.common_composable

import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.droid.tech.R
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw
import ru.droid.tech.base.util.rememberState


@Composable
fun ButtonAccentApp(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ThemeApp.shape.smallAll,
    colors: ColorButtonApp = colorsButtonAccentApp(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentStart: @Composable RowScope.() -> Unit = {},
    contentEnd: @Composable RowScope.() -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            contentStart.invoke(this)
            TextButtonMinLine(
                color = if (enabled) colors.contentColor else colors.disabledContentColor,
                text = text
            )
            contentEnd.invoke(this)
        },
    )
}


@Composable
fun ButtonAccentTextApp(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ThemeApp.shape.smallAll,
    colors: ColorButtonApp = colorsButtonAccentTextApp(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentStart: @Composable RowScope.() -> Unit = {},
    contentEnd: @Composable RowScope.() -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            contentStart.invoke(this)
            TextButtonMinLine(
                color = if (enabled) colors.contentColor else colors.disabledContentColor,
                text = text
            )
            contentEnd.invoke(this)
        },
    )
}

@Composable
fun TextButtonApp(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ThemeApp.shape.smallAll,
    styleText: TextStyle = ThemeApp.typography.button,
    colors: ColorButtonApp = colorsButtonAccentTextApp(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    ifCorrectTextSize: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentStart: @Composable RowScope.() -> Unit = {},
    contentEnd: @Composable RowScope.() -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor.copy(0f),
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor.copy(0f),
            disabledContentColor = colors.disabledContentColor
        ),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            contentStart.invoke(this)

            if (ifCorrectTextSize) {
                TextButtonMinLine(
                    color = if (enabled) colors.contentColor else colors.disabledContentColor,
                    style = styleText,
                    text = text
                )
            }

            if (!ifCorrectTextSize) {
                Text(
                    text = text,
                    color = if (enabled) colors.contentColor else colors.disabledContentColor,
                    style = styleText,
                    softWrap = false,
                    maxLines = 1,
                )
            }
            contentEnd.invoke(this)
        },
    )
}

@Composable
fun ButtonWeakApp(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ThemeApp.shape.smallAll,
    colors: ColorButtonApp = colorsButtonWeakApp(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentStart: @Composable RowScope.() -> Unit = {},
    contentEnd: @Composable RowScope.() -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            contentStart.invoke(this)
            TextButtonMinLine(
                color = if (enabled) colors.contentColor else colors.disabledContentColor,
                text = text
            )
            contentEnd.invoke(this)
        },
    )
}

@Composable
fun ButtonWeakSquareApp(
    onClick: () -> Unit,
    @RawRes icon: Int = R.raw.ic_add,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ThemeApp.shape.smallAll,
    colors: ColorButtonApp = colorsButtonWeakApp(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = border,
        interactionSource = interactionSource
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                Modifier
                    .size(ButtonDefaults.MinHeight)
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconApp(
                    painter = rememberImageRaw(id = icon),
                    tint = LocalContentColor.current
                )
            }
        }
    }
}


@Composable
fun IconButtonApp(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ColorButtonApp = colorsIconButtonApp(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun FloatingActionButtonApp(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = ThemeApp.colors.containerVariant,
    contentColor: Color = ThemeApp.colors.onBackground,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun colorsButtonAccentApp(
    containerColor: Color = ThemeApp.colors.primary,
    contentColor: Color = ThemeApp.colors.onPrimary,
    disabledContainerColor: Color = ThemeApp.colors.container,
    disabledContentColor: Color = ThemeApp.colors.textLight
) = ColorButtonApp(
    containerColor = containerColor,
    contentColor = contentColor,
    disabledContainerColor = disabledContainerColor,
    disabledContentColor = disabledContentColor
)

@Composable
fun colorsButtonAccentTextApp(
    containerColor: Color = ThemeApp.colors.background,
    contentColor: Color = ThemeApp.colors.primary,
    disabledContainerColor: Color = ThemeApp.colors.container,
    disabledContentColor: Color = ThemeApp.colors.textLight
) = ColorButtonApp(
    containerColor = containerColor,
    contentColor = contentColor,
    disabledContainerColor = disabledContainerColor,
    disabledContentColor = disabledContentColor
)

@Composable
fun colorsIconButtonApp(
    containerColor: Color = ThemeApp.colors.background.copy(.0f),
    contentColor: Color = ThemeApp.colors.onBackground,
    disabledContainerColor: Color = ThemeApp.colors.container.copy(.0f),
    disabledContentColor: Color = ThemeApp.colors.textLight
) = ColorButtonApp(
    containerColor = containerColor,
    contentColor = contentColor,
    disabledContainerColor = disabledContainerColor,
    disabledContentColor = disabledContentColor
)

data class ColorButtonApp(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)

@Composable
fun colorsButtonWeakApp(
    containerColor: Color = ThemeApp.colors.background,
    contentColor: Color = ThemeApp.colors.textDark,
    disabledContainerColor: Color = ThemeApp.colors.container,
    disabledContentColor: Color = ThemeApp.colors.textLight
) = ColorButtonApp(
    containerColor = containerColor,
    contentColor = contentColor,
    disabledContainerColor = disabledContainerColor,
    disabledContentColor = disabledContentColor
)


@Composable
fun elevationButtonApp() = ButtonDefaults.buttonElevation(
    defaultElevation = 2.dp,
    pressedElevation = 4.dp,
    focusedElevation = 3.dp,
    hoveredElevation = 2.dp,
    disabledElevation = 0.dp
)

@Composable
private fun TextButtonMinLine(
    color: Color,
    text: String,
    style: TextStyle = ThemeApp.typography.button
) {
    var readyToDraw by rememberState { false }
    var textStyle by rememberState { style }
    Text(
        modifier = Modifier
            .drawWithContent {
                if (readyToDraw) drawContent()
            },
        text = text,
        color = color,
        style = textStyle,
        softWrap = false,
        maxLines = 1,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                textStyle =
                    textStyle.copy(fontSize = textStyle.fontSize * 0.95)
            } else {
                readyToDraw = true
            }
        },
    )
}


@Preview(backgroundColor = 0xFFBDAEAE)
@Composable
fun ButtonPreview() {
    TechTheme {
        Column(modifier = Modifier
            .background(ThemeApp.colors.backgroundVariant)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonAccentApp(
                onClick = { /*TODO*/ },
                text = "ButtonAccentApp"
            )

            ButtonAccentApp(
                enabled = false,
                onClick = { /*TODO*/ },
                text = "ButtonAccentApp"
            )

            ButtonWeakApp(
                onClick = { /*TODO*/ },
                text = "ButtonWeakApp"
            )

            ButtonWeakApp(
                enabled = false,
                onClick = { /*TODO*/ },
                text = "ButtonWeakApp"
            )

            ButtonWeakSquareApp(
                enabled = true,
                onClick = { /*TODO*/ },
            )

            ButtonAccentTextApp(
                onClick = { /*TODO*/ },
                text = "ButtonAccentTextApp"
            )

            ButtonAccentTextApp(
                enabled = false,
                onClick = { /*TODO*/ },
                text = "ButtonAccentTextApp"
            )

            TextButtonApp(
                enabled = false,
                onClick = { /*TODO*/ },
                text = "TextButtonApp"
            )

            TextButtonApp(
                enabled = true,
                onClick = { /*TODO*/ },
                text = "TextButtonApp"
            )

            IconButtonApp(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.AccountBox, null)
            }

            IconButtonApp(
                enabled = false,
                onClick = { /*TODO*/ }) {
                Icon(Icons.Default.AccountBox, null)
            }

            FloatingActionButtonApp(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Phone, null)
            }

            FloatingActionButtonApp(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Phone, null)
            }

        }

    }

}