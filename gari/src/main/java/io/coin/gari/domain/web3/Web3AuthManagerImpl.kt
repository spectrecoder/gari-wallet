package io.coin.gari.domain.web3

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import io.coin.gari.exceptions.IllegalWeb3AuthConfigException
import io.coin.gari.exceptions.Web3AuthorizeException
import io.coin.gari.utils.decodeHex
import java8.util.concurrent.CompletableFuture

internal class Web3AuthManagerImpl(
    private val web3AuthConfig: Web3AuthConfig
) : Web3AuthManager {

    private lateinit var web3Auth: Web3Auth
    private var loginResult: CompletableFuture<ByteArray>? = null

    override fun onCreate(context: Context, intent: Intent?) {
        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = web3AuthConfig.web3AuthClientId,
                network = web3AuthConfig.network.toWeb3Domain(),
                redirectUrl = Uri.parse(web3AuthConfig.redirectUrl),
                loginConfig = hashMapOf(
                    "jwt" to LoginConfigItem(
                        clientId = web3AuthConfig.web3AuthClientId,
                        verifier = web3AuthConfig.verifier,
                        name = web3AuthConfig.verifierTitle,
                        typeOfLogin = TypeOfLogin.JWT
                    )
                )
            )
        )
    }

    @Throws(IllegalWeb3AuthConfigException::class)
    override fun verifyConfig() {
        web3AuthConfig.redirectUrl.requireConfig("redirectUrl is not allowed to be empty")
        web3AuthConfig.web3AuthClientId.requireConfig("web3AuthClientId is not allowed to be empty")
    }

    override fun onNewIntent(intent: Intent?) {
        web3Auth.setResultUrl(intent?.data)
    }

    override fun login(jwtToken: String): CompletableFuture<ByteArray> {
        val resultOutput = CompletableFuture<ByteArray>()

        web3Auth.login(
            LoginParams(
                loginProvider = Provider.JWT,
                extraLoginOptions = ExtraLoginOptions(
                    verifierIdField = web3AuthConfig.verifierIdField,
                    id_token = jwtToken,
                    domain = web3AuthConfig.verifierDomain,
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
        ed25519PrivateKey: String?,
        error: Throwable?,
        resultOutput: CompletableFuture<ByteArray>
    ) {
        if (error != null || ed25519PrivateKey.isNullOrEmpty()) {
            resultOutput.completeExceptionally(Web3AuthorizeException(error))
        } else {
            val decodedKey = try {
                ed25519PrivateKey.decodeHex()
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

    private fun String.requireConfig(errorMessage: String) {
        if (this.isEmpty()) {
            throw IllegalWeb3AuthConfigException(errorMessage)
        }
    }
}