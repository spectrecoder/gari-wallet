package io.gari.sample.network

object Api {

    object Url {


        const val DUMMY_TOKEN_DOMAIN = "https://demo-gari-sdk.vercel.app"
    }

    object Path {

        const val LOGIN = "/api/login"
        const val TRANSACTION = "/api/transaction"
        const val REFRESH_JWT_TOKEN = "/api/replaceJwtToken"
    }

    object Header {

        const val TOKEN = "token"
    }

    object Param {

        const val USER_ID = "userId"
        const val SIGNED_TRANSACTION = "partialSignedEncodedTransaction"
        const val TOKEN = "token"
    }
}