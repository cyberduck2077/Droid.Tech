package ru.data.common.models.local.screens

import ru.data.common.models.local.maps.UserUI

sealed class StatusScreenInfoUser {
    data object Load : StatusScreenInfoUser()
    data class UserInMyDroid(val user: UserUI) : StatusScreenInfoUser()
    data class UserSomeone(val user: UserUI) : StatusScreenInfoUser()
}