package ru.data.common.domain.di

import org.koin.dsl.module
import ru.data.common.db.DBApp
import ru.data.common.domain.use_case.UseCaseAlbums
import ru.data.common.domain.use_case.UseCaseDroid
import ru.data.common.domain.use_case.UseCaseInterests
import ru.data.common.domain.use_case.UseCaseLocations
import ru.data.common.domain.use_case.UseCaseMedia
import ru.data.common.domain.use_case.UseCaseMembershipDroid
import ru.data.common.domain.use_case.UseCasePosts
import ru.data.common.domain.use_case.UseCaseRequestsDroid
import ru.data.common.domain.use_case.UseCaseSignIn
import ru.data.common.domain.use_case.UseCaseUser
import ru.data.common.domain.use_case.UseCaseUsers
import ru.data.common.domain.use_case.UseCaseWishesAndList


    fun providersModuleDomain() = module {
        factory { UseCaseSignIn(get(), get(), get(), get()) }
        factory { UseCaseUser(get(), get(), get(), get()) }
        factory { UseCaseInterests(get()) }
        factory { UseCaseUsers(get(), get()) }
        factory { UseCaseMembershipDroid(get(), get()) }
        factory { UseCaseDroid(get(), get(), get(), get()) }
        factory { UseCaseRequestsDroid(get(), get()) }
        factory { UseCaseAlbums(get(), get()) }
        factory { UseCaseMedia(get(), get()) }
        factory { UseCasePosts(get(), get(), get(), get()) }
        factory { UseCaseWishesAndList(get(), get()) }
        factory {
            val cityDb = get<DBApp>().getDB()
            UseCaseLocations(get(), cityDb)
        }
    }



