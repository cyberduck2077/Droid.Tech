package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ru.droid.tech.R
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberImageRaw

@Composable
fun DialogContentDetailedInformation(
    DroidName: String? = null,
    numDroid: Int? = null,
    birthday: String? = null,
    city: String? = null,
    cityOrigin: String? = null,
    phone: String? = null,
    tg: String? = null,
    aboutYou: String? = null,
    doing: String? = null,
    interests: String? = null,
    likeMusic: String? = null,
    films: String? = null,
    books: String? = null,
    games: String? = null,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textDetailedInformation
        )
        BoxSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconApp(
                modifier = Modifier.padding(end = DimApp.screenPadding),
                tint = ThemeApp.colors.primary,
                painter = rememberImageRaw(id = R.raw.ic_email)
            )
            TextButtonStyle(text = DroidName ?: "", color = ThemeApp.colors.primary)

        }
        FillLineHorizontal(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DimApp.screenPadding)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconApp(
                modifier = Modifier.padding(end = DimApp.screenPadding),
                tint = ThemeApp.colors.primary,
                painter = rememberImageRaw(id = R.raw.ic_group)
            )
            TextButtonStyle(text = TextApp.titleDroid, color = ThemeApp.colors.textDark)
            BoxFillWeight()
            if (numDroid != null)
                TextBodyLarge(text = numDroid.toString())

        }
        FillLineHorizontal(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DimApp.screenPadding)
        )
        if (birthday != null) {
            BoxSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBodyMedium(
                    text = TextApp.textBirthday,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TextBodyMedium(
                    text = birthday, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

            }
        }
        if (city != null) {
            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBodyMedium(
                    text = TextApp.textCity,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TextBodyMedium(
                    text = city, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        if (cityOrigin != null) {
            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBodyMedium(
                    text = TextApp.textOriginalCity,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TextBodyMedium(
                    text = cityOrigin, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        if (phone != null) {
            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBodyMedium(
                    text = TextApp.textPhone,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TextBodyMedium(
                    text = phone, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        if (tg != null) {
            BoxSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBodyMedium(
                    text = TextApp.textTg,
                    color = ThemeApp.colors.textLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TextBodyMedium(
                    text = tg, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        if (aboutYou != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textAboutYourself, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = aboutYou
            )

        }
        if (doing != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textDoing, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = doing
            )

        }
        if (interests != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textInterests, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = interests
            )

        }
        if (likeMusic != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textLikeMusics, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = likeMusic
            )

        }
        if (films != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textLikeFilms, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = films
            )

        }
        if (books != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textLikeBooks, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = books
            )

        }
        if (games != null) {
            BoxSpacer()
            TextBodyMedium(text = TextApp.textLikeGames, color = ThemeApp.colors.textLight)
            TextBodyMedium(
                text = games
            )
        }
        BoxSpacer(1f)
    }
}
