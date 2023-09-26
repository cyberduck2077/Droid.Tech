package ru.data.common.models.local

import kotlinx.serialization.Serializable
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.network.NetworkModelUser

@Serializable
data class LocalFileValue(
    val user: UserUI? = null,
    val chooserDroidId: Int? = null,
    val token:String? = null,
    val tokenFirebase:String? = null,
)


