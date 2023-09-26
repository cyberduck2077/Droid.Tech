package ru.data.common.di


import org.koin.dsl.module
import ru.data.common.db.DBApp

fun providersModuleDB() = module {
    single { DBApp(get()) }
}