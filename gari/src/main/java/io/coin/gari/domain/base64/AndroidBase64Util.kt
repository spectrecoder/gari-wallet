package io.coin.gari.domain.base64

import android.util.Base64

class AndroidBase64Util : Base64Util {

    override fun toBase64(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.DEFAULT)
    }

    override fun fromBase64(input: String): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }
}