package dev.zmeuion.vitalya.di

import dev.zmeuion.vitalya.data.ScheduleRepository
import org.koin.dsl.module

val DataModule = module {

    single { ScheduleRepository(get(), get()) }

}