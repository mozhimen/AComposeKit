package com.mozhimen.composek.ui.focus

import androidx.compose.ui.focus.FocusEventModifierNode
import androidx.compose.ui.focus.FocusPropertiesModifierNode
import androidx.compose.ui.focus.FocusTargetNode
import androidx.compose.ui.focus.getFocusState
import androidx.compose.ui.focus.refreshFocusEventNodes
import androidx.compose.ui.node.Nodes
import androidx.compose.ui.node.visitSelfAndChildren

/**
 * @ClassName FocusInvalidationManager
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * The [FocusInvalidationManager] allows us to schedule focus related nodes for invalidation.
 * These nodes are invalidated after onApplyChanges. It does this by registering an
 * onApplyChangesListener when nodes are scheduled for invalidation.
 */
internal class FocusInvalidationManager(
    private val onRequestApplyChangesListener: (() -> Unit) -> Unit
) {
    private var focusTargetNodes = mutableSetOf<FocusTargetNode>()
    private var focusEventNodes = mutableSetOf<FocusEventModifierNode>()
    private var focusPropertiesNodes = mutableSetOf<FocusPropertiesModifierNode>()

    fun scheduleInvalidation(node: FocusTargetNode) {
        focusTargetNodes.scheduleInvalidation(node)
    }

    fun scheduleInvalidation(node: FocusEventModifierNode) {
        focusEventNodes.scheduleInvalidation(node)
    }

    fun scheduleInvalidation(node: FocusPropertiesModifierNode) {
        focusPropertiesNodes.scheduleInvalidation(node)
    }

    private fun <T> MutableSet<T>.scheduleInvalidation(node: T) {
        if (add(node)) {
            // If this is the first node scheduled for invalidation,
            // we set up a listener that runs after onApplyChanges.
            if (focusTargetNodes.size + focusEventNodes.size + focusPropertiesNodes.size == 1) {
                onRequestApplyChangesListener.invoke(invalidateNodes)
            }
        }
    }

    private val invalidateNodes: () -> Unit = {
        // Process all the invalidated FocusProperties nodes.
        focusPropertiesNodes.forEach {
            // We don't need to invalidate a focus properties node if it was scheduled for
            // invalidation earlier in the composition but was then removed.
            if (!it.node.isAttached) return@forEach

            it.visitSelfAndChildren(Nodes.FocusTarget) { focusTarget ->
                focusTargetNodes.add(focusTarget)
            }
        }
        focusPropertiesNodes.clear()

        // Process all the focus events nodes.
        val focusTargetsWithInvalidatedFocusEvents = mutableSetOf<FocusTargetNode>()
        focusEventNodes.forEach { focusEventNode ->
            // When focus nodes are removed, the corresponding focus events are scheduled for
            // invalidation. If the focus event was also removed, we don't need to invalidate it.
            // We call onFocusEvent with the default value, just to make it easier for the user,
            // so that they don't have to keep track of whether they caused a focused item to be
            // removed (Which would cause it to lose focus).
            if (!focusEventNode.node.isAttached) {
                focusEventNode.onFocusEvent(Inactive)
                return@forEach
            }

            var requiresUpdate = true
            var aggregatedNode = false
            var focusTarget: FocusTargetNode? = null
            focusEventNode.visitSelfAndChildren(Nodes.FocusTarget) {

                // If there are multiple focus targets associated with this focus event node,
                // we need to calculate the aggregated state.
                if (focusTarget != null) {
                    aggregatedNode = true
                }

                focusTarget = it

                // If the associated focus node is already scheduled for invalidation, it will
                // send an onFocusEvent if the invalidation causes a focus state change.
                // However this onFocusEvent was invalidated, so we have to ensure that we call
                // onFocusEvent even if the focus state didn't change.
                if (focusTargetNodes.contains(it)) {
                    requiresUpdate = false
                    focusTargetsWithInvalidatedFocusEvents.add(it)
                    return@visitSelfAndChildren
                }
            }

            if (requiresUpdate) {
                focusEventNode.onFocusEvent(
                    if (aggregatedNode) {
                        focusEventNode.getFocusState()
                    } else {
                        focusTarget?.focusState ?: Inactive
                    }
                )
            }
        }
        focusEventNodes.clear()

        // Process all the focus target nodes.
        focusTargetNodes.forEach {
            // We don't need to invalidate the focus target if it was scheduled for invalidation
            // earlier in the composition but was then removed.
            if (!it.isAttached) return@forEach

            val preInvalidationState = it.focusState
            it.invalidateFocus()
            if (preInvalidationState != it.focusState ||
                focusTargetsWithInvalidatedFocusEvents.contains(it)) {
                it.refreshFocusEventNodes()
            }
        }
        focusTargetNodes.clear()
        focusTargetsWithInvalidatedFocusEvents.clear()

        check(focusPropertiesNodes.isEmpty()) { "Unprocessed FocusProperties nodes" }
        check(focusEventNodes.isEmpty()) { "Unprocessed FocusEvent nodes" }
        check(focusTargetNodes.isEmpty()) { "Unprocessed FocusTarget nodes" }
    }
}
