package ru.droid.tech.screens.module_main.media_edit

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.data.common.domain.memory.gDMessage
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.models.local.maps.MediaUI


class EditMediaModel(
    private val apiMedia: UseCaseMedia,
) : BaseModel() {

    private val _media = MutableStateFlow<MediaUI?>(null)
    val media = _media.asStateFlow()


    fun getMediaById(id: Int) = coroutineScope.launch {
        _media.value = apiMedia.getMediaById(id = id)
    }

    fun editMedia(
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
    ) = coroutineScope.launch {
        _media.value?.id?.let { idMedia ->//todo() переделать
            apiMedia.editMedia(
                idMedia = idMedia,
                address = addressEnter,
                description = descriptionEnter,
                happened = customDateEnter,
                flowStart = {},
                flowSuccess = {
                    goBackStack()
                },
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowStop = {},
                flowMessage = ::gDMessage,
            )
        }
    }
}