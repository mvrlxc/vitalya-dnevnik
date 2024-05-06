package dev.zmeuion.vitalya.di

import android.os.Build
import androidx.annotation.RequiresApi
import dev.zmeuion.vitalya.data.LoginRepository
import dev.zmeuion.vitalya.ui.screens.OptionsViewModel
import dev.zmeuion.vitalya.ui.screens.login.LoginViewModel
import dev.zmeuion.vitalya.ui.screens.ScheduleScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val UiModule = module {

    viewModel { ScheduleScreenViewModel(get()) }

    viewModel { LoginViewModel(get<LoginRepository>()) }

    viewModel { OptionsViewModel(get()) }
}