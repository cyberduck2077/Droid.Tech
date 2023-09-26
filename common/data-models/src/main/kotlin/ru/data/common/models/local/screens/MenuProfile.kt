package ru.data.common.models.local.screens

import ru.data.common.models.res.TextApp

enum class MenuProfile {
    MEDIA,
    WISHLIST,
    AFFAIRS,
    AWARDS;

    fun getTextMenu() = when (this) {
        MEDIA -> TextApp.titleMedia
        WISHLIST -> TextApp.titleWishList
        AFFAIRS -> TextApp.titleAffairs
        AWARDS -> TextApp.titleAwards
    }
}
