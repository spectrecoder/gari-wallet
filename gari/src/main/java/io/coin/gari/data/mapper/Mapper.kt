package io.coin.gari.data.mapper

import io.coin.gari.exceptions.InvalidResponseBodyException

internal interface Mapper<in Input, out Output> {

    @Throws(InvalidResponseBodyException::class)
    fun from(model: Input): Output
}