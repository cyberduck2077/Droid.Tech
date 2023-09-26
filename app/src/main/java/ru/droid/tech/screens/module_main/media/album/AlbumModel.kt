package ru.droid.tech.screens.module_main.media.album

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.media.meta.MetaScreen
import ru.droid.tech.screens.module_main.media_list.MediaListScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetListImage
import ru.data.common.domain.use_case.UseCaseAlbums
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.models.local.DataForPostingMedia
import ru.data.common.models.local.maps.AlbumUI
import ru.data.common.models.local.maps.MediaUI

class AlbumModel(
    private val apiMedia: UseCaseMedia,
    private val apiAlbum: UseCaseAlbums,
    private val context: Context,
) : BaseModel() {

    private val _listMedia = MutableStateFlow(listOf<MediaUI>())
    val listMedia = _listMedia.asStateFlow()

    private val _albumInfo = MutableStateFlow(AlbumUI(id = 0))
    val albumInfo = _albumInfo.asStateFlow()


    fun getMediaByAlbum(albumId: Int) = coroutineScope.launch {
        if (albumId == 0) {
            _listMedia.update { apiMedia.getMedias(page = 1, isPersonal = true) }
            return@launch
        }
        apiAlbum.getAlbumId(albumId)?.let { data ->
            _albumInfo.update { data }
        }
        _listMedia.update { apiMedia.getMedias(albumIds = listOf(albumId)) }
    }

    fun uploadPhoto(
        image: List<Uri>,
    ) = coroutineScope.launch {
        val listMedia = mutableListOf<DataForPostingMedia>()
        image.forEach { image ->
            listMedia.add(
                DataForPostingMedia(
                    uri = image.toString(),
                    album_id = albumInfo.value.id,
                    name = null,//todo()
                    is_favorite = null,//todo()
                    address = null,//todo()
                    lat = null,//todo()
                    lon = null,//todo()
                    happened = null,//todo()
                    description = null,//todo()
                )
            )
        }
        apiMedia.postNewMedia(
            mediaList = listMedia,
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                _listMedia.value = it
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
        )
    }


    fun renameAlbum(name: String?) = coroutineScope.launch {
        albumInfo.value.let { album ->
            apiAlbum.putAlbums(
                albumId = album.id,
                name = name,
                description = album.description,
                location = album.location,
                customDate = album.customDate,
                isPrivate = album.isPrivate ?: false,
                flowStart = {},
                flowSuccess = {},
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowStop = {},
                flowMessage = {})
        }
    }

    fun goToViewScreen(media: MediaUI, album: AlbumUI) {
        navigator.push(MediaListScreen(mediaId = media.id, albumId = album.id))
    }


    fun deleteAlbum() = coroutineScope.launch {
        if (albumInfo.value.id == 0) return@launch
        albumInfo.value.id.let { apiAlbum.deleteAlbum(it) }
        navigator.pop()
    }


    fun downloadAlbum() = coroutineScope.launch {
        if (albumInfo.value.id == 0) return@launch
        apiAlbum.downloadAlbum(
            idAlbum = albumInfo.value.id,
            flowStart = ::gDLoaderStart,
            flowSuccess = {},
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun setAlbumIsFavorite() = coroutineScope.launch {
        if (albumInfo.value.id == 0) return@launch
        apiAlbum.putAlbumInFavorite(
            albumId = albumInfo.value.id,
            favorite = true,
            flowStart = {},
            flowSuccess = {},
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = { message(it) }
        )
    }

    fun changeAlbum(description: String?) = coroutineScope.launch {
        albumInfo.value.let { album ->
            apiAlbum.putAlbums(
                albumId = album.id,
                name = album.name,
                description = description,
                location = album.location,
                customDate = album.customDate,
                isPrivate = false,
                flowStart = {},
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowSuccess = {},
                flowStop = {},
                flowMessage = {})
        }
    }

    fun setImagesForUpload(images: List<Uri>) {
        val listMedia = mutableListOf<DataForPostingMedia>()
        images.forEach {
            listMedia.add(
                DataForPostingMedia(
                    uri = it.toString(),
                    name = null,
                    description = null,
                    happened = null,
                    is_favorite = null,
                    album_id = null,
                    address = null,
                    lat = null,
                    lon = null
                )
            )
        }
        gDSetListImage(listMedia)
    }

    fun goToMetaScreen(album: AlbumUI) {
        navigator.push(MetaScreen(album.id))
    }

}