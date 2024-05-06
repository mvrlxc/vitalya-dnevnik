package dev.zmeuion.vitalya.di

import androidx.room.Room
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.database.Database
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java, "database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<Database>().getScheduleDao()
    }

    single { DataStoreManager(androidContext()) }

}