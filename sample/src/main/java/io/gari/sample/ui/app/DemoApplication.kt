package io.gari.sample.ui.app

import android.app.Application
import io.gari.sample.di.KoinLoader

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KoinLoader.start(this)
    }
}