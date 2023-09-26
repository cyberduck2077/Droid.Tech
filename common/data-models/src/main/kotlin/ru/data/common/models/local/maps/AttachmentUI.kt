package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelAttachment
import ru.data.common.models.util.millDateDDMMYYYY

data class AttachmentUI(
    val name: String,
    val isFavorite: Boolean,
    val lat: Int?,
    val lon: Int?,
    val id: Int,
    val extension: String?,
    val url: String,
    val firstFrame: String?,
    val duration: String?,
    val created: Long,
    val createdHuman: String = created.millDateDDMMYYYY(),
    val happened: Int?,
    val address: String?,
    val size: Int,
    val owner: UserUI?,
    val isVideo: Boolean = false,
) {
    companion object {
        fun mapFrom(
            attach: NetworkModelAttachment,
            locationDao:((Int)-> City?)?,
        ) = AttachmentUI(
            name = attach.name,
            isFavorite = attach.is_favorite,
            lat = attach.lat,
            lon = attach.lon,
            id = attach.id,
            extension = attach.extension,
            url = attach.url,
            firstFrame = attach.first_frame,
            duration = attach.duration,
            created = attach.created.times(1000),
            happened = attach.happened,
            address = attach.address,
            size = attach.size,
            owner = attach.owner?.let {
                UserUI.mapFrom(
                    user = it,
                    locationDao = locationDao
                )
            },
            isVideo = attach.is_video
        )
    }
}