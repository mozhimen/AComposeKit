package com.mozhimen.composek.material

import androidx.annotation.IntRange
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * @ClassName ProgressSemantics
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */

/**
 * Contains the [semantics] required for a determinate progress indicator or the progress part of
 * a slider, that represents progress within [valueRange]. [value] outside of this range will be
 * coerced into this range.
 *
 * @sample androidx.compose.foundation.samples.DeterminateProgressSemanticsSample
 *
 * @param value current value of the ProgressIndicator/Slider. If outside of [valueRange] provided,
 * value will be coerced to this range. Must not be NaN.
 * @param valueRange range of values that value can take. Passed [value] will be coerced to this
 * range
 * @param steps if greater than 0, specifies the amounts of discrete values, evenly distributed
 * between across the whole value range. If 0, any value from the range specified is allowed.
 * Must not be negative.
 */
@Stable
fun Modifier.progressSemantics(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0
): Modifier {
    // Older versions of Talkback will ignore nodes with range info which aren't focusable or
    // screen reader focusable. Setting this semantics as merging descendants will mark it as
    // screen reader focusable.
    return semantics(mergeDescendants = true) {
        progressBarRangeInfo =
            ProgressBarRangeInfo(value.coerceIn(valueRange), valueRange, steps)
        stateDescription = "progress"
    }
}

/**
 * Contains the [semantics] required for an indeterminate progress indicator, that represents the
 * fact of the in-progress operation.
 *
 * If you need determinate progress 0.0 to 1.0, consider using overload with the progress
 * parameter.
 *
 * @sample androidx.compose.foundation.samples.IndeterminateProgressSemanticsSample
 *
 */
@Stable
fun Modifier.progressSemantics(): Modifier {
    // Older versions of Talkback will ignore nodes with range info which aren't focusable or
    // screen reader focusable. Setting this semantics as merging descendants will mark it as
    // screen reader focusable.
    return semantics(mergeDescendants = true) {
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        stateDescription = "progress"
    }
}
