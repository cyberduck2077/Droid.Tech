package ru.data.common.domain.use_case

import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import ru.data.common.models.local.maps.WishUI
import ru.data.common.models.local.maps.WishUICreating
import ru.data.common.models.local.maps.WishlistUI
import ru.data.common.models.logger.LogCustom.logE
import ru.data.common.models.network.CreatingWish
import ru.data.common.models.network.CreatingWishlist
import ru.data.common.models.res.TextApp
import ru.data.common.network.api.ApiWishesAndList
import java.io.File

class UseCaseWishesAndList(
    private val apiWishesAndList: ApiWishesAndList,
    private val daoLocation: UseCaseLocations,
) {

    suspend fun getWishes(
        wishlistId: Int? = null,
        title: String? = null,
        page: Int? = null,
    ): List<WishUI> = apiWishesAndList.getWishes(
        page = page,
        wishlistId = wishlistId,
        title = title
    ).data?.map {
        WishUI.mapFrom(
            wish = it,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()

    suspend fun postWish(
        imagePatch: String?,
        bodyWish: WishUICreating,
        compressedFile: (String) -> File,
        fileDefault: File,
        flowStart: () -> Unit = {},
        flowSuccess: (WishUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()


        val responseStart = apiWishesAndList.postWishlist(CreatingWish.mapFrom(bodyWish))

        if (responseStart.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(responseStart.getDescriptionRudApi())
            flowUnauthorized.invoke()
            return
        }

        if (!responseStart.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(responseStart.getDescriptionRudApi())
            return
        }
        var message = responseStart.getDescriptionRudApi()
        responseStart.data?.let { newData ->
            imagePatch?.let {
                val compressedImageFile = compressedFile.invoke(imagePatch)
                val responseImage = apiWishesAndList.postWishCover(newData.id, compressedImageFile)
                responseImage.data?.let { newDataAndImage ->
                    flowSuccess.invoke(WishUI.mapFrom(
                        wish = newDataAndImage,
                        locationDao = { runBlocking { daoLocation.getDdCity(it) } }
                    ))
                    message = responseImage.getDescriptionRudApi()
                } ?: run {
                    flowMessage.invoke(responseImage.getDescriptionRudApi())
                }
            } ?: run {
                val responseImage = apiWishesAndList.postWishCover(newData.id, fileDefault)
                responseImage.data?.let { newDataAndImage ->
                    flowSuccess.invoke(WishUI.mapFrom(
                        wish = newDataAndImage,
                        locationDao = { runBlocking { daoLocation.getDdCity(it) } }
                    ))
                    message = responseImage.getDescriptionRudApi()
                } ?: run {
                    flowMessage.invoke(responseImage.getDescriptionRudApi())
                    logE("postWish", responseImage)
                }
            }
        }
        flowMessage.invoke(message)
        flowStop.invoke()
    }

    suspend fun getWishlistFromId(
        wishlistId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (WishlistUI) -> Unit,
        flowStop: () -> Unit = {},
        flowError: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiWishesAndList.getWishlist(wishlistId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowError.invoke()
            return
        }

        response.data?.let { newData ->
            val data = WishlistUI.mapFrom(
                wishlist = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            )
            flowSuccess.invoke(data)
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getWish", response)
        }
        flowStop.invoke()


    }

    suspend fun getWish(
        wishId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (WishUI) -> Unit,
        flowStop: () -> Unit = {},
        flowError: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiWishesAndList.getWish(wishId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            flowError.invoke()
            logE("getWish", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(WishUI.mapFrom(
                wish = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getWish", response)
        }
        flowStop.invoke()
    }

    suspend fun deleteWish(
        wishId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiWishesAndList.deleteWish(wishId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("deleteWish", response)
            return
        }
        flowMessage.invoke(response.description ?: TextApp.textDone)
        flowSuccess.invoke()
        flowStop.invoke()
    }

    suspend fun deleteWishList(
        wishListId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: () -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val response = apiWishesAndList.deleteWishlist(wishListId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("deleteWishList", response)
            return
        }
        flowMessage.invoke(response.description ?: TextApp.textDone)
        flowSuccess.invoke()
        flowStop.invoke()
    }

    suspend fun putMyWish(
        wishId: Int,
        updatingWish: WishUICreating,
        flowStart: () -> Unit = {},
        flowSuccess: (WishUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiWishesAndList.putWish(
            wishId,
            CreatingWish.mapFrom(updatingWish)
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(WishUI.mapFrom(
                wish = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
        }
        flowStop.invoke()
    }

    suspend fun postWishFulfillment(
        wishId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (WishUI) -> Unit,
        flowStop: () -> Unit = {},
        flowError: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiWishesAndList.postWishFulfillment(wishId)

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postWishFulfillment", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(WishUI.mapFrom(
                wish = newData,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postWishFulfillment", response)
        }
        flowStop.invoke()
    }

    suspend fun postWishCover(
        wishId: Int,
        image: String,
        compressedFile: (String) -> File,
        flowStart: () -> Unit = {},
        flowSuccess: (WishUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val compressedImageFile = compressedFile.invoke(image)
        val response = apiWishesAndList.postWishCover(wishId, compressedImageFile)
        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postWishCover", response)
            return
        }

        response.data?.let { newDataAndImage ->
            flowSuccess.invoke(WishUI.mapFrom(
                wish = newDataAndImage,
                locationDao = { runBlocking { daoLocation.getDdCity(it) } }
            ))
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("postWishCover", response)
        }
        flowStop.invoke()
    }

    suspend fun postWishlist(
        title: String? = null,
        isSecret: Boolean? = null,
        authorIds: List<Int>? = null,
        flowStart: () -> Unit = {},
        flowSuccess: (WishlistUI) -> Unit,
        flowStop: () -> Unit = {},
        flowUnauthorized: () -> Unit,
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()

        val response = apiWishesAndList.postWishlist(
            CreatingWishlist(
                title = title,
                is_secret = isSecret,
                author_ids = authorIds

            )
        )

        if (response.status == HttpStatusCode.Unauthorized) {
            flowStop.invoke()
            flowMessage.invoke(TextApp.textWrongLoginOrPassword)
            flowUnauthorized.invoke()
            return
        }

        if (!response.status.isSuccess()) {
            flowStop.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(
                WishlistUI.mapFrom(
                    wishlist = newData,
                    locationDao = { runBlocking { daoLocation.getDdCity(it) } }
                )
            )
        } ?: run {
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("putWish", response)
        }
        flowStop.invoke()
    }

    suspend fun getWishlist(
        title: String? = null,
        page: Int? = null,
    ): List<WishlistUI> = apiWishesAndList.getWishlists(
        page = page,
        title = title
    ).data?.map { data ->
        WishlistUI.mapFrom(
            wishlist = data,
            locationDao = { runBlocking { daoLocation.getDdCity(it) } }
        )
    } ?: listOf()

}