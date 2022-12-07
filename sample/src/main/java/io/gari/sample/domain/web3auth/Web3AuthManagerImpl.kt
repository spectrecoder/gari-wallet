package io.gari.sample.domain.web3auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import java8.util.concurrent.CompletableFuture

class Web3AuthManagerImpl : Web3AuthManager {

    private lateinit var web3Auth: Web3Auth

    override fun onCreate(context: Context, intent: Intent?) {
        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = WEB3_AUTH_CLIENT_ID,
                network = Web3Auth.Network.TESTNET,
                redirectUrl = Uri.parse("${context.packageName}://auth"),
                whiteLabel = WhiteLabelData(
                    "Gari SDK Sample",
                    null,
                    null,
                    "en",
                    true,
                    hashMapOf("primary" to "#229954")
                ),
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

    override fun onNewIntent(intent: Intent?) {
        // Handle user signing in when app is active
        web3Auth.setResultUrl(intent?.data)
    }

    override fun login() {
        val selectedLoginProvider = Provider.GOOGLE   // Can be GOOGLE, FACEBOOK, TWITCH etc.
        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> =
            web3Auth.login(LoginParams(selectedLoginProvider))

        loginCompletableFuture.whenComplete { loginResponse, error ->
            if (error == null) {
                println(loginResponse)
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private companion object {

        /* client id from web3 auth dashboard */
        private const val WEB3_AUTH_CLIENT_ID =
            "BO12qnqLP_vnsd3iCcH7sU3GGqYmOGr_1IgDno3t35KjWFZcdk7HIPeGGJINB4DKyvsX3YZeFdjwSbCUItLJI3U"
    }
}