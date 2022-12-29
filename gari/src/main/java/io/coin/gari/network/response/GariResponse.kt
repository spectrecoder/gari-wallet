package io.coin.gari.network.response

import com.google.gson.annotations.SerializedName

internal open class GariResponse<Data>(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("data")
    val data: Data? = null
)