package dev.zmeuion.vitalya.di

import android.content.Context
import androidx.work.WorkerParameters
import dev.zmeuion.vitalya.data.LoginRepository
import dev.zmeuion.vitalya.data.OptionsRepository
import dev.zmeuion.vitalya.data.ScheduleRepository
import dev.zmeuion.vitalya.data.UpdateScheduleWorker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataModule = module {

    single { ScheduleRepository(get(), get()) }

    single { LoginRepository(get(), get()) }

    single { OptionsRepository(get()) }

    // single { UpdateScheduleWorker(get(), get(), androidContext(), get()) }

}