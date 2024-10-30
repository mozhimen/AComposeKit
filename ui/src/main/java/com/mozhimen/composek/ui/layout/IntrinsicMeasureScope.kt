package com.mozhimen.composek.ui.layout

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * @ClassName IntrinsicMeasureScope
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * The receiver scope of a layout's intrinsic measurements lambdas.
 */
interface IntrinsicMeasureScope : Density {
    /**
     * The [LayoutDirection] of the `Layout` or `LayoutModifier` using the measure scope
     * to measure their children.
     */
    val layoutDirection: LayoutDirection

    /**
     * This indicates whether the ongoing measurement is for lookahead pass.
     * [IntrinsicMeasureScope] implementations, especially [MeasureScope] implementations should
     * override this flag to reflect whether the measurement is intended for lookahead pass.
     *
     * @sample androidx.compose.ui.samples.animateContentSizeAfterLookaheadPass
     */
    val isLookingAhead: Boolean
        get() = false
}