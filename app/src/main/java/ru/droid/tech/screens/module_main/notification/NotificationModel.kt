package ru.droid.tech.screens.module_main.notification

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.models.network.NetworkModelNotification

class NotificationModel(
) : BaseModel() {


    private val _listNotification = MutableStateFlow<List<NetworkModelNotification>>(listOf())
    val listNotification = _listNotification.asStateFlow()

    fun getNotification() = coroutineScope.launch{
        gDSetLoader(true)
        delay(2000)
        gDSetLoader(false)
    }



}
