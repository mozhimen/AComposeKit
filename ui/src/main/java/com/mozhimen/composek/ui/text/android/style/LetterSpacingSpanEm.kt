package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * @ClassName LetterSpacingSpanEm
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Span used to adjust the letter spacing, in the unit of Em.
 */
internal class LetterSpacingSpanEm(val letterSpacing: Float) : MetricAffectingSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.letterSpacing = letterSpacing
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.letterSpacing = letterSpacing
    }
}
