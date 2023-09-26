package ru.droid.tech.screens.module_main.wish_list_new

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.wish_new.NewWish
import ru.data.common.domain.use_case.UseCaseWishesAndList

class NewWishListModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {


    fun createNewWishList(
        title: String,
        isPrivate: Boolean
    ) = coroutineScope.launch(Dispatchers.IO) {
        apiWishesAndList.postWishlist(
            title = title,
            isSecret = isPrivate,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                getNavigationLevel(NavLevel.MAIN)?.push(NewWish(it.id))
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }
}