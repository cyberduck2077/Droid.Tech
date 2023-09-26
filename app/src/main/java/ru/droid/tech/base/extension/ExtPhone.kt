package ru.droid.tech.base.extension

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import ru.data.common.models.util.onlyDigit


fun TextFieldValue.getFormattedNumberRuNoSeven(): TextFieldValue {
    val answer = text.onlyDigit()
    val lengthChange = text.length - answer.length
    var rangeStart = selection.start
    var rangeEnd = selection.end
    var outF = ""

    return try {
        if (answer.isNotEmpty() && answer.length < 12) {
            outF += "+${answer[0]}"
            if (answer.length > 1) outF += " (" + answer.substring(1, 4.coerceAtMost(answer.length))
            if (answer.length >= 5) outF += ") " + answer.substring(4,
                7.coerceAtMost(answer.length))
            if (answer.length >= 8) outF += "-" + answer.substring(7, 9.coerceAtMost(answer.length))
            if (answer.length >= 10) outF += "-" + answer.substring(9, answer.length)
        } else if (answer.isNotEmpty()) {
            outF += "+$answer"
        }
        when {
            lengthChange == 0 || (lengthChange == 1 && rangeStart == 12) -> {
                rangeStart += outF.length
                rangeEnd += outF.length
            }
            lengthChange == 1 && rangeStart == 3 -> {
                rangeStart += outF.length
                rangeEnd += outF.length
            }
            selection.end == text.length -> {
                rangeStart += lengthChange
                rangeEnd += lengthChange
            }
        }
        TextFieldValue(text = outF, selection = TextRange(rangeStart, rangeEnd), this.composition)
    } catch (e: Exception) {
        this
    }
}

fun String.formattedNumberPhoneRuNoSeven(): String {
    var phone =  this
    var outF = ""
    return try {
        if (phone.length < 12) {
            outF += "+${this[0]}"
            if (phone.length > 1) outF += " (" + phone.substring(1, 4.coerceAtMost(phone.length))
            if (phone.length >= 5) outF += ") " + phone.substring(4, 7.coerceAtMost(phone.length))
            if (phone.length >= 8) outF += "-" + phone.substring(7, 9.coerceAtMost(phone.length))
            if (phone.length >= 10) outF += "-" + phone.substring(9, phone.length)
        } else if (phone.isNotEmpty()) {
            outF += "+$phone"
        }
        outF
    } catch (e: Exception) {
        this
    }
}
