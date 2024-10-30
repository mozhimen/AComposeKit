package com.mozhimen.composek.ui.node

import androidx.compose.ui.unit.IntSize
import com.mozhimen.composek.ui.layout.LayoutCoordinates

/**
 * @ClassName LayoutAwareModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:52
 * @Version 1.0
 */
/**
 * A [androidx.compose.ui.Modifier.Node] which receives various callbacks in response to local
 * changes in layout.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.layout.OnRemeasuredModifier] and
 * [androidx.compose.ui.layout.OnPlacedModifier]
 *
 * Example usage:
 * @sample androidx.compose.ui.samples.OnSizeChangedSample
 * @sample androidx.compose.ui.samples.OnPlaced
 * @sample androidx.compose.ui.samples.LayoutAwareModifierNodeSample
 */
interface LayoutAwareModifierNode : DelegatableNode {
    /**
     * [onPlaced] is called after the parent [LayoutModifier] and parent layout has
     * been placed and before child [LayoutModifier] is placed. This allows child
     * [LayoutModifier] to adjust its own placement based on where the parent is.
     */
    fun onPlaced(coordinates: LayoutCoordinates) {}

    /**
     * This method is called when the layout content is remeasured. The
     * most common usage is [onSizeChanged].
     */
    fun onRemeasured(size: IntSize) {}
}
