package com.mozhimen.composek.ui.layout

import androidx.compose.ui.unit.Constraints

/**
 * @ClassName Measurable
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * A part of the composition that can be measured. This represents a layout.
 * The instance should never be stored.
 */
interface Measurable : IntrinsicMeasurable {
    /**
     * Measures the layout with [constraints], returning a [Placeable] layout that has its new
     * size. A [Measurable] can only be measured once inside a layout pass.
     */
    fun measure(constraints: Constraints): Placeable
}
