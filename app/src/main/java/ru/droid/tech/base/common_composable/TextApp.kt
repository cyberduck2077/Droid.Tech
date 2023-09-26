package ru.droid.tech.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.theme.TechTheme
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun TextTitleLarge(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.titleLarge
    )
}

@Composable
fun TextTitleMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.titleMedium
    )
}

@Composable
fun TextTitleSmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.titleSmall
    )
}

@Composable
fun TextBodyLarge(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.bodyLarge
    )
}

@Composable
fun TextBodyMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.bodyMedium
    )
}

@Composable
fun TextButtonStyle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.button
    )
}

@Composable
fun TextCaption(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.caption
    )
}

@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.label
    )
}

@Composable
fun TextLabelSmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.labelSmall
    )
}


@Composable
fun TextTitleLarge(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.titleLarge,
        inlineContent = inlineContent
    )
}

@Composable
fun TextTitleMedium(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = ThemeApp.typography.titleMedium
    )
}

@Composable
fun TextTitleSmall(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.titleSmall
    )
}

@Composable
fun TextBodyLarge(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.bodyLarge
    )
}

@Composable
fun TextBodyMedium(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.bodyMedium
    )
}


@Composable
fun TextButtonStyle(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.button
    )
}

@Composable
fun TextCaption(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.caption
    )
}

@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.label
    )
}

@Composable
fun TextLabelSmall(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    color: Color = ThemeApp.colors.textDark,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        inlineContent = inlineContent,
        style = ThemeApp.typography.labelSmall
    )
}

@Preview
@Composable
private fun TestTextApp() {
    TechTheme {
        val text1 = "Test Text App !!!!"
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextTitleLarge(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextTitleMedium(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextTitleSmall(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextBodyLarge(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextBodyMedium(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextButtonStyle(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextCaption(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextLabel(text = text1,)
            Box(modifier = Modifier.size(10.dp))
            TextLabelSmall(text = text1,)
            Box(modifier = Modifier.size(10.dp))
        }
    }
}


