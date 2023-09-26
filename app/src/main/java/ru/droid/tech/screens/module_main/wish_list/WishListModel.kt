package ru.droid.tech.screens.module_main.wish_list

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_main.wish_detail.DetailWish
import ru.droid.tech.screens.module_main.wish_new.NewWish
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.network.NetworkModelWish

class WishListModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {


    private val _listWish = MutableStateFlow(listOf<WishUI>())
    val listWish = _listWish.asStateFlow()


    fun getWishList(idWishList: Int?) = coroutineScope.launch(Dispatchers.IO) {
        _listWish.value = apiWishesAndList.getWishes(wishlistId = idWishList)
    }

    fun goToNewWish() {
        getNavigationLevel(NavLevel.MAIN)?.push(NewWish(0))
    }

    fun goToBackStackAndClearLists() {
        _listWish.update { listOf() }
        goBackStack()
    }


    fun goToDetailWish(wish: WishUI) {
        navigator.push(DetailWish(wish.id))
    }

}