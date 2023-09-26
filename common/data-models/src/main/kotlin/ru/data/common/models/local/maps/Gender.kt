package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp


enum class Gender() {
    MAN,
    WOMAN;

    companion object {
        fun getEnum(status: Int) = entries.getOrElse(status) { null }
    }

    fun getGenderYourSatellite() = when (this) {
        WOMAN -> MAN
        MAN   -> WOMAN
    }

    fun getGenderText() = when (this) {
        WOMAN -> TextApp.textGenderWoman
        MAN   -> TextApp.textGenderMan
    }

    fun getGenderTextShort() = when (this) {
        WOMAN -> TextApp.textGenderWomanShort
        MAN   -> TextApp.textGenderManShort
    }

    fun getEnterDataYourSatellite() = when (this) {
        WOMAN -> TextApp.textGenderInterMan
        MAN   -> TextApp.textGenderInterWoman
    }
}