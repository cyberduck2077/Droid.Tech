package ru.data.common.network.di

import org.koin.dsl.module
import ru.data.common.network.Client
import ru.data.common.network.api.ApiAlbums
import ru.data.common.network.api.ApiAttachments
import ru.data.common.network.api.ApiDroid
import ru.data.common.network.api.ApiDroidMembers
import ru.data.common.network.api.ApiInterests
import ru.data.common.network.api.ApiLocation
import ru.data.common.network.api.ApiMedia
import ru.data.common.network.api.ApiPosts
import ru.data.common.network.api.ApiRequestsDroid
import ru.data.common.network.api.ApiSignIn
import ru.data.common.network.api.ApiTopics
import ru.data.common.network.api.ApiUser
import ru.data.common.network.api.ApiUsers
import ru.data.common.network.api.ApiWishesAndList

fun providersModuleNetWork(enableNetworkLogs: Boolean) = module {
    single { Client(get(), enableNetworkLogs) }

    factory { ApiSignIn(get()) }
    factory { ApiTopics(get()) }
    factory { ApiUser(get()) }
    factory { ApiUsers(get()) }
    factory { ApiLocation(get()) }
    factory { ApiMedia(get()) }
    factory { ApiInterests(get()) }
    factory { ApiRequestsDroid(get()) }
    factory { ApiDroid(get()) }
    factory { ApiDroidMembers(get()) }
    factory { ApiAlbums(get()) }
    factory { ApiPosts(get()) }
    factory { ApiAttachments(get()) }
    factory { ApiWishesAndList(get()) }
}

