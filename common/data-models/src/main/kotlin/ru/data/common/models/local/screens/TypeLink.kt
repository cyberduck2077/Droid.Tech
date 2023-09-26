package ru.data.common.models.local.screens

import ru.data.common.models.res.TextApp

enum class TypeLink {
    IN_MY_RIBBON,
    IN_MESSAGE;
    fun getText() =when(this){
        IN_MY_RIBBON -> TextApp.textInHourFeed
        IN_MESSAGE   -> TextApp.textInTheMessage
    }
}
