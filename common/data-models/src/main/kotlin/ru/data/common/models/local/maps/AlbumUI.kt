package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelAlbum
import ru.data.common.models.res.TextApp
import ru.data.common.models.util.millDateDDMMYYYY

data class AlbumUI(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val cover: String? = null,
    val customDate: Long? = null,
    val customDateHuman: String = customDate?.millDateDDMMYYYY() ?: "",
    val isPrivate: Boolean? = null,
    val id: Int,
    val created: Long? = null,
    val createdHuman: String = created?.millDateDDMMYYYY() ?: "",
    val owner: UserUI = UserUI(),
    val isFavorite: Boolean? = null,
) {

    companion object {
        fun mapFrom(
            album: NetworkModelAlbum,
            locationDao: ((Int) -> City?)?,
        ) = AlbumUI(
            name = album.name,
            description = album.description,
            location = album.location,
            cover = album.cover,
            customDate = album.custom_date,
            isPrivate = album.is_private,
            id = album.id,
            created = album.created?.times(1000),
            owner = UserUI.mapFrom(
                user = album.owner,
                locationDao = locationDao,
            ),
            isFavorite = album.is_favorite

        )

        fun getEmpty(
            owner: UserUI,
            cover: String
        ) = AlbumUI(
            name = TextApp.textMyAlbom,
            description = TextApp.textMyAlbom,
            id = -1,
            cover = cover,
            owner = owner,
        )
    }
}