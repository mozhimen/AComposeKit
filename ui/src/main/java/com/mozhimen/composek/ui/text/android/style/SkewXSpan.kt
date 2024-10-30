package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * @ClassName SkewXSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Span which shear text in x direction. A pixel at (x, y) will be transfer to (x + y * skewX, y),
 * where y is the distant above baseline.
 *
 */
internal open class SkewXSpan(val skewX: Float) : MetricAffectingSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.textSkewX = skewX + textPaint.textSkewX
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.textSkewX = skewX + textPaint.textSkewX
    }
}
