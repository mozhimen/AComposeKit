package com.mozhimen.composek.ui.text.android.style

import android.text.TextPaint
import android.text.style.CharacterStyle

/**
 * @ClassName ShadowSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * A span which applies a shadow effect to the covered text.
 *
 */
internal class ShadowSpan(
    val color: Int,
    val offsetX: Float,
    val offsetY: Float,
    val radius: Float
) : CharacterStyle() {
    override fun updateDrawState(tp: TextPaint) {
        tp.setShadowLayer(radius, offsetX, offsetY, color)
    }
}
