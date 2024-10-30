package com.mozhimen.composek.foundation.textk2.input.internal

/**
 * @ClassName CodepointHelpers
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
internal  fun CharSequence.codePointAt(index: Int): Int =
    java.lang.Character.codePointAt(this, index)

internal  fun CharSequence.codePointCount(): Int =
    java.lang.Character.codePointCount(this, 0, length)

internal  fun charCount(codePoint: Int): Int =
    java.lang.Character.charCount(codePoint)