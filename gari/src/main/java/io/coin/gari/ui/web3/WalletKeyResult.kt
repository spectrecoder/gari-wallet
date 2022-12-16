package io.coin.gari.ui.web3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class WalletKeyResult : Parcelable {

    @Parcelize
    class Success(val key: ByteArray) : WalletKeyResult()

    @Parcelize
    object Failure : WalletKeyResult()

    @Parcelize
    object Canceled : WalletKeyResult()
}