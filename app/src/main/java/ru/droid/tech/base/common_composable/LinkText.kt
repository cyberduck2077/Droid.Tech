package ru.droid.tech.base.common_composable

import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontDroid
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import ru.droid.tech.base.theme.ThemeApp

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    hyperLinks: List<String>,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = ThemeApp.colors.textDark,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    linkTextColor: Color = ThemeApp.colors.primary,
    linkStyle: TextStyle = textStyle.copy(color = linkTextColor),
    onClickText: (String, Int)->Unit,
) {
    val tag = "URL"
    val annotatedString = buildAnnotatedString {
        append(fullText)

        for(value in hyperLinks){
            val startIndex = fullText.indexOf(value)
            val endIndex = startIndex + value.length
            addStyle(
                style = SpanStyle(
                    color = linkStyle.color,
                    fontSize = linkStyle.fontSize,
                    fontWeight = linkStyle.fontWeight,
                    textDecoration = linkStyle.textDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = tag,
                annotation = value,
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = textStyle.toParagraphStyle(),
            start = 0,
            end = fullText.length
        )
    }


    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = textStyle.copy(color = textColor, textAlign = textAlign),
        onClick = {
            annotatedString
                .getStringAnnotations(tag, it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    var indexItem = 0
                    hyperLinks.forEachIndexed { index, s ->
                        if (stringAnnotation.item == s) indexItem = index
                    }
                    onClickText(stringAnnotation.item, indexItem)
                }
        }
    )
}


@Composable
fun TextLinksWeb(
    text: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    tag: String = "URL",
    regex: Regex = """(http(s)?://)?([\w-]+\.)+[\w-]+(/[\w- ;,./?%&=]*)?""".toRegex(),
    linkTgHttps: String  = "https://t.me/",
    telegramUsernameRegex: Regex = """@(\w+)""".toRegex(),
    textColor: Color = ThemeApp.colors.onBackground,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontDroid: FontDroid? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = ThemeApp.typography.bodyLarge
) {

    val annotatedText = buildAnnotatedString {
        append(text)
        regex.findAll(text).forEach {
            val range = it.range
            addStyle(SpanStyle(color = ThemeApp.colors.primary, textDecoration = TextDecoration.Underline),
                range.first, range.last +1)
            addStringAnnotation(tag, it.value, range.first, range.last+1)
        }

        telegramUsernameRegex.findAll(text).forEach {
            val range = it.range
            addStyle(SpanStyle(color = ThemeApp.colors.primary, textDecoration = TextDecoration.Underline),
                range.first, range.last + 1)
            val username = try {
                it.groups[1]?.value
            } catch (e:Exception){
                e.printStackTrace()
                null
            }
            if (username != null) {
                addStringAnnotation(tag, linkTgHttps + username, range.first, range.last + 1)
            }
        }
    }

    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontDroid = fontDroid,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )
    SelectionContainer {
        ClickableText(
            text = annotatedText,
            modifier = modifier,
            style = mergedStyle,
            softWrap = softWrap,
            overflow = overflow,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            onClick = { position ->
                annotatedText.getStringAnnotations(tag, position, position).firstOrNull()?.let { link ->
                    onClick.invoke(link.item)
                }
            })
    }
}



@Composable
fun TextLinksWebNew(
    text: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    tag: String = "URL",
    regex: Regex = """(http(s)?://)?([\w-]+\.)+[\w-]+(/[\w- ;,./?%&=]*)?""".toRegex(),
    linkTgHttps: String  = "https://t.me/",
    telegramUsernameRegex: Regex = """@(\w+)""".toRegex(),
    textColor: Color = ThemeApp.colors.onBackground,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontDroid: FontDroid? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = ThemeApp.typography.bodyLarge
) {

    fun getWebsiteName(url: String): String {
        val prefix = "https://"
        val trimmedUrl = if (url.startsWith(prefix)) {
            url.removePrefix(prefix)
        } else {
            url
        }
        return trimmedUrl
    }

    val annotatedText = buildAnnotatedString {
        append(text)
        regex.findAll(text).forEach {
            val range = it.range
            addStyle(SpanStyle(color = ThemeApp.colors.primary, textDecoration = TextDecoration.Underline),
                range.first, range.last +1)
            addStringAnnotation(tag, it.value, range.first, range.last+1)
        }

        telegramUsernameRegex.findAll(text).forEach {
            val range = it.range
            addStyle(SpanStyle(color = ThemeApp.colors.primary, textDecoration = TextDecoration.Underline),
                range.first, range.last + 1)
            val username = try {
                it.groups[1]?.value
            } catch (e:Exception){
                e.printStackTrace()
                null
            }
            if (username != null) {
                addStringAnnotation(tag, linkTgHttps + username, range.first, range.last + 1)
            }
        }
    }

    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontDroid = fontDroid,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )
        ClickableText(
            text = annotatedText,
            modifier = modifier,
            style = mergedStyle,
            softWrap = softWrap,
            overflow = overflow,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            onClick = { position ->
                annotatedText.getStringAnnotations(tag, position, position).firstOrNull()?.let { link ->
                    onClick.invoke(link.item)
                }
            })
}
