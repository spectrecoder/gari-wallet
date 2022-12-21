package io.gari.sample.di

import io.gari.sample.data.DemoRepository
import io.gari.sample.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    viewModel { LoginViewModel(get()) }

    factory { DemoRepository(get()) }
}