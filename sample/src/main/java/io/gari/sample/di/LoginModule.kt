package io.gari.sample.di

import io.gari.sample.data.LoginRepository
import io.gari.sample.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    viewModel { LoginViewModel(get()) }

    factory { LoginRepository(get()) }
}