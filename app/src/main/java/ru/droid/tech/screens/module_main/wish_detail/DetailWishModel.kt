package ru.droid.tech.screens.module_main.wish_detail

import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.wish_update.UpdateWish
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.res.TextApp

class DetailWishModel(
    private val apiWishesAndList: UseCaseWishesAndList
) : BaseModel() {

    private val _wish = MutableStateFlow<WishUI?>(null)
    val wish = _wish.asStateFlow()

    fun getWish(id: Int) = coroutineScope.launch(Dispatchers.IO) {
        apiWishesAndList.getWish(
            wishId = id,
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                _wish.value = it
            },
            flowError = {
                goBackStack()
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun sendNewStatus() = coroutineScope.launch(Dispatchers.IO) {
        _wish.value?.let {
            apiWishesAndList.postWishFulfillment(
                wishId = it.id,
                flowStart = ::gDLoaderStart,
                flowSuccess = {
                    _wish.value = it
                    gDSetLoader(false)
                },
                flowError = {
                    goBackStack()
                },
                flowStop = ::gDLoaderStop,
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowMessage = ::message
            )

        } ?: run {
            message(TextApp.textSomethingWentWrong)
        }
    }

    fun goToReedit() {
        _wish.value?.id?.let {
            navigator.push(UpdateWish(it))
        }
    }

    fun deleteWish() = coroutineScope.launch(Dispatchers.IO) {
        _wish.value?.id?.let {
            apiWishesAndList.deleteWish(
                wishId = it,
                flowStart = ::gDLoaderStart,
                flowSuccess = {
                    gDSetLoader(false)
                    goBackStack()
                },
                flowStop = ::gDLoaderStop,
                flowUnauthorized = {
                    getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
                },
                flowMessage = ::message
            )
        }
    }
}