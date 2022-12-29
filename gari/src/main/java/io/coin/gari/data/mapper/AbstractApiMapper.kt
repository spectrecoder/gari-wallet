package io.coin.gari.data.mapper

import io.coin.gari.exceptions.InvalidResponseBodyException
import kotlin.reflect.KProperty0

abstract class AbstractApiMapper<Input, Output> : Mapper<Input, Output> {

    @Throws(InvalidResponseBodyException::class)
    fun requireNotEmpty(input: String?, fieldName: KProperty0<String?>): String {
        if (input.isNullOrEmpty()) {
            throw InvalidResponseBodyException("API response missing property: [${fieldName.name}]")
        }

        return input
    }
}