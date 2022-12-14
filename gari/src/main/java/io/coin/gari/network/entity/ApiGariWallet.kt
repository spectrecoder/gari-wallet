package io.coin.gari.network.entity

import com.google.gson.annotations.SerializedName

internal class ApiGariWallet(
    @SerializedName("publicKey")
    val publicKey: String?
)