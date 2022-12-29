package io.coin.gari.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

fun BigDecimal.toLamports(): BigInteger =
    this.multiply(TOKEN_DECIMALS_COUNT.toPowerValue()).toBigInteger()

fun BigDecimal.fromLamports(): BigDecimal =
    this.divide(TOKEN_DECIMALS_COUNT.toPowerValue(), TOKEN_DECIMALS_COUNT, RoundingMode.HALF_UP)
        .stripTrailingZerosFixed()

fun BigDecimal.stripTrailingZerosFixed(): BigDecimal {
    return if (isZero())
        BigDecimal.ZERO
    else
        stripTrailingZeros()
}

fun BigDecimal.isZero(): Boolean = this.compareTo(BigDecimal.ZERO) == 0

fun Int.toPowerValue(): BigDecimal =
    BigDecimal(POWER_VALUE.pow(this))

private const val POWER_VALUE = 10.0
private const val TOKEN_DECIMALS_COUNT = 9
