package ru.droid.tech.base.util.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.datetime.*
import ru.droid.tech.base.theme.ThemeApp
import ru.droid.tech.base.util.rememberState
import java.time.YearMonth
import java.util.Locale
import java.time.format.TextStyle as  TextStyleF

@Composable
fun DataPickerddmmyyy(
    modifier: Modifier = Modifier,
    startTimeMillis: Long? = null,
    firstColor: Color = ThemeApp.colors.primary,
    secondColor: Color = ThemeApp.colors.textLight,
    firstStyle: TextStyle = ThemeApp.typography.titleMedium,
    secondStyle: TextStyle = ThemeApp.typography.bodyLarge,
    thirdStyle: TextStyle = ThemeApp.typography.bodyMedium,
    onDataChose: (Long) -> Unit,
) {

    var chooseYear by rememberState { getChooseYear(startTimeMillis) }
    var chooseMonth by rememberState { getChooseMonth(startTimeMillis) }
    var chooseDay by rememberState { getChooseDay(startTimeMillis) }

    val listYear by rememberState { getHundredYears() }
    val listMonth by rememberState { getMonths() }
    var listDay by rememberState { getDays(chooseMonth, chooseYear) }


    LaunchedEffect(key1 = chooseYear, key2 = chooseMonth, key3 = chooseDay, block = {
        onDataChose.invoke(getMillis(chooseYear, chooseMonth, chooseDay))
    })

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfiniteItemsPicker(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f),
            items = listDay,
            indexInit = listDay.indexOfFirst { it == chooseDay }.coerceAtLeast(0),
            onItemSelected = { it?.let { chooseDay = it } },
        ) { item, visibleNumb ->

            val color by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstColor
                    VisiblyItem.SECOND -> secondColor
                    VisiblyItem.THIRD  -> secondColor.copy(.4f)
                    else               -> secondColor.copy(.1f)
                }
            }

            val style by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstStyle
                    VisiblyItem.SECOND -> secondStyle
                    else               -> thirdStyle
                }
            }
            TextDataPicker(
                modifier = Modifier.align(Alignment.Center),
                text = item,
                color = color,
                style = style
            )
        }

        InfiniteItemsPicker(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            items = listMonth,
            indexInit = listMonth.indexOfFirst { it == chooseMonth }.coerceAtLeast(0),
            onItemSelected = { it?.let { chooseMonth = it
                listDay =  getDays(chooseMonth, chooseYear)
            } },
        ) { item, visibleNumb ->

            val color by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstColor
                    VisiblyItem.SECOND -> secondColor
                    VisiblyItem.THIRD  -> secondColor.copy(.4f)
                    else               -> secondColor.copy(.1f)
                }
            }

            val style by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstStyle
                    VisiblyItem.SECOND -> secondStyle
                    else               -> thirdStyle
                }
            }
            TextDataPicker(
                modifier = Modifier.align(Alignment.Center),
                text = getNameMonth(item),
                color = color,
                style = style
            )
        }
        InfiniteItemsPicker(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f),
            items = listYear,
            indexInit = listYear.indexOfFirst { it == chooseYear }.coerceAtLeast(0),
            onItemSelected = { it?.let { chooseYear = it
                listDay =  getDays(chooseMonth, chooseYear)
            } },
        ) { item, visibleNumb ->

            val color by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstColor
                    VisiblyItem.SECOND -> secondColor
                    VisiblyItem.THIRD  -> secondColor.copy(.4f)
                    else               -> secondColor.copy(.1f)
                }
            }

            val style by rememberState(visibleNumb) {
                when (visibleNumb) {
                    VisiblyItem.FIRST  -> firstStyle
                    VisiblyItem.SECOND -> secondStyle
                    else               -> thirdStyle
                }
            }
            TextDataPicker(
                modifier = Modifier.align(Alignment.Center),
                text = item,
                color = color,
                style = style
            )
        }
    }
}

@Composable
private fun TextDataPicker(
    modifier: Modifier = Modifier,
    text: Any?,
    textAlign: TextAlign = TextAlign.Center,
    color: Color,
    style: TextStyle,

    ) {
    Text(
        text = text.toString(),
        modifier = modifier,
        textAlign = textAlign,
        color = color,
        softWrap = false,
        maxLines = 1,
        overflow = TextOverflow.Visible,
        style = style
    )
}

private fun getMillis(year: Int, month: Int, day: Int): Long {
    val localDate = try {
        LocalDate
        LocalDateTime(year, Month.of(month), day, 0, 0)
    } catch (e : Exception){
        e.printStackTrace()
        LocalDateTime(year, Month.of(month), 28, 0, 0)
    }
    val localDateTime = localDate.toInstant(UtcOffset.ZERO)
    return localDateTime.toEpochMilliseconds()
}


private fun getChooseYear(currentTime: Long? = null): Int {
    val time = currentTime ?: Clock.System.now().toEpochMilliseconds()
    val instantEpoc = Instant.fromEpochMilliseconds(time)
    return  instantEpoc.toLocalDateTime(TimeZone.UTC).year
}

private fun getHundredYears(): List<Int> {
    val currentTime = Clock.System.now().toEpochMilliseconds()
    val currentYear = getChooseYear(currentTime)
    val years = (currentYear - 100)..currentYear
    return years.toList()
}


private fun getChooseMonth(currentTime: Long? = null): Int {
    val time = currentTime ?: Clock.System.now().toEpochMilliseconds()
    val instantEpoc = Instant.fromEpochMilliseconds(time)
    return instantEpoc.toLocalDateTime(TimeZone.UTC).month.value
}


private fun getChooseDay(currentTime: Long? = null): Int {
    val time = currentTime ?: Clock.System.now().toEpochMilliseconds()
    val instantEpoc = Instant.fromEpochMilliseconds(time)
    return instantEpoc.toLocalDateTime(TimeZone.UTC).dayOfMonth
}

private fun getMonths(): List<Int> {
    return Month.values().map { it.value }
}

private fun getDays(month: Int, year: Int): List<Int> {
    val yearMonth = YearMonth.of(year, Month.of(month))
    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()
    return (firstDay.dayOfMonth..lastDay.dayOfMonth).toList()

}

fun getNameMonth(numb: Int): String {
    return Month.of(numb).getDisplayName(TextStyleF.FULL, LocaleRuPicker)
}

private val LocaleRuPicker = Locale("ru", "RU")