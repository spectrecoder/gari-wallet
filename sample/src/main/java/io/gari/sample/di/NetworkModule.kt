package io.gari.sample.di

import com.google.gson.Gson
import io.gari.sample.network.HttpClientBuilder
import io.gari.sample.network.NetworkProvider
import io.gari.sample.network.service.LoginNetworkService
import org.koin.dsl.module

val networkModule = module {

    factory { Gson() }

    factory { HttpClientBuilder() }

    single { NetworkProvider(get(), get()) }

    single { get<NetworkProvider>().provideLoginService() }

    single { LoginNetworkService(get()) }
}