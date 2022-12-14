package io.coin.gari.domain.entity

sealed class GariWalletState {

    class Activated(val pubKey: String) : GariWalletState()

    object NotExist : GariWalletState()

    class Error(val error: Throwable?) : GariWalletState()
}