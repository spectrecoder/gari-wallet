package io.coin.gari.ui.auth.core

import io.coin.gari.domain.web3.Web3AuthConfig

internal sealed class AuthConfigArgs {

    class Web3AuthArgs(
        val web3AuthConfig: Web3AuthConfig,
        val jwtToken: String
    ) : AuthConfigArgs()

    class WebViewArgs(
        val jwtToken: String
    ) : AuthConfigArgs()
}