package com.mozhimen.composek.ui.focus

import com.mozhimen.composek.ui.node.requireOwner
import com.mozhimen.composek.ui.focus.FocusStateImpl.Active
import com.mozhimen.composek.ui.focus.FocusStateImpl.ActiveParent
import com.mozhimen.composek.ui.focus.FocusStateImpl.Captured
import com.mozhimen.composek.ui.focus.FocusStateImpl.Inactive
import com.mozhimen.composek.ui.node.DelegatableNode
import com.mozhimen.composek.ui.node.Nodes
import com.mozhimen.composek.ui.node.visitSelfAndAncestors
import com.mozhimen.composek.ui.node.visitSelfAndChildren

/**
 * @ClassName FocusEventModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Implement this interface create a modifier node that can be used to observe focus state changes
 * to a [FocusTargetNode] down the hierarchy.
 */
interface FocusEventModifierNode : DelegatableNode {

    /**
     * A parent FocusEventNode is notified of [FocusState] changes to the [FocusTargetNode]
     * associated with this [FocusEventModifierNode].
     */
    fun onFocusEvent(focusState: FocusState)
}

internal fun FocusEventModifierNode.invalidateFocusEvent() {
    requireOwner().focusOwner.scheduleInvalidation(this)
}

internal fun FocusEventModifierNode.getFocusState(): FocusState {
    visitSelfAndChildren(Nodes.FocusTarget) {
        when (val focusState = it.focusState) {
            // If we find a focused child, we use that child's state as the aggregated state.
            Active, ActiveParent, Captured -> return focusState
            // We use the Inactive state only if we don't have a focused child.
            // ie. we ignore this child if another child provides aggregated state.
            Inactive -> return@visitSelfAndChildren
        }
    }
    return FocusStateImpl.Inactive
}

/**
 * Sends a "Focus Event" up the hierarchy that asks all [FocusEventModifierNode]s to recompute their
 * observed focus state.
 *
 * Make this public after [FocusTargetNode] is made public.
 */
internal fun FocusTargetNode.refreshFocusEventNodes() {
    visitSelfAndAncestors(Nodes.FocusEvent, untilType = Nodes.FocusTarget) {
        // TODO(251833873): Consider caching it.getFocusState().
        it.onFocusEvent(it.getFocusState())
    }
}
