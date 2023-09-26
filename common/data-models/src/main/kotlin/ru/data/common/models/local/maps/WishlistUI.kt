package ru.data.common.models.local.maps

import ru.data.common.models.data_base.City
import ru.data.common.models.network.NetworkModelWishlist
import ru.data.common.models.util.millDateDDMMYYYY

data class WishlistUI(
    val title: String?,
    val isSecret: Boolean?,
    val id: Int,
    val created: Long,
    val createdHuman: String = created.millDateDDMMYYYY(),
    val cover: String?,
    val wishes: List<WishUI>?,
) {

    companion object{
        fun mapFrom(
            wishlist: NetworkModelWishlist,
            locationDao:((Int)-> City?)?,
        )= WishlistUI(
            title = wishlist.title ,
            isSecret = wishlist.is_secret ,
            id = wishlist.id ,
            created = wishlist.created.times(1000) ,
            cover = wishlist.cover ,
            wishes = wishlist.wishes?.map { wish -> WishUI.mapFrom(
                wish = wish,
                locationDao = locationDao
            ) }
        )
    }
}
