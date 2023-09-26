package ru.data.common.models.local

data class DataForPostingMedia(
    val uri: String,
    val name: String?,
    val description: String?,
    val happened: Long?,
    val is_favorite: Boolean?,
    val album_id: Int?,
    val address: String?,
    val lat: Long?,
    val lon: Long?
)
