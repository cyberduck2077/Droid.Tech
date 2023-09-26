package ru.droid.tech.screens.module_main.main_gifts

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.wish_list.WishList
import ru.droid.tech.screens.module_main.wish_list_new.NewWishList
import ru.droid.tech.screens.module_main.wish_new.NewWish
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.maps.WishlistUI

class GiftsModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {


    private val _listWishlists = MutableStateFlow(listOf<WishlistUI>())
    val listWishlists = _listWishlists.asStateFlow()

    init {
        getWishesAndList()
    }


    private fun getWishesAndList() = coroutineScope.launch(Dispatchers.IO) {
        gDSetLoader(true)
        _listWishlists.value = apiWishesAndList.getWishlist()
        gDSetLoader(false)
    }

    fun goInWishlist(wish: WishlistUI?) {
        if (wish == null) {
            goToWishList(wish)
            return
        }
        if (wish.wishes.isNullOrEmpty()) {
            goToNewWish(wish.id)
            return
        }
        goToWishList(wish)
    }

    fun deleteWishlist(wish: WishlistUI) = coroutineScope.launch(Dispatchers.IO) {
        apiWishesAndList.deleteWishList(
            wishListId = wish.id,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                getWishesAndList()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun goToNewWish(wishListId: Int = 0) {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWish(wishListId))
    }

    fun goToNewWishList() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWishList())
    }

    fun goToWishList(wishList: WishlistUI?) {
        navigator.push(
            WishList(
                wishListIdRote = wishList?.id,
                wishListTitleRote = wishList?.title
            )
        )
    }
}