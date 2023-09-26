package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelWish
import ru.data.common.models.util.millDateDDMMYYYY

data class WishUI(
    val title: String?,
    val description: String?,
    val price: Long?,
    val link: String?,
    val id: Int,
    val cover: String?,
    val created: Long,
    val createdHuman: String = created.millDateDDMMYYYY() ?: "",
    val isFulfilled: StatusFulfilled?,
    val user: UserUI,
    val wishlist: WishUIShort?,
) {

    companion object {
        fun mapFrom(
            wish: NetworkModelWish,
            locationDao: ((Int) -> City?)?,
        ) = WishUI(
            title = wish.title,
            description = wish.description,
            price = wish.price,
            link = wish.link,
            id = wish.id,
            cover = wish.cover,
            created = wish.created.times(1000),
            isFulfilled = wish.is_fulfilled?.let { StatusFulfilled.getEnum(it) },
            user = UserUI.mapFrom(
                user = wish.user,
                locationDao = locationDao
            ),
            wishlist = wish.wishlist?.let { WishUIShort.mapFrom(it) }


        )
    }

    fun getWishUICreating(
        titleNew: String?,
        descriptionNew: String?,
        priceNew: Long?,
        linkNew: String?,
        wishlistNew: WishlistUI?,
    ) = WishUICreating(
        title = if (titleNew != this.title) titleNew else null,
        description = if (descriptionNew != this.description) descriptionNew else null,
        price = if (priceNew != this.price) priceNew else null,
        link = if (linkNew != this.link) linkNew else null,
        wishlistId = if (wishlistNew?.id != this.wishlist?.id) wishlistNew?.id else null,
    )
}






