package ru.data.common.ds.di


import org.koin.dsl.module
import ru.data.common.ds.FileStoreApp


fun providersModuleDataStore(fileProvide: ProvideFile)= module {
    single { FileStoreApp(fileProvide)}
}

