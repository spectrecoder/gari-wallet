package io.coin.gari.domain.web3

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.utils.decodeHex
import java8.util.concurrent.CompletableFuture

class Web3AuthManagerImpl : Web3AuthManager {

    private lateinit var web3Auth: Web3Auth
    private var loginResult: CompletableFuture<ByteArray>? = null

    override fun onCreate(context: Context, intent: Intent?) {
        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = WEB3AUTH_CLIENT_ID,
                network = Web3Auth.Network.TESTNET,
                redirectUrl = Uri.parse(REDIRECT_URL),
                loginConfig = hashMapOf(
                    "jwt" to LoginConfigItem(
                        clientId = WEB3AUTH_CLIENT_ID,
                        verifier = WEB3AUTH_VERIFIER,
                        name = WEB3AUTH_VERIFIER_TITLE,
                        typeOfLogin = TypeOfLogin.JWT
                    )
                )
            )
        )

//        web3Auth.setResultUrl(intent?.data)

        /*web3Auth.sessionResponse().whenComplete { loginResponse, error ->
            loginResult?.let { handleResult(loginResponse?.ed25519PrivKey, error, it) }
        }*/
    }

    override fun onNewIntent(intent: Intent?) {
        web3Auth.setResultUrl(intent?.data)
    }

    override fun login(jwtToken: String): CompletableFuture<ByteArray> {
        val resultOutput = CompletableFuture<ByteArray>()

        web3Auth.login(
            LoginParams(
                loginProvider = Provider.GOOGLE,
                extraLoginOptions = ExtraLoginOptions(
                    verifierIdField = USER_VERIFIER_ID_FIELD,
                    id_token = jwtToken,
                    domain = TOKEN_VERIFIER_DOMAIN,
                )
            )
        ).whenComplete { loginResponse, error ->
            val ed25519PrivKey = loginResponse.ed25519PrivKey
            handleResult(ed25519PrivKey, error, resultOutput)
        }

        return resultOutput.also {
            loginResult = it
        }
    }

    private fun handleResult(
        ed25519PrivKey: String?,
        error: Throwable?,
        resultOutput: CompletableFuture<ByteArray>
    ) {
        if (error != null || ed25519PrivKey.isNullOrEmpty()) {
            resultOutput.completeExceptionally(Web3AuthorizeException(error))
        } else {
            val decodedKey = try {
                ed25519PrivKey.decodeHex()
            } catch (error: Throwable) {
                resultOutput.completeExceptionally(
                    Web3AuthorizeException(
                        "Unable to decode ed25519PrivateKey",
                        error
                    )
                )
                return
            }

            resultOutput.complete(decodedKey)
        }
    }

    private companion object {

        private const val USER_VERIFIER_ID_FIELD = "uid"
        private const val WEB3AUTH_CLIENT_ID =
            "BAGatRxirFvKTvUNeB_urIsfZsXUEh-JUcWSi432p_5pewX_0wEvYuGQBe1IjKI35lyrqTV5qDgFznmj6N7fdvY"
        private const val REDIRECT_URL = "io.coin.gari://auth"
        private const val WEB3AUTH_VERIFIER = "pubg-game-verifier"
        private const val WEB3AUTH_VERIFIER_TITLE = "Demo React POC"
        private const val TOKEN_VERIFIER_DOMAIN = "https://demo-gari-sdk.vercel.app"
    }
}