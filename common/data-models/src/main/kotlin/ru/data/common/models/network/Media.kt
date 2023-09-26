package ru.data.common.models.network

data class NetworkModelMedia(
    val name: String?,
    val is_favorite: Boolean?,
    val id: Int,
    val extension: String?,
    val url: String?,
    val first_frame: String?,
    val duration: String?,
    val created: Long?,
    val size: Int?,
    val owner: NetworkModelUser?,
    val is_video: Boolean = false,
    val album_id: Int?,
    val description: String?,
    val happened: Long?,
    val address: String?
)


data class UpdatingMedia(
    val name: String?,
    val description: String?,
    val happened: Long?,
    val is_favorite: Boolean?,
    val album_id: Int?,
    val address: String?,
    val lat: Long?,
    val lon: Long?
)

data class UpdatingMediaWithId(
    val id: Int,
    val updatingMedia: UpdatingMedia
)

data class IsFavoriteBody(
    val is_favorite: Boolean?
)