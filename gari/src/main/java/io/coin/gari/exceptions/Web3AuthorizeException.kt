package io.coin.gari.exceptions

internal class Web3AuthorizeException : Exception {

    constructor() : super()

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String) : super(message)
}