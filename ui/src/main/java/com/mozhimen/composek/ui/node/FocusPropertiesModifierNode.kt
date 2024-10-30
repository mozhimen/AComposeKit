package com.mozhimen.composek.ui.node

import com.mozhimen.composek.ui.focus.FocusProperties

/**
 * @ClassName FocusPropertiesModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:12
 * @Version 1.0
 */
/**
 * Implement this interface create a modifier node that can be used to modify the focus properties
 * of the associated [FocusTargetNode].
 */
interface FocusPropertiesModifierNode : DelegatableNode {
    /**
     * A parent can modify the focus properties associated with the nearest
     * [FocusTargetNode] child node. If a [FocusTargetNode] has multiple parent
     * [FocusPropertiesModifierNode]s, properties set by a parent higher up in the hierarchy
     * overwrite properties set by those that are lower in the hierarchy.
     */
    fun applyFocusProperties(focusProperties: FocusProperties)
}

fun FocusPropertiesModifierNode.invalidateFocusProperties() {
    requireOwner().focusOwner.scheduleInvalidation(this)
}
