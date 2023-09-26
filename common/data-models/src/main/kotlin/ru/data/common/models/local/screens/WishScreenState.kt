package ru.data.common.models.local.screens

import ru.data.common.models.local.maps.WishlistUI

sealed class WishScreenState {
    data object NewWish : WishScreenState()
    data class NewWishListEmpty(val wishlist: WishlistUI) : WishScreenState()
    data object LoadWish : WishScreenState()
}