package io.coin.gari.domain.base64

interface Base64Util {

    fun toBase64(input : ByteArray) : String

    fun fromBase64(input : String) : ByteArray
}