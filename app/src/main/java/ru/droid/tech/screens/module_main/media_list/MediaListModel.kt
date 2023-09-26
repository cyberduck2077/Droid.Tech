package ru.droid.tech.screens.module_main.media_list

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.media.album.AlbumScreen
import ru.droid.tech.screens.module_main.media_edit.EditMedia
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.models.local.maps.AlbumUI
import ru.data.common.models.local.maps.MediaUI

class MediaListModel(
    private val apiMedia: UseCaseMedia,
) : BaseModel() {

    private val _listMedia = MutableStateFlow(listOf<MediaUI>())
    val listMedia = _listMedia.asStateFlow()

    private val _albumInfo = MutableStateFlow(AlbumUI(id = 0))
    val albumInfo = _albumInfo.asStateFlow()

    fun getMedia(idAlbum: Int) = coroutineScope.launch(Dispatchers.IO) {
        if (idAlbum != 0) {
            _listMedia.value = apiMedia.getMedias(albumIds = listOf(idAlbum))
        } else {
            _listMedia.value = apiMedia.getMedias(isPersonal = true)
        }
    }


    fun goToEditScreen(id: Int) {
        navigator.push(EditMedia(id))
    }

    fun addToFavoriteMedia(media: MediaUI) = coroutineScope.launch(Dispatchers.IO)  {
        apiMedia.editMedia(
            idMedia = media.id,
            isFavorite = true,
            flowStart = {},
            flowSuccess = {},
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = {})
    }


    fun getAlbumAndGo() {
        navigator.push(AlbumScreen(albumInfo.value.id))
    }

    fun deleteMedia(id: Int) = coroutineScope.launch(Dispatchers.IO)  {
        apiMedia.deleteMedia(
            mediaId = id,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                navigator.pop()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }
}