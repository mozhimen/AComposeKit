package com.mozhimen.composek.ui.layout

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import com.mozhimen.composek.ui.node.LayoutNode

/**
 * @ClassName RootMeasurePolicy
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:24
 * @Version 1.0
 */
internal object RootMeasurePolicy : LayoutNode.NoIntrinsicsMeasurePolicy(
    "Undefined intrinsics block and it is required"
) {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        return when {
            measurables.isEmpty() -> {
                layout(constraints.minWidth, constraints.minHeight) {}
            }
            measurables.size == 1 -> {
                val placeable = measurables[0].measure(constraints)
                layout(
                    constraints.constrainWidth(placeable.width),
                    constraints.constrainHeight(placeable.height)
                ) {
                    placeable.placeRelativeWithLayer(0, 0)
                }
            }
            else -> {
                val placeables = measurables.fastMap {
                    it.measure(constraints)
                }
                var maxWidth = 0
                var maxHeight = 0
                placeables.fastForEach { placeable ->
                    maxWidth = maxOf(placeable.width, maxWidth)
                    maxHeight = maxOf(placeable.height, maxHeight)
                }
                layout(
                    constraints.constrainWidth(maxWidth),
                    constraints.constrainHeight(maxHeight)
                ) {
                    placeables.fastForEach { placeable ->
                        placeable.placeRelativeWithLayer(0, 0)
                    }
                }
            }
        }
    }
}
