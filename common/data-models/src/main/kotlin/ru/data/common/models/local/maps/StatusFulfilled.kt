package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp

/**
 * new = 0
 *
 * progress = 1
 *
 * fulfilled  = 2
 */
enum class StatusFulfilled() {
    NEW,
    PROGRESS,
    FULFILLED;

    companion object {
        fun getEnum(num: Int?) = entries.getOrElse(num ?: 0) { NEW }
    }

    fun enabledButtonDetailWish() = when (this) {
        NEW -> true
        PROGRESS -> false
        FULFILLED -> true
    }


    fun getTextButtonForeWish() = when (this) {
        NEW -> TextApp.textBook
        PROGRESS -> TextApp.textBooked
        FULFILLED -> TextApp.textGave
    }

    fun getNextStatus() = when (this) {
        NEW -> PROGRESS
        PROGRESS -> FULFILLED
        FULFILLED -> null
    }
}