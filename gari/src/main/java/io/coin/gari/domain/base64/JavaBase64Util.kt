package io.coin.gari.domain.base64

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class JavaBase64Util : Base64Util {

    override fun toBase64(input: ByteArray): String {
        return Base64.getMimeEncoder().encodeToString(input)
    }

    override fun fromBase64(input: String): ByteArray {
        return Base64.getMimeDecoder().decode(input)
    }
}