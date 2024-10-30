package com.mozhimen.composek.ui.text.platform

import android.text.TextPaint
import androidx.annotation.VisibleForTesting
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextDecoration
import com.mozhimen.composek.ui.text.platform.extensions.correctBlurRadius
import kotlin.math.roundToInt

/**
 * @ClassName AndroidTextPaint
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal class AndroidTextPaint(flags: Int, density: Float) : TextPaint(flags) {
    init {
        this.density = density
    }

    // A wrapper to use Compose Paint APIs on this TextPaint
    private val composePaint: Paint = this.asComposePaint()

    private var textDecoration: TextDecoration = TextDecoration.None

    @VisibleForTesting
    internal var shadow: Shadow = Shadow.None

    private var drawStyle: DrawStyle? = null

    fun setTextDecoration(textDecoration: TextDecoration?) {
        if (textDecoration == null) return
        if (this.textDecoration != textDecoration) {
            this.textDecoration = textDecoration
            isUnderlineText = TextDecoration.Underline in this.textDecoration
            isStrikeThruText = TextDecoration.LineThrough in this.textDecoration
        }
    }

    fun setShadow(shadow: Shadow?) {
        if (shadow == null) return
        if (this.shadow != shadow) {
            this.shadow = shadow
            if (this.shadow == Shadow.None) {
                clearShadowLayer()
            } else {
                setShadowLayer(
                    correctBlurRadius(this.shadow.blurRadius),
                    this.shadow.offset.x,
                    this.shadow.offset.y,
                    this.shadow.color.toArgb()
                )
            }
        }
    }

    fun setColor(color: Color) {
        if (color.isSpecified) {
            composePaint.color = color
            composePaint.shader = null
        }
    }

    fun setBrush(brush: Brush?, size: Size, alpha: Float = Float.NaN) {
        // if size is unspecified and brush is not null, nothing should be done.
        // it basically means brush is given but size is not yet calculated at this time.
        if ((brush is SolidColor && brush.value.isSpecified) ||
            (brush is ShaderBrush && size.isSpecified)) {
            // alpha is always applied even if Float.NaN is passed to applyTo function.
            // if it's actually Float.NaN, we simply send the current value
            brush.applyTo(
                size,
                composePaint,
                if (alpha.isNaN()) composePaint.alpha else alpha.coerceIn(0f, 1f)
            )
        } else if (brush == null) {
            composePaint.shader = null
        }
    }

    fun setDrawStyle(drawStyle: DrawStyle?) {
        if (drawStyle == null) return
        if (this.drawStyle != drawStyle) {
            this.drawStyle = drawStyle
            when (drawStyle) {
                Fill -> {
                    // Stroke properties such as strokeWidth, strokeMiter are not re-set because
                    // Fill style should make those properties no-op. Next time the style is set
                    // as Stroke, stroke properties get re-set as well.
                    composePaint.style = PaintingStyle.Fill
                }
                is Stroke -> {
                    composePaint.style = PaintingStyle.Stroke
                    composePaint.strokeWidth = drawStyle.width
                    composePaint.strokeMiterLimit = drawStyle.miter
                    composePaint.strokeJoin = drawStyle.join
                    composePaint.strokeCap = drawStyle.cap
                    composePaint.pathEffect = drawStyle.pathEffect
                }
            }
        }
    }

    // BlendMode is only available to DrawScope.drawText.
    // not intended to be used by TextStyle/SpanStyle.
    var blendMode: BlendMode by composePaint::blendMode
}

/**
 * Accepts an alpha value in the range [0f, 1f] then maps to an integer value
 * in [0, 255] range.
 */
internal fun TextPaint.setAlpha(alpha: Float) {
    if (!alpha.isNaN()) {
        val alphaInt = alpha.coerceIn(0f, 1f).times(255).roundToInt()
        setAlpha(alphaInt)
    }
}
