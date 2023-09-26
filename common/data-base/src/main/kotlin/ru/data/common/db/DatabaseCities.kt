package ru.data.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.data.common.models.data_base.CityDao
import ru.data.common.models.data_base.CountryDao
import ru.data.common.models.data_base.RegionDao

@Database(
    entities = [
        CityDao::class,
        RegionDao::class,
        CountryDao::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseCities : RoomDatabase() {

    abstract val cityDb: CityDaoRoom

    companion object Factory {
        fun build(appContext: Context): DatabaseCities =
            Room.databaseBuilder(
                appContext,
                DatabaseCities::class.java,
                "database_cities_v0"
            ).fallbackToDestructiveMigration()
                .build()
    }
}