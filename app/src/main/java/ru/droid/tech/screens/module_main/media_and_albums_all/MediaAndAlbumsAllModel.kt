package ru.droid.tech.screens.module_main.media_and_albums_all

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.create_new_album.CreateNewAlbum
import ru.droid.tech.screens.module_main.media.album.AlbumScreen
import ru.droid.tech.screens.module_main.media.all_albums.AlbumAllScreen
import ru.droid.tech.screens.module_main.media_list.MediaListScreen
import ru.data.common.domain.memory.GlobalDada
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.use_case.UseCaseAlbums
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.models.local.DataForPostingMedia
import ru.data.common.models.local.maps.AlbumUI
import ru.data.common.models.local.maps.DroidUI
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.local.screens.ScreenStatusShortContent
import ru.data.common.models.local.screens.MenuMediaRibbon

class MediaAndAlbumsAllModel(
    private val apiMedia: UseCaseMedia,
    private val apiAlbum: UseCaseAlbums,
    private val apiDroid: UseCaseDroid,
    private val context: Context,
) : BaseModel() {

    private val _menuMediaRibbon = MutableStateFlow(MenuMediaRibbon.MY)
    val menuMediaRibbon = _menuMediaRibbon.asStateFlow()

    private val _screenStatus = MutableStateFlow<ScreenStatusShortContent>(ScreenStatusShortContent.Load)
    val screenStatus = _screenStatus.asStateFlow()

    private val _listAllMedia = MutableStateFlow(listOf<MediaUI>())
    private val _listAllAlbums = MutableStateFlow(listOf<AlbumUI>())
    val listAllMedia = _listAllMedia.asStateFlow()
    val listAllAlbums = _listAllAlbums.asStateFlow()

    private val _listMyMedia = MutableStateFlow(listOf<MediaUI>())
    private val _listMyAlbums = MutableStateFlow(listOf<AlbumUI>())
    val listMyMedia = _listMyMedia.asStateFlow()
    val listMyAlbums = _listMyAlbums.asStateFlow()

    private val _listDroidMedia = MutableStateFlow(listOf<MediaUI>())
    private val _listDroidAlbums = MutableStateFlow(listOf<AlbumUI>())
    val listDroidMedia = _listDroidMedia.asStateFlow()
    val listDroidAlbums = _listDroidAlbums.asStateFlow()

    private val _listFavoritesMedia = MutableStateFlow(listOf<MediaUI>())
    private val _listFavoritesAlbums = MutableStateFlow(listOf<AlbumUI>())
    val listFavoritesMedia = _listFavoritesMedia.asStateFlow()
    val listFavoritesAlbums = _listFavoritesAlbums.asStateFlow()

    private val _albumInMetaScreen = MutableStateFlow<AlbumUI?>(null)
    val albumInMetaScreen = _albumInMetaScreen.asStateFlow()

    private var listDroid = listOf<DroidUI>()

    private val _imagesUpload = MutableStateFlow<List<DataForPostingMedia>?>(listOf())
    val imagesUpload = _imagesUpload.asStateFlow()

    init {
        coroutineScope.launch(Dispatchers.IO) {
            getMyDroid()
        }
    }

    private suspend fun getMyDroid() {
        apiDroid.getMyDroid(
            flowStart = {},
            flowSuccess = {
                listDroid = it
            },
            flowStop = {},
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun chooseMenu(menu: MenuMediaRibbon) {
        _menuMediaRibbon.value = menu
        onStartedInitFilterMedia()
    }


    fun onStartedInitFilterMedia() = coroutineScope.launch(Dispatchers.IO) {
        _screenStatus.update { ScreenStatusShortContent.Load }
        when (_menuMediaRibbon.value) {
            MenuMediaRibbon.MY -> getMyMedia()
            MenuMediaRibbon.ALL -> getAllAlbums()
            MenuMediaRibbon.Droid -> getDroidMedia()
            MenuMediaRibbon.FAVORITES -> getFavoritesMedia()
        }
        _screenStatus.update { ScreenStatusShortContent.Ready }
    }

    fun getListImageForUpload() {
        _imagesUpload.value = GlobalDada.value?.listImageForUpload
    }

    fun getAlbum(albumId: Int) = coroutineScope.launch(Dispatchers.IO) {
        _albumInMetaScreen.update { apiAlbum.getAlbumId(albumId) }
    }

    fun uploadPhoto(
        image: List<Uri>,
        album: AlbumUI?
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiMedia.postNewMedia(
            mediaList = image.map { imageMap ->
                DataForPostingMedia(
                    uri = imageMap.toString(),
                    album_id = album?.id,
                    name = null,//todo()
                    is_favorite = null,//todo()
                    address = null,//todo()
                    lat = null,//todo()
                    lon = null,//todo()
                    happened = null,//todo()
                    description = null,//todo()
                )
            },
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                _listAllMedia.value = it
                goBackStack()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message,
        )
    }

    private suspend fun getMyMedia() {
        val media = coroutineScope.launch(Dispatchers.IO) {
            _listMyMedia.update { apiMedia.getMedias(page = 1, isPersonal = true) }
        }
        val albums = coroutineScope.launch(Dispatchers.IO) {
            val defaultAlbum = listOf(
                AlbumUI.getEmpty(
                    owner = apiDroid.getUser(),
                    cover = listMyMedia.value.firstOrNull()?.url ?: ""
                )
            )
            _listMyAlbums.update { defaultAlbum + apiAlbum.getAlbums(isPersonal = true) }
        }
        joinAll(
            media,
            albums,
        )
    }

    private suspend fun getAllAlbums() {
        val media = coroutineScope.launch(Dispatchers.IO) {
            _listAllMedia.update { apiMedia.getMedias(page = 1) }
        }
        val albums = coroutineScope.launch(Dispatchers.IO) {
            _listAllAlbums.update { apiAlbum.getAlbums() }
        }
        joinAll(
            media,
            albums,
        )
    }

    private suspend fun getDroidMedia() {
        val media = coroutineScope.launch(Dispatchers.IO) {
            _listDroidMedia.update {
                if (listDroid.isNotEmpty()) {
                    apiMedia.getMedias(page = 1, DroidIds = listDroid.map { it.id })
                } else {
                    listOf()
                }
            }
        }
        val albums = coroutineScope.launch(Dispatchers.IO) {
            _listDroidAlbums.update {
                if (listDroid.isNotEmpty()) {
                    apiAlbum.getAlbums(DroidIds = listDroid.map { it.id })
                } else {
                    listOf()
                }
            }
        }
        joinAll(
            media,
            albums,
        )
    }

    private suspend fun getFavoritesMedia() {
        val media = coroutineScope.launch(Dispatchers.IO) {
            _listFavoritesMedia.update {
                apiMedia.getMedias(page = 1,
                    isFavorite = true)
            }
        }
        val albums = coroutineScope.launch(Dispatchers.IO) {
            _listFavoritesAlbums.update {
                apiAlbum.getAlbums(
                    isFavorite = true)
            }
        }
        joinAll(
            media,
            albums,
        )
    }

    fun goToCreateNewAlbum() {
        navigator.push(CreateNewAlbum())
    }

    fun goToAlbum(album: AlbumUI) {
        navigator.push(AlbumScreen(album.id))
    }

    fun goToAllAlbum() {
        navigator.push(AlbumAllScreen())
    }

    fun goToViewScreen(media: MediaUI) {
        navigator.push(MediaListScreen(mediaId = media.id))
    }
}





