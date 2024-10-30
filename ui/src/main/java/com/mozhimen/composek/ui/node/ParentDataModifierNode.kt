package com.mozhimen.composek.ui.node

import androidx.compose.ui.unit.Density

/**
 * @ClassName ParentDataModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:41
 * @Version 1.0
 */
/**
 * A [Modifier.Node] that provides data to the parent [Layout]. This can be read from within the
 * the [Layout] during measurement and positioning, via [IntrinsicMeasurable.parentData].
 * The parent data is commonly used to inform the parent how the child [Layout] should be measured
 * and positioned.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.layout.ParentDataModifier]
 */
interface ParentDataModifierNode : DelegatableNode {
    /**
     * Provides a parentData, given the [parentData] already provided through the modifier's chain.
     */
    fun Density.modifyParentData(parentData: Any?): Any?
}

/**
 * This invalidates the current node's parent data, and ensures that layouts that utilize it will be
 * scheduled to relayout for the next frame.
 */
fun ParentDataModifierNode.invalidateParentData() = requireLayoutNode().invalidateParentData()
