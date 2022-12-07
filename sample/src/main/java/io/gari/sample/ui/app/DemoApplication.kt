package io.gari.sample.ui.app

import com.web3auth.core.Web3AuthApp
import io.gari.sample.di.KoinLoader

class DemoApplication : Web3AuthApp() {

    override fun onCreate() {
        super.onCreate()

        KoinLoader.start(this)
    }
}