package ru.data.common.models.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

fun String.onlyDigit() = Regex("[^0-9]").replace(this, "")

private val LocaleRuTime = Locale("ru", "RU")

fun Long.secDataDDMMYYYHHMM(): String = SimpleDateFormat("dd.MM.yyyy HH:mm", LocaleRuTime).format(this * 1000L)
fun Long.secDateDDMMYYYY(): String = SimpleDateFormat("dd.MM.yyyy", LocaleRuTime).format(this * 1000L)

fun Long.millDateDDMMYYYY(): String = SimpleDateFormat("dd.MM.yyyy", LocaleRuTime).format(this)
fun Long.millDateDDMMYYYYHHMM(): String = SimpleDateFormat("dd.MM.yyyy  HH:mm", LocaleRuTime).format(this)
fun Long.millDateDDMMYYYYg(): String = SimpleDateFormat("dd MMMM yyyy г.", LocaleRuTime).format(this)

fun Long.millToDay(): Int = SimpleDateFormat("dd", LocaleRuTime).format(this).toIntOrNull() ?: 0
fun Long.millToYer(): Int = SimpleDateFormat("yyyy", LocaleRuTime).format(this).toIntOrNull() ?: 0
fun Long.millToMonth(): Int = SimpleDateFormat("MM", LocaleRuTime).format(this).toIntOrNull() ?: 0

fun Long.secDataHHMM(): String = SimpleDateFormat("HH:mm", LocaleRuTime).format(this * 1000L)

fun Long.formatTimeElapsed(): String {
    val currentUnixTime = Instant.now().epochSecond
    val elapsedTime = currentUnixTime - this

    return when {
        elapsedTime < 60     -> "только что"
        elapsedTime < 3600 -> {
            val minutes = elapsedTime / 60
            "$minutes минут назад"
        }

        elapsedTime < 86400  -> {
            val hours = elapsedTime / 3600
            "сегодня в ${this.secDataHHMM()}"
        }

        elapsedTime < 172800 -> "вчера в ${this.secDataHHMM()}"
        else                 -> {
            val date = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDate()
            date.toString()
        }
    }
}

