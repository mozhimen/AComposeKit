package com.mozhimen.composek.ui.text.android.style

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * @ClassName TypefaceSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Span that displays text in the given Typeface. In Android Framework, TypefaceSpan that accepts
 * a Typeface as constructor argument was added in API 28, therefore was not usable before 28.
 *
 * @constructor Constructs a [android.text.style.TypefaceSpan] from a [Typeface]. The previous
 * style of the TextPaint is overridden and the style of the typeface is used.
 * @param typeface Typeface to render the text with.
 */
internal class TypefaceSpan(val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(ds: TextPaint) {
        updateTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        updateTypeface(paint)
    }

    private fun updateTypeface(paint: Paint) {
        paint.typeface = typeface
    }
}
