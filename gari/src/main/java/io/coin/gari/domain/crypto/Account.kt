package io.coin.gari.domain.crypto

class Account {
    private var keyPair: TweetNaclFast.Signature.KeyPair

    constructor() {
        keyPair = TweetNaclFast.Signature.keyPair()
    }

    constructor(secretKey: ByteArray) {
        keyPair = TweetNaclFast.Signature.keyPair_fromSecretKey(secretKey)
    }

    private constructor(keyPair: TweetNaclFast.Signature.KeyPair) {
        this.keyPair = keyPair
    }

    val publicKey: PublicKey
        get() = PublicKey(keyPair.publicKey)

    val secretKey: ByteArray
        get() = keyPair.secretKey
}