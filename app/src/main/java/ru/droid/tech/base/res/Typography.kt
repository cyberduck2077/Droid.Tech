package ru.droid.tech.base.res


import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontDroid
import androidx.compose.ui.unit.sp
import ru.droid.tech.R
import ru.droid.tech.base.theme.TypographySchemeApp


val TypographyApp = TypographySchemeApp(
    titleLarge = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_semi_bold)),
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_regular)),
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_bold)),
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_regular)),
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_regular)),
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_semi_bold)),
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_regular)),
        fontSize = 12.sp
    ),
    label = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_extra_bold)),
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontDroid(Font(R.font.nunito_sans_extra_bold)),
        fontSize = 12.sp
    )
)
