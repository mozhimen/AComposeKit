package com.mozhimen.composek.textk2.input.internal

/**
 * @ClassName MathUtils
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
/**
 * Adds [this] and [right], and if an overflow occurs returns result of [defaultValue].
 */
internal inline fun Int.addExactOrElse(right: Int, defaultValue: () -> Int): Int {
    val result = this + right
    // HD 2-12 Overflow iff both arguments have the opposite sign of the result
    return if (this xor result and (right xor result) < 0) defaultValue() else result
}

/**
 * Subtracts [right] from [this], and if an overflow occurs returns result of [defaultValue].
 */
internal inline fun Int.subtractExactOrElse(right: Int, defaultValue: () -> Int): Int {
    val result = this - right
    // HD 2-12 Overflow iff the arguments have different signs and
    // the sign of the result is different from the sign of x
    return if (this xor right and (this xor result) < 0) defaultValue() else result
}
