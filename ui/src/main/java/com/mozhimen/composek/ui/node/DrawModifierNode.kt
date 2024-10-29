package com.mozhimen.composek.ui.node

import androidx.compose.ui.graphics.drawscope.ContentDrawScope

/**
 * @ClassName DrawModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * A [Modifier.Node] that draws into the space of the layout.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.draw.DrawModifier]
 *
 * @sample androidx.compose.ui.samples.DrawModifierNodeSample
 */
interface DrawModifierNode : DelegatableNode {
    fun ContentDrawScope.draw()
    fun onMeasureResultChanged() {}
}

/**
 * Invalidates this modifier's draw layer, ensuring that a draw pass will
 * be run on the next frame.
 */
fun DrawModifierNode.invalidateDraw() {
    if (node.isAttached) {
        requireCoordinator(Nodes.Any).invalidateLayer()
    }
}
