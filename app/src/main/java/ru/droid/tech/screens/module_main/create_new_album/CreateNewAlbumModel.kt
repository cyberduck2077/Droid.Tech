package ru.droid.tech.screens.module_main.create_new_album

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.media.album.AlbumScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCaseAlbums
import ru.data.common.models.local.maps.AlbumUICreating
import ru.data.common.models.res.TextApp

class CreateNewAlbumModel(
    private val apiAlbum: UseCaseAlbums,
) : BaseModel() {

    fun createAlbum(
        titleEnter: String,
        descriptionEnter: String,
        customDateEnter: Long?,
        addressEnter: String?,
//        isPrivateEnter: Boolean,
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiAlbum.postAlbums(
            bodyAlbum = AlbumUICreating(
                name = titleEnter,
                description = descriptionEnter,
                location = addressEnter,
                customDate = customDateEnter,
                isPrivate = false
            ),
            flowStart = ::gDLoaderStart,
            flowError = {
                message(TextApp.errorCreateAlbum)
                navigator.pop()
            },
            flowSuccess = {
                message(TextApp.textAlbumCreated)
                navigator.push(AlbumScreen(it.id))
            },

            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }
}