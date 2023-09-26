package ru.droid.tech.screens.module_main.wish_new

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.coroutineScope
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.droid.tech.R
import ru.droid.tech.base.BaseModel
import ru.droid.tech.screens.module_authorization.AuthScreen
import ru.droid.tech.screens.module_main.core_main.HomeMainScreen
import ru.data.common.domain.memory.gDLoaderStart
import ru.data.common.domain.memory.gDLoaderStop
import ru.data.common.domain.memory.gDSetLoader
import ru.data.common.domain.memory.gDSetScreenMain
import ru.data.common.domain.use_case.UseCaseWishesAndList
import ru.data.common.models.local.maps.Gender
import ru.data.common.models.local.maps.UserUI
import ru.data.common.models.local.maps.WishUICreating
import ru.data.common.models.local.maps.WishlistUI
import ru.data.common.models.local.screens.ScreensHome
import ru.data.common.models.local.screens.WishScreenState
import ru.data.common.models.network.NetworkModelWishlist
import ru.data.common.models.res.TextApp
import java.io.File

class NewWishModel(
    private val apiWishesAndList: UseCaseWishesAndList,
    private val context: Context,
) : BaseModel() {

    private val _idWishlist = MutableStateFlow<WishlistUI?>(null)
    val idWishlist = _idWishlist.asStateFlow()


    private val _listWishlists = MutableStateFlow(listOf<WishlistUI>())
    val listWishlists = _listWishlists.asStateFlow()

    private val _stateScreen = MutableStateFlow<WishScreenState>(WishScreenState.LoadWish)
    val stateScreen = _stateScreen.asStateFlow()

    init {
        getWishesAndList()
    }

    private fun getWishesAndList() = coroutineScope.launch(Dispatchers.IO) {
        _listWishlists.value = apiWishesAndList.getWishlist()
    }

    fun addNewWishlist(idWishlist: WishlistUI?) {
        _idWishlist.value = idWishlist
        _stateScreen.update { WishScreenState.NewWish }
    }

    fun initWishlist(idWishlist: Int) = coroutineScope.launch(Dispatchers.IO) {
        if (idWishlist == 0) {
            _stateScreen.update { WishScreenState.NewWish }
            return@launch
        }
        apiWishesAndList.getWishlistFromId(
            wishlistId = idWishlist,
            flowStart = {},
            flowSuccess = { wishlist ->
                if (wishlist.wishes.isNullOrEmpty()) {
                    _stateScreen.update { WishScreenState.NewWishListEmpty(wishlist) }
                }
            },
            flowStop = {},
            flowError = {},
            flowUnauthorized = {},
            flowMessage = {}
        )
    }

    fun sendNewWish(
        imagePatch: String?,
        name: String,
        description: String,
        price: String,
        link: String,
        wishlist: WishlistUI,
    ) = coroutineScope.launch(Dispatchers.IO) {
        val inputStream =
            context.resources.openRawResource(R.raw.image_stub_wish)

        val file = File(context.cacheDir, "image_stub_wish.jpg")

        if (file.exists()) {
            file.delete()
        }

        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        apiWishesAndList.postWish(
            imagePatch = imagePatch,
            fileDefault = file,
            compressedFile = {
                runBlocking {
                    Compressor.compress(context, it.toUri().toFile()) {
                        default(width = 1024)
                    }
                }
            },
            bodyWish = WishUICreating(
                title = name,
                description = description,
                price = price.toLongOrNull()?.times(TextApp.divForeRub),
                link = link,
                wishlistId = wishlist.id
            ),
            flowStart = ::gDLoaderStart,
            flowSuccess = {
                gDSetLoader(false)
                goBackStack()
            },
            flowUnauthorized = {
                getNavigationLevel(NavLevel.MAIN)?.push(AuthScreen())
            },
            flowStop = ::gDLoaderStop,
            flowMessage = ::message
        )
    }

    fun goToBackOnMain() {
        gDSetScreenMain(ScreensHome.GIFTS_SCREEN)
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(HomeMainScreen())
    }
}

