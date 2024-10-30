package com.mozhimen.composek.ui.text.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * @ClassName TextGeometricTransform
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Define a geometric transformation on text.
 *
 * @param scaleX The scale of the text on the horizontal direction. The default value is 1.0f, i.e
 * no scaling.
 * @param skewX The shear of the text on the horizontal direction. A pixel at (x, y), where y is
 * the distance above baseline, will be transformed to (x + y * skewX, y). The default value is
 * 0.0f i.e. no skewing.
 */
@Immutable
class TextGeometricTransform(
    val scaleX: Float = 1.0f,
    val skewX: Float = 0f
) {
    companion object {
        @Stable
        internal val None = TextGeometricTransform(1.0f, 0.0f)
    }

    fun copy(
        scaleX: Float = this.scaleX,
        skewX: Float = this.skewX
    ): TextGeometricTransform {
        return TextGeometricTransform(scaleX, skewX)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextGeometricTransform) return false
        if (scaleX != other.scaleX) return false
        if (skewX != other.skewX) return false
        return true
    }

    override fun hashCode(): Int {
        var result = scaleX.hashCode()
        result = 31 * result + skewX.hashCode()
        return result
    }

    override fun toString(): String {
        return "TextGeometricTransform(scaleX=$scaleX, skewX=$skewX)"
    }
}

fun lerp(
    start: TextGeometricTransform,
    stop: TextGeometricTransform,
    fraction: Float
): TextGeometricTransform {
    return TextGeometricTransform(
        androidx.compose.ui.util.lerp(start.scaleX, stop.scaleX, fraction),
        androidx.compose.ui.util.lerp(start.skewX, stop.skewX, fraction)
    )
}
