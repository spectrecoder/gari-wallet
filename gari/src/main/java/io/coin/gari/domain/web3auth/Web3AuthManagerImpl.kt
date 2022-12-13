package io.coin.gari.domain.web3auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import io.coin.gari.R
import java8.util.concurrent.CompletableFuture

class Web3AuthManagerImpl : Web3AuthManager {

    private lateinit var web3Auth: Web3Auth

    private var privateKey: CompletableFuture<ByteArray>? = null

    override fun onCreate(context: Context, intent: Intent?) {
        val clientId = context.getString(R.string.web3auth_project_id)
        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = clientId,
                network = Web3Auth.Network.TESTNET,
                redirectUrl = Uri.parse("io.gari.sample://auth"),
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

        // Handle user signing in when app is not alive
        web3Auth.setResultUrl(intent?.data)

        // Call sessionResponse() in onCreate() to check for any existing session.
        val sessionResponse: CompletableFuture<Web3AuthResponse> = web3Auth.sessionResponse()
        sessionResponse.whenComplete { loginResponse, error ->
            if (error == null) {
                Log.d("MainActivity_Web3Auth", "Good: ${loginResponse}")
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
                // Ideally, you should initiate the login function here.
            }
        }
    }

    // Handle user signing in when app is active
    override fun onNewIntent(intent: Intent?) {
        web3Auth.setResultUrl(intent?.data)
    }

    override fun login(jwtToken: String): CompletableFuture<ByteArray> {
        val resultOutput = CompletableFuture<ByteArray>()

        web3Auth.login(
            LoginParams(
                loginProvider = Provider.JWT,
                extraLoginOptions = ExtraLoginOptions(
                    verifierIdField = "uid",
                    id_token = jwtToken,
                    domain = "https://demo-gari-sdk.vercel.app/"
                )
            )
        ).whenComplete { t, u ->
            resultOutput.complete(t.ed25519PrivKey!!.toByteArray())
        }

        this.privateKey = resultOutput

        return resultOutput
    }
}