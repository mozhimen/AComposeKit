package com.mozhimen.composek.ui.text.input

import android.view.inputmethod.ExtractedText
import androidx.compose.ui.text.input.TextFieldValue

/**
 * @ClassName InputState
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */

internal fun TextFieldValue.toExtractedText(): ExtractedText {
    val res = ExtractedText()
    res.text = text
    res.startOffset = 0
    res.partialEndOffset = text.length
    res.partialStartOffset = -1 // -1 means full text
    res.selectionStart = selection.min
    res.selectionEnd = selection.max
    res.flags = if ('\n' in text) 0 else ExtractedText.FLAG_SINGLE_LINE
    return res
}
