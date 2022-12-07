package io.coin.gari.network.response

import com.google.gson.annotations.SerializedName

internal class WalletDetailsResponse(
    @SerializedName("userExist")
    val userExist: Boolean? = null
) : GariResponse()