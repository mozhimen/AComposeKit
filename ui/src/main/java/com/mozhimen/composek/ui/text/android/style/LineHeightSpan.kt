package com.mozhimen.composek.ui.text.android.style

import android.graphics.Paint.FontMetricsInt
import kotlin.math.ceil

/**
 * @ClassName LineHeightSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The span which modifies the height of the covered paragraphs. A paragraph is defined as a
 * segment of string divided by '\n' character. To make sure the span work as expected, the
 * boundary of this span should align with paragraph boundary.
 * @constructor Create a LineHeightSpan which sets the line height to `height` physical pixels.
 * @param lineHeight The specified line height in pixel unit, which is the space between the
 * baseline of adjacent lines.
 */
internal class LineHeightSpan(
    val lineHeight: Float
) : android.text.style.LineHeightSpan {

    override fun chooseHeight(
        text: CharSequence,
        start: Int,
        end: Int,
        spanstartVertical: Int,
        lineHeight: Int,
        fontMetricsInt: FontMetricsInt
    ) {
        // In StaticLayout, line height is computed with descent - ascent
        val currentHeight = fontMetricsInt.lineHeight()
        // If current height is not positive, do nothing.
        if (currentHeight <= 0) {
            return
        }
        val ceiledLineHeight = ceil(this.lineHeight).toInt()
        val ratio = ceiledLineHeight * 1.0f / currentHeight
        fontMetricsInt.descent = ceil(fontMetricsInt.descent * ratio.toDouble()).toInt()
        fontMetricsInt.ascent = fontMetricsInt.descent - ceiledLineHeight
    }
}
