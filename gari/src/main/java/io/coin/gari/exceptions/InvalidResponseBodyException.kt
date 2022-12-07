package io.coin.gari.exceptions

/**
 * This exception will be thrown in case SDK received response from GARI service,
 * which is not able to parse
 */
class InvalidResponseBodyException : Exception {

    constructor(cause: Throwable) : super(cause)

    constructor() : super()
}