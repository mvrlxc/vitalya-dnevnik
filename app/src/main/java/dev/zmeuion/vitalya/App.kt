package dev.zmeuion.vitalya

import android.app.Application
import dev.zmeuion.vitalya.di.DataModule
import dev.zmeuion.vitalya.di.DatabaseModule
import dev.zmeuion.vitalya.di.NetworkModule
import dev.zmeuion.vitalya.di.UiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(NetworkModule, DatabaseModule, UiModule, DataModule)
            )
        }
    }
}