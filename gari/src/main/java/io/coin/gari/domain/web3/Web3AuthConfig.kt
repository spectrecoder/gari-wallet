package io.coin.gari.domain.web3

import android.os.Parcelable
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

