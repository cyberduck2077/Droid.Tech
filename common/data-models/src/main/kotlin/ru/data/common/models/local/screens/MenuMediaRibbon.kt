package ru.data.common.models.local.screens

import ru.data.common.models.res.TextApp

enum class MenuMediaRibbon {
    MY,
    ALL,
    Droid,
    FAVORITES;

    fun getTextMenu() = when (this) {
        MY  -> TextApp.titleMyMedia
        ALL -> TextApp.titleAll
        Droid   -> TextApp.titleDroid
        FAVORITES -> TextApp.titleFavorites
    }
}
