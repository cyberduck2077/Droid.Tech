package ru.data.common.models.local.maps

import ru.data.common.models.network.NetworkModelWishShort


data class WishUIShort(
    val id: Int,
    val title: String,
) {
    companion object {
        fun mapFrom(wish: NetworkModelWishShort) = WishUIShort(
            id = wish.id,
            title = wish.title
        )
    }
}