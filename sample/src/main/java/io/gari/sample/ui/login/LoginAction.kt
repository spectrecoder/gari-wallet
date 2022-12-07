package io.gari.sample.ui.login

sealed class LoginAction {

    class Web3AuthTokenReady(val token: String) : LoginAction()
}