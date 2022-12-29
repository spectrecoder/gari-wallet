package io.coin.gari.domain.web3

import android.os.Parcelable
import com.web3auth.core.Web3Auth
import kotlinx.parcelize.Parcelize

@Parcelize
class Web3AuthConfig(
    val web3AuthClientId: String,
    val redirectUrl: String,
    val verifier: String,
    val verifierTitle: String,
    val verifierIdField: String,
    val verifierDomain: String,
    val network: Web3Network
) : Parcelable

enum class Web3Network {
    MAINNET,
    TESTNET,
    CYAN;

    fun toWeb3Domain(): Web3Auth.Network {
        return when (this) {
            MAINNET -> Web3Auth.Network.MAINNET
            TESTNET -> Web3Auth.Network.TESTNET
            CYAN -> Web3Auth.Network.CYAN
        }
    }
}