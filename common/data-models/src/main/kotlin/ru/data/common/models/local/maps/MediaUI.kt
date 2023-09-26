package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelMedia
import ru.data.common.models.util.millDateDDMMYYYY

data class MediaUI(
    val name: String?,
    val isFavorite: Boolean?,
    val id: Int,
    val extension: String?,
    val url: String?,
    val firstFrame: String?,
    val duration: String?,
    val created: Long?,
    val createdHuman: String = created?.millDateDDMMYYYY() ?: "",
    val size: Int?,
    val owner: UserUI?,
    val isVideo: Boolean = false,
    val albumId: Int?,
    val description: String?,
    val happened: Long?,
    val happenedHuman: String = happened?.millDateDDMMYYYY() ?: "",
    val address: String?
) {
    companion object {
        fun mapFrom(
            media: NetworkModelMedia,
            locationDao: ((Int) -> City?)?
        ) = MediaUI(
            name = media.name,
            isFavorite = media.is_favorite,
            id = media.id,
            extension = media.extension,
            url = media.url,
            firstFrame = media.first_frame,
            duration = media.duration,
            created = media.created?.times(1000),
            size = media.size,
            owner = media.owner?.let { user ->
                UserUI.mapFrom(
                    user = user,
                    locationDao = locationDao
                )
            },
            isVideo = media.is_video,
            albumId = media.album_id,
            description = media.description,
            happened = media.happened?.times(1000),
            address = media.address
        )
    }

    fun getDescForPhoto(): String {
        val str: List<String?> = listOf(this.name, this.description)
        return str.filterNotNull()
            .joinToString(separator = " · ", postfix = if (this.address != null) " · " else "")
    }
}