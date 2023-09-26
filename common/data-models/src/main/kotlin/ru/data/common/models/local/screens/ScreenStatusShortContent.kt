package ru.data.common.models.local.screens

sealed class ScreenStatusShortContent {
    data object Load : ScreenStatusShortContent()
    data object Ready : ScreenStatusShortContent()
}