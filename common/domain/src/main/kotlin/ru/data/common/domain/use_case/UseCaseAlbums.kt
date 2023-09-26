package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.models.local.maps.AlbumUI
import ru.data.common.models.local.maps.AlbumUICreating
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.logger.LogCustom.logE
import ru.data.common.models.network.CreatingAlbum
import ru.data.common.models.network.IsFavoriteBody
import ru.data.common.models.network.BaseApi
import ru.data.common.models.res.TextApp
import ru.data.common.network.api.ApiAlbums

class UseCaseAlbums(
    private val apiAlbums: ApiAlbums,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getAlbums(
        name: String? = null,
        page: Int? = null,
        DroidIds: List<Int>? = listOf(),
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        isFavorite: Boolean? = null,
    ): List<AlbumUI> = apiAlbums.getAlbums(
        name = name,
        page = page,
        DroidIds = DroidIds,
        isPersonal = isPersonal,
        isPrivate = isPrivate,
        isFavorite = isFavorite
    ).data?.map {
        AlbumUI.mapFrom(
            album = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()

    suspend fun postAlbums(
        bodyAlbum: AlbumUICreating,
        flowStart: () -> Unit = {},
        flowError: () -> Unit = {},
        flowSuccess: (AlbumUI) -> Unit,
        flowUnauthorized: () -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiAlbums.postAlbums(CreatingAlbum.mapFrom(bodyAlbum))

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postAlbums", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(AlbumUI.mapFrom(
                album = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postAlbums", response)
        }
        flowStop.invoke()
    }

    suspend fun getAlbumId(
        albumId: Int
    ): AlbumUI? = apiAlbums.getAlbumId(
        albumId = albumId
    ).data?.let {
        AlbumUI.mapFrom(
            album = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    }

    suspend fun putAlbumInFavorite(
        albumId: Int,
        favorite: Boolean? = null,
        flowStart: () -> Unit = {},
        flowSuccess: (MediaUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiAlbums.putAlbumInFavorite(
            albumId = albumId,
            favorite = IsFavoriteBody(is_favorite = favorite)
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbumInFavorite", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(MediaUI.mapFrom(
                media = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbumInFavorite", response)
        }
        flowStop.invoke()
    }

    suspend fun putAlbums(
        albumId: Int,
        name: String?,
        description: String?,
        location: String?,
        customDate: Long?,
        isPrivate: Boolean = false, //default: false
        flowStart: () -> Unit = {},
        flowSuccess: (MediaUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiAlbums.putAlbums(
            albumId = albumId,
            updatingAlbum = CreatingAlbum(
                name = name,
                description = description,
                location = location,
                custom_date = customDate,
                is_private = isPrivate
            )
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbums", response)
            return
        }
        response.data?.let { newData ->
            flowSuccess.invoke(MediaUI.mapFrom(
                media = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putAlbums", response)
        }
        flowStop.invoke()
    }


    suspend fun deleteAlbum(
        albumId: Int
    ): BaseApi<Any> {
        val response = apiAlbums.deleteAlbum(albumId)

        if (!response.status.isSuccess()) {
            logE("postAlbums", response.message)
        }
        return response
    }

    suspend fun downloadAlbum(
        idAlbum: Int,
        flowStart: () -> Unit = {},
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
        flowSuccess: () -> Unit,
    ) {
        flowStart.invoke()
        apiAlbums.downloadAlbum(albumId = idAlbum)
        flowSuccess.invoke()
        flowMessage.invoke(TextApp.textDone)
        flowStop.invoke()
    }
}