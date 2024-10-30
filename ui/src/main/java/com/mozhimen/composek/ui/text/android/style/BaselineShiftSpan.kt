package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import kotlin.math.ceil

/**
 * @ClassName BaselineShiftSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Span which shifts the vertical position of baseline.
 */
internal open class BaselineShiftSpan(val multiplier: Float) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.baselineShift += ceil(textPaint.ascent() * multiplier).toInt()
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.baselineShift += ceil(textPaint.ascent() * multiplier).toInt()
    }
}
