package io.coin.gari.network.entity

import com.google.gson.annotations.SerializedName

internal class ApiEncodedTransaction(
    @SerializedName("encodedTransaction")
    val encodedTransaction: String? = null
)