package dev.zmeuion.vitalya.di

import dev.zmeuion.vitalya.ui.screens.TestScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val UiModule = module {

    viewModel {  TestScreenViewModel(get())  }
}