package com.mozhimen.composek.ui.text.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * @ClassName BaselineShift
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The amount by which the text is shifted up or down from current the baseline.
 * @constructor
 * @sample androidx.compose.ui.text.samples.BaselineShiftSample
 * @sample androidx.compose.ui.text.samples.BaselineShiftAnnotatedStringSample
 *
 * @param multiplier shift the baseline by multiplier * (baseline - ascent)
 */
@Immutable
@kotlin.jvm.JvmInline
value class BaselineShift(val multiplier: Float) {
    companion object {
        /**
         * Default baseline shift for superscript.
         */
        @Stable
        val Superscript = BaselineShift(0.5f)

        /**
         * Default baseline shift for subscript
         */
        @Stable
        val Subscript = BaselineShift(-0.5f)

        /**
         * Constant for no baseline shift.
         */
        @Stable
        val None = BaselineShift(0.0f)
    }
}

/**
 * Linearly interpolate two [BaselineShift]s.
 */
@Stable
fun lerp(start: BaselineShift, stop: BaselineShift, fraction: Float): BaselineShift {
    return BaselineShift(
        androidx.compose.ui.util.lerp(
            start.multiplier,
            stop.multiplier,
            fraction
        )
    )
}
