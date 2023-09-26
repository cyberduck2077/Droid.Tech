package ru.droid.tech.main

import android.app.Application
import ru.droid.tech.BuildConfig
import ru.droid.tech.di.setModels
import ru.data.common.domain.di.initKoin
import ru.data.common.models.logger.LogCustom
import ru.data.common.models.res.TextApp

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TextApp.initDebugAndVersion(BuildConfig.DEBUG, BuildConfig.VERSION_NAME)
        LogCustom.init(BuildConfig.VERSION_NAME, BuildConfig.DEBUG)
        initKoin(
            enableNetworkLogs = BuildConfig.DEBUG,
            context = this@App
        ) {
            modules(setModels)
        }
    }
}