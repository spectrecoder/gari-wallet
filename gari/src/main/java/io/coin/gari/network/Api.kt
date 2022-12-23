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
        const val WALLET_DETAILS = "/appwallet/get-wallet-details"
        const val WALLET_CREATE = "/appwallet/new-user-wallet"
        const val AIRDROP_GET_ENCODED_TRANSACTION = "/appwallet/get-encode-transaction-airdrop"
        const val AIRDROP_SEND_SIGNED_TRANSACTION = "/appwallet/airdrop"
        const val GET_SIGNED_TRANSACTION = "/appwallet/get-encode-transaction"
    }

    object Param {
        const val PUBLIC_KEY = "publicKey"
        const val AIRDROP_AMOUNT = "airdropAmount"
        const val ENCODED_TRANSACTION = "encodedTransaction"
        const val RECEIVER_PUBLIC_KEY = "receiverPublicKey"
        const val AMOUNT = "amount"
    }
}