package ru.data.common.models.network

import ru.data.common.models.local.maps.WishUICreating

data class NetworkModelWish(
    val title: String?,
    val description: String?,
    val price: Long?,
    val link: String?,
    val id: Int,
    val cover: String?,
    val created: Long,
    val is_fulfilled: Int?,
    val user: NetworkModelUser,
    val wishlist: NetworkModelWishShort?,
)

data class NetworkModelWishlist(
    val title: String?,
    val is_secret: Boolean?,
    val id: Int,
    val created: Long,
    val cover: String?,
    val wishes: List<NetworkModelWish>?,
)

data class CreatingWishlist(
    val title: String? = null,
    val is_secret: Boolean? = null,
    val author_ids: List<Int>? = null
)

data class CreatingWish(
    val title: String? = null,
    val description: String? = null,
    val price: Long? = null,
    val link: String? = null,
    val wishlist_id: Int? = null,
) {
    companion object {
        fun mapFrom(wish: WishUICreating) = CreatingWish(
            title = wish.title,
            description = wish.description,
            price = wish.price,
            link = wish.link,
            wishlist_id = wish.wishlistId
        )
    }
}

data class NetworkModelWishShort(
    val id: Int,
    val title: String,
)

