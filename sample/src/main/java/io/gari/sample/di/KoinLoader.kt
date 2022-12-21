package io.gari.sample.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object KoinLoader {

    fun start(application: Context) {
        val moduleList = loginModule +
                walletModule +
                airdropModule +
                transactionsModule +
                networkModule

        startKoin {
            androidContext(application)
            modules(moduleList)
        }
    }
}