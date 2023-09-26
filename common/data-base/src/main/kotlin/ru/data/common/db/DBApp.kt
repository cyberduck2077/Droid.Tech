package ru.data.common.db

import android.content.Context

class DBApp(appContext: Context) {

    private val baseInit = DatabaseCities.build(appContext)

    fun getDB(): CityDaoRoom = baseInit.cityDb
}