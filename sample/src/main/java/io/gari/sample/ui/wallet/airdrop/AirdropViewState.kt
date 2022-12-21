package io.gari.sample.ui.wallet.airdrop

sealed class AirdropViewState {

    object Ready : AirdropViewState()

    class Completed(val signature: String) : AirdropViewState()
}