package io.gari.sample.ui.app

import io.coin.gari.domain.Gari
import io.coin.gari.domain.web3.Web3AuthConfig
import io.coin.gari.domain.web3.Web3Network
import io.coin.gari.ui.app.GariApp
import io.gari.sample.di.KoinLoader

class DemoApplication : GariApp() {

    override fun onCreate() {
        super.onCreate()

        KoinLoader.start(this)

        val web3AuthConfig = Web3AuthConfig(
            web3AuthClientId = "BAGatRxirFvKTvUNeB_urIsfZsXUEh-JUcWSi432p_5pewX_0wEvYuGQBe1IjKI35lyrqTV5qDgFznmj6N7fdvY",
            redirectUrl = "io.coin.gari://auth",
            verifierIdField = "uid",
            verifier = "pubg-game-verifier",
            verifierTitle = "Gari Wallet Demo",
            verifierDomain = "https://demo-gari-sdk.vercel.app",
            network = Web3Network.TESTNET
        )

        Gari.initialize(
            clientId = "d8817deb-dceb-40a4-a890-21f0286c8fba",
            web3AuthConfig = web3AuthConfig
        )
        Gari.setLogsEnabled(true)
    }
}