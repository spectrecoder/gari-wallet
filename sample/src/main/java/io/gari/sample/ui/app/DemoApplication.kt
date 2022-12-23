package io.gari.sample.ui.app

import io.coin.gari.domain.Gari
import io.coin.gari.ui.app.GariApp
import io.gari.sample.di.KoinLoader

class DemoApplication : GariApp() {

    override fun onCreate() {
        super.onCreate()

        KoinLoader.start(this)

        Gari.initialize("d8817deb-dceb-40a4-a890-21f0286c8fba")
        Gari.setLogsEnabled(true)
    }
}