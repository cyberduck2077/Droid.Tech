package ru.data.common.models.network

import ru.data.common.models.local.maps.AlbumUICreating

data class CreatingAlbum(
    val name: String?,
    val description: String?,
    val location: String?,
    val custom_date: Long?,
    val is_private: Boolean = false,
){
    companion object {
        fun mapFrom(album : AlbumUICreating)=  CreatingAlbum(
            name =album.name ,
            description =album.description ,
            location =album.location ,
            custom_date =album.customDate?.div(1000) ,
            is_private =album.isPrivate
        )
    }
}

data class NetworkModelAlbum(
    val name: String? = null,
    val description: String? = null,
    val location: String? = null,
    val cover: String? = null,
    val custom_date: Long? = null,
    val is_private: Boolean? = null,
    val id: Int,
    val created: Long? = null,
    val owner: NetworkModelUser = NetworkModelUser(),
    val is_favorite: Boolean? = null,
)