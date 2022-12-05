package io.coin.gari.domain

object GariWallet {

    private var clientId: String = ""

    fun initialize(clientId: String) {
        this.clientId = clientId
    }
}