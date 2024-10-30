package com.mozhimen.composek.ui.node

import com.mozhimen.composek.ui.layout.LayoutCoordinates

/**
 * @ClassName GlobalPositionAwareModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:53
 * @Version 1.0
 */
/**
 * A [androidx.compose.ui.Modifier.Node] whose [onGloballyPositioned] is called with the final
 * LayoutCoordinates of the Layout when the global position of the content may have changed.
 * Note that it will be called after a composition when the coordinates are finalized.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.layout.OnGloballyPositionedModifier]
 *
 * Usage example:
 * @sample androidx.compose.ui.samples.OnGloballyPositioned
 * @sample androidx.compose.ui.samples.GlobalPositionAwareModifierNodeSample
 *
 * @see LayoutCoordinates
 */
interface GlobalPositionAwareModifierNode : DelegatableNode {
    /**
     * Called with the final LayoutCoordinates of the Layout after measuring.
     * Note that it will be called after a composition when the coordinates are finalized.
     * The position in the modifier chain makes no difference in either
     * the [LayoutCoordinates] argument or when the [onGloballyPositioned] is called.
     */
    fun onGloballyPositioned(coordinates: LayoutCoordinates)
}
