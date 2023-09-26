package ru.droid.tech.screens.module_main.show_Droid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.droid.tech.base.common_composable.BoxSpacer
import ru.droid.tech.base.common_composable.ButtonAccentApp
import ru.droid.tech.base.common_composable.ButtonAccentTextApp
import ru.droid.tech.base.common_composable.CheckerApp
import ru.droid.tech.base.common_composable.DropMenuApp
import ru.droid.tech.base.common_composable.DropMenuCities
import ru.droid.tech.base.common_composable.FillLineHorizontal
import ru.droid.tech.base.common_composable.TextBodyLarge
import ru.droid.tech.base.common_composable.TextBodyMedium
import ru.droid.tech.base.res.DimApp
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import ru.data.common.models.data_base.City
import ru.data.common.models.local.maps.Gender

@Composable
fun ContentDialogSearchUsers(
    ageFrom: Int?,
    ageTo: Int?,
    gender: Gender?,
    locationChoose: City?,
    locations: List<City>,
    onSearchLocation: (text: String?) -> Unit,
    onDismiss: () -> Unit,
    onFilterUserSearch: (
        genderNew: Gender?,
        ageFromNew: Int?,
        ageToNew: Int?,
        locationChooseNew: City?
    ) -> Unit,
) {

    var locationEnter by rememberState(locationChoose) { locationChoose }
    var genderEnter by rememberState(gender) { gender }


    var ageFromEnter by rememberState(ageFrom) { ageFrom }
    var ageToEnter by rememberState(ageTo) { ageTo }

    val ageCountMin = remember(ageToEnter){(1..(ageToEnter ?: 120)).toList()}
    val ageCountMax = remember(ageFromEnter){ ((ageFromEnter ?: 1)..120).toList()}

    val onFilter = remember() {
        {
            onFilterUserSearch.invoke(
                genderEnter,
                ageFromEnter,
                ageToEnter,
                locationEnter

            )
        }
    }

    val onFilterNull = remember() {
        {
            onFilterUserSearch.invoke(
                null,
                null,
                null,
                null,

                )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ThemeApp.colors.background)
    ) {

        Column(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding)
        ) {
            TextBodyLarge(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = TextApp.textSearchFilter
            )
            BoxSpacer()
            TextBodyLarge(text = TextApp.textCity)
            BoxSpacer(0.5f)
            DropMenuCities(
                items = locations,
                modifier = Modifier.fillMaxWidth(),
                enterNameLocation = onSearchLocation,
                checkItem = { locationEnter = it },
                locationChooser = locationEnter?.name,
            )
            BoxSpacer()
            TextBodyLarge(text = TextApp.textAge)
            BoxSpacer(0.5f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                DropMenuApp(
                    items = ageCountMin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    checkItem = { ageFromEnter = it },
                    chooserText = ageFromEnter?.toString() ?: "",
                    placeholderText = TextApp.textFrom,
                    menuItem = { item ->
                        TextBodyLarge(text = item.toString())
                        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                    }
                )

                Box(
                    modifier = Modifier
                        .padding(DimApp.textPaddingMin)
                        .height(DimApp.lineHeight)
                        .width(DimApp.screenPadding)
                        .background(ThemeApp.colors.borderLight)
                )

                DropMenuApp(
                    items = ageCountMax,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    checkItem = { ageToEnter = it },
                    chooserText = ageToEnter?.toString() ?: "",
                    placeholderText = TextApp.textBefore,
                    menuItem = { item ->
                        TextBodyLarge(text = item.toString())
                        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                    }
                )
            }
            BoxSpacer()
            TextBodyLarge(text = TextApp.textGender_)
            BoxSpacer(0.5f)
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckerApp(
                checked = genderEnter == null,
                onCheckedChange = {
                    genderEnter = null
                })
            TextBodyMedium(text = TextApp.textAnswer)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckerApp(
                checked = genderEnter == Gender.MAN,
                onCheckedChange = {
                    genderEnter = Gender.MAN
                })
            TextBodyMedium(text = TextApp.textGenderMan)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckerApp(
                checked = genderEnter == Gender.WOMAN,
                onCheckedChange = {
                    genderEnter = Gender.WOMAN
                })
            TextBodyMedium(text = TextApp.textGenderWoman)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)
        ) {
            ButtonAccentTextApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    onFilterNull.invoke()
                    onDismiss.invoke()
                },
                text = TextApp.titleCancel
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    onFilter.invoke()
                    onDismiss.invoke()
                },
                text = TextApp.holderSave
            )
        }
        BoxSpacer()
    }

}