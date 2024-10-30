package com.mozhimen.composek.foundation.textk2.input.internal

import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldCharSequence

/**
 * @ClassName ToCharArray
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
@OptIn(ExperimentalFoundationApi::class)
internal fun CharSequence.toCharArray(
    destination: CharArray,
    destinationOffset: Int,
    startIndex: Int,
    endIndex: Int
) {
    if (this is TextFieldCharSequence) {
        toCharArray(destination, destinationOffset, startIndex, endIndex)
    } else {
        TextUtils.getChars(this, startIndex, endIndex, destination, destinationOffset)
    }
}