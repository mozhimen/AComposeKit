package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * @ClassName FontFeatureSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Span that change font feature settings for font.
 */
internal class FontFeatureSpan(val fontFeatureSettings: String) : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.fontFeatureSettings = fontFeatureSettings
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.fontFeatureSettings = fontFeatureSettings
    }
}
