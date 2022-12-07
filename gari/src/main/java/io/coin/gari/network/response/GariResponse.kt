package io.coin.gari.network.response

import com.google.gson.annotations.SerializedName

internal open class GariResponse(
    @SerializedName("code")
    val code: Int? = null
)