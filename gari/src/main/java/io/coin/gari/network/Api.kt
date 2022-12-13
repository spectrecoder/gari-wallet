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
        const val WALLET_CREATE = "/Appwallet/newUserWallet"
        const val AIRDROP_GET_ENCODED_TRANSACTION = "/Appwallet/getEncodeTransactionAirdrop"
        const val AIRDROP_SEND_SIGNED_TRANSACTION = "/Appwallet/airdrop"
    }

    object Param {
        const val PUBLIC_KEY = "publicKey"
        const val AIRDROP_AMOUNT = "airdropAmount"
        const val ENCODED_TRANSACTION = "encodedTransaction"
    }
}