package io.coin.gari.network

internal object Api {

    object Url {
        const val BASE_URL = "https://dev-gari-sdk-backend.chingari.io/"
    }

    object Header {
        const val GARI_CLIENT_ID = "gariclientid"
        const val TOKEN = "token"
    }

    object Path {
        const val WALLET_DETAILS = "/Appwallet/getWalletDetails"
    }
}