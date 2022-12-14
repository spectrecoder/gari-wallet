package io.coin.gari.domain.web3auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import io.coin.gari.R
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.utils.decodeHex
import java8.util.concurrent.CompletableFuture

class Web3AuthManagerImpl : Web3AuthManager {

    private lateinit var web3Auth: Web3Auth
    private var loginResult: CompletableFuture<ByteArray>? = null

    override fun onCreate(context: Context, intent: Intent?) {
        val clientId = context.getString(R.string.web3auth_project_id)

        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = clientId,
                network = Web3Auth.Network.TESTNET,
                redirectUrl = Uri.parse("${context.packageName}://auth"),
                loginConfig = hashMapOf(
                    "jwt" to LoginConfigItem(
                        clientId = clientId,
                        verifier = "pubg-game-verifier",
                        name = "pubg-game-verifier",
                        typeOfLogin = TypeOfLogin.JWT
                    )
                )
            )
        )

        web3Auth.setResultUrl(intent?.data)

        web3Auth.sessionResponse().whenComplete { loginResponse, error ->
            loginResult?.let { handleResult(loginResponse, error, it) }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        web3Auth.setResultUrl(intent?.data)
    }

    override fun login(jwtToken: String): CompletableFuture<ByteArray> {
        val resultOutput = CompletableFuture<ByteArray>()

        web3Auth.login(
            LoginParams(
                loginProvider = Provider.JWT, extraLoginOptions = ExtraLoginOptions(
                    verifierIdField = "uid",
                    id_token = jwtToken,
                    domain = "https://demo-gari-sdk.vercel.app/"
                )
            )
        ).whenComplete { loginResponse, error ->
            handleResult(loginResponse, error, resultOutput)
        }

        return resultOutput.also {
            loginResult = it
        }
    }

    private fun handleResult(
        loginResponse: Web3AuthResponse?,
        error: Throwable?,
        resultOutput: CompletableFuture<ByteArray>
    ) {
        if (error != null || loginResponse == null) {
            resultOutput.completeExceptionally(Web3AuthorizeException(error))
        } else {
            val ed25519PrivateKey = loginResponse.ed25519PrivKey

            if (ed25519PrivateKey.isNullOrEmpty()) {
                resultOutput.completeExceptionally(Web3AuthorizeException("Missing private key"))
                return
            }

            val decodedKey = try {
                ed25519PrivateKey.decodeHex()
            } catch (error: Throwable) {
                resultOutput.completeExceptionally(
                    Web3AuthorizeException(
                        "Unable to decode ed25519PrivateKey", error
                    )
                )
                return
            }

            resultOutput.complete(decodedKey)
        }
    }
}