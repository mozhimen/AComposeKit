package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.CharacterStyle

/**
 * @ClassName TextDecorationSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * A span which applies the underline and strike through to the affected text.
 *
 * @property isUnderlineText whether to draw the under for the affected text.
 * @property isStrikethroughText whether to draw strikethrough line for the affected text.
 */
internal class TextDecorationSpan(
    val isUnderlineText: Boolean,
    val isStrikethroughText: Boolean
) : CharacterStyle() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = isUnderlineText
        textPaint.isStrikeThruText = isStrikethroughText
    }
}
