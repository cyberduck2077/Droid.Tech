package ru.droid.tech.screens.module_main.wish_update

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.maps.WishlistUI
import ru.data.common.models.res.TextApp

class UpdateWishModel(
    private val apiWishesAndList: UseCaseWishesAndList,
    private val context: Context
) : BaseModel() {

    private val _wish = MutableStateFlow<WishUI?>(null)
    val wish = _wish.asStateFlow()

    private val _listWishlists = MutableStateFlow(listOf<WishlistUI>())
    val listWishlists = _listWishlists.asStateFlow()

    init {
        getWishesAndList()
    }

    fun getWish(id: Int) = coroutineScope.launch {
        apiWishesAndList.getWish(
            wishId = id,
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
    }

    private fun getWishesAndList() = coroutineScope.launch {
        _listWishlists.value = apiWishesAndList.getWishlist()
    }

    fun sendImage(image: String) = coroutineScope.launch {
        val wishOld = _wish.value ?: run {
            message(TextApp.textSomethingWentWrong)
            return@launch
        }
        apiWishesAndList.postWishCover(
            wishId = wishOld.id,
            image = image,
            flowStart = ::gDLoaderStart,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            flowSuccess = {
                gDSetLoader(false)
                _wish.value = it
                message(TextApp.textWishUpdate)
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }

    fun sendNewWish(
        name: String,
        description: String,
        price: String,
        link: String,
        wishlist: WishlistUI,
    ) = coroutineScope.launch {
        val wishOld = _wish.value ?: run {
            message(TextApp.textSomethingWentWrong)
            return@launch
        }

        apiWishesAndList.putMyWish(
            wishId = wishOld.id,
            updatingWish = wishOld.getWishUICreating(
                titleNew = name,
                descriptionNew = description,
                priceNew = price.toLongOrNull()?.times(TextApp.divForeRub),
                linkNew = link,
                wishlistNew = wishlist
            ),
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                _wish.value = it
                message(TextApp.textWishUpdate)
            },
            flowStop = ::gDLoaderStop,
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowMessage = ::message
        )
    }
}