package io.coin.gari.network.response

import com.google.gson.annotations.SerializedName
import io.coin.gari.network.entity.ApiGariWallet

internal class WalletDetailsResponse(
    @SerializedName("userExist")
    val userExist: Boolean? = null
) : GariResponse<ApiGariWallet>()