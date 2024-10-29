package com.mozhimen.composek.ui.layout

import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.unit.LayoutDirection
import com.mozhimen.composek.ui.node.checkMeasuredSize

/**
 * @ClassName Layout
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
// A large value to use as a replacement for Infinity with DefaultIntrinisicMeasurable.
// A layout likely won't use this dimension as it is opposite from the one being measured in
// the max/min Intrinsic Width/Height, but it is possible. For example, if the direct child
// uses normal measurement/layout, we don't want to return Infinity sizes when its parent
// asks for intrinsic size. 15 bits can fit in a Constraints, so should be safe unless
// the parent adds to it and the other dimension is also very large (> 2^15).
internal const val LargeDimension = (1 shl 15) - 1

/**
 * Receiver scope for [Layout]'s and [LayoutModifier]'s layout lambda when used in an intrinsics
 * call.
 */
internal class IntrinsicsMeasureScope(
    intrinsicMeasureScope: IntrinsicMeasureScope,
    override val layoutDirection: LayoutDirection,
) : MeasureScope, IntrinsicMeasureScope by intrinsicMeasureScope {
    override fun layout(
        width: Int,
        height: Int,
        alignmentLines: Map<AlignmentLine, Int>,
        placementBlock: Placeable.PlacementScope.() -> Unit
    ): MeasureResult {
        val w = width.coerceAtLeast(0)
        val h = height.coerceAtLeast(0)
        checkMeasuredSize(w, h)
        return object : MeasureResult {
            override val width: Int
                get() = w
            override val height: Int
                get() = h
            override val alignmentLines: Map<AlignmentLine, Int>
                get() = alignmentLines

            override fun placeChildren() {
                // Intrinsics should never be placed
            }
        }
    }
}
