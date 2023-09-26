package ru.data.common.models.local.screens

import ru.data.common.models.res.TextApp

enum class MyDroidStatusScr {
    Droid,
    APPLICATIONS,
    INVITATIONS,
    SEARCH;

    fun getTextStage() = when (this) {
        Droid       -> TextApp.titleDroid
        APPLICATIONS -> TextApp.titleApplications
        INVITATIONS -> TextApp.titleInvitations
        SEARCH       -> TextApp.titleSearch
    }
}