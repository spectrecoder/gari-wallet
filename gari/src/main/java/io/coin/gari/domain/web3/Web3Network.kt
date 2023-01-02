package io.coin.gari.domain.web3

import com.web3auth.core.Web3Auth

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