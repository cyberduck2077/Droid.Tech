package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.models.local.DataForPostingMedia
import ru.data.common.models.local.maps.MediaUI
import ru.data.common.models.logger.LogCustom.logD
import ru.data.common.models.network.NetworkModelMedia
import ru.data.common.models.network.UpdatingMedia
import ru.data.common.network.api.ApiMedia
import java.io.File

class UseCaseMedia(
    private val apiMedia: ApiMedia,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getMedias(
        name: String? = null,
        extension: String? = null,
        albumIds: List<Int>? = null,
        isFavorite: Boolean? = null,
        withoutAlbum: Boolean? = null,
        DroidIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        page: Int? = null,
    ): List<MediaUI> = apiMedia.getMedias(
        name = name,
        extension = extension,
        isFavorite = isFavorite,
        page = page,
        albumIds = albumIds,
        withoutAlbum = withoutAlbum,
        DroidIds = DroidIds,
        isPersonal = isPersonal,
        isPrivate = isPrivate
    ).data?.map {
        MediaUI.mapFrom(
            media = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()


    suspend fun getMediasPagination(
        name: String? = null,
        extension: String? = null,
        albumIds: List<Int>? = null,
        isFavorite: Boolean? = null,
        withoutAlbum: Boolean? = null,
        DroidIds: List<Int>? = null,
        isPersonal: Boolean? = null,
        isPrivate: Boolean? = null,
        page: Int? = null,
        listStatus: (
            hasNext: Boolean?,
            hasPrev: Boolean?,
            codeResponse: Int,
        ) -> Unit
    ): List<MediaUI> {
        val response = apiMedia.getMedias(
            name = name,
            extension = extension,
            isFavorite = isFavorite,
            page = page,
            albumIds = albumIds,
            withoutAlbum = withoutAlbum,
            DroidIds = DroidIds,
            isPersonal = isPersonal,
            isPrivate = isPrivate
        )

        listStatus.invoke(
            response.meta?.paginator?.has_next,
            response.meta?.paginator?.has_prev,
            response.getCodeResponse()
        )

        return response.data?.map { data ->
            MediaUI.mapFrom(
                media = data,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
        } ?: listOf()
    }

    suspend fun getMediaById(
        id: Int
    ): MediaUI? = apiMedia.getMediaById(
        media_id = id
    ).data?.let {
        MediaUI.mapFrom(
            media = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    }

    suspend fun postNewMedia(
        mediaList: List<DataForPostingMedia>,
        flowStart: () -> Unit = {},
        compressedFile: (String) -> File,
        flowSuccess: (List<MediaUI>) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        logD("mediaList = $mediaList")

        val listMediaUris = mutableListOf<NetworkModelMedia>()
        mediaList.forEach { media ->
            val compressedImageFile = compressedFile.invoke(media.uri)
            val data = apiMedia.postMyMedia(file = compressedImageFile)

            if (data.status == HttpStatusCode.Unauthorized) {
                flowStop.invoke()
                flowMessage.invoke(data.getDescriptionRudApi())
                flowUnauthorized.invoke()
                return
            }
            if (!data.status.isSuccess()) {
                flowStop.invoke()
                flowMessage.invoke(data.getDescriptionRudApi())
                return
            }
            val idMedia = data.data?.id ?: run {
                flowMessage.invoke(data.getDescriptionRudApi())
            }
            if (idMedia is Int) {
                logD("idMedia = $idMedia")
                val dataPut = apiMedia.putMyMedia(
                    updatingMedia = UpdatingMedia(
                        name = media.name,
                        is_favorite = media.is_favorite,
                        album_id = media.album_id,
                        address = media.address,
                        lat = media.lat,
                        lon = media.lon,
                        description = media.description,
                        happened = media.happened,
                    ), mediaId = idMedia
                )
                dataPut.data?.let { newData ->
                    listMediaUris.add(newData)
                } ?: run {
                    flowMessage.invoke(data.getDescriptionRudApi())
                }
            }
        }
        flowStop.invoke()
        flowSuccess.invoke(listMediaUris.map {
            MediaUI.mapFrom(
                media = it,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
        })

    }

    suspend fun editMedia(
        idMedia: Int,
        name: String? = null,
        isFavorite: Boolean? = null,
        albumId: Int? = null,
        address: String? = null,
        lat: Long? = null,
        lon: Long? = null,
        description: String? = null,
        happened: Long? = null,
        flowStart: () -> Unit = {},
        flowSuccess: (NetworkModelMedia) -> Unit,
        flowUnauthorized: () -> Unit,
        flowStop: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val data = apiMedia.putMyMedia(
            updatingMedia = UpdatingMedia(
                name = name,
                is_favorite = isFavorite,
                album_id = albumId,
                address = address,
                lat = lat,
                lon = lon,
                description = description,
                happened = happened,
            ), mediaId = idMedia
        )

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }
        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        data.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowMessage.invoke(data.getDescriptionRudApi())
        }
        flowStop.invoke()
    }


    suspend fun deleteMedia(
        mediaId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiMedia.deleteMedia(mediaId)

        if (data.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }
        if (!data.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(data.getDescriptionRudApi())
            return
        }
        flowSuccess.invoke()
        flowStop.invoke()
    }
}