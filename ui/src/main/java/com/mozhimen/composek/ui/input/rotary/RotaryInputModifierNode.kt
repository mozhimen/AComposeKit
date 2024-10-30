package com.mozhimen.composek.ui.input.rotary

import androidx.compose.ui.input.rotary.RotaryScrollEvent
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import com.mozhimen.composek.ui.node.DelegatableNode

/**
 * @ClassName RotaryInputModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:55
 * @Version 1.0
 */
/**
 * Implement this interface to create a [Modifier.Node] that can intercept rotary scroll events.
 *
 * The event is routed to the focused item. Before reaching the focused item,
 * [onPreRotaryScrollEvent]() is called for parents of the focused item. If the parents don't
 * consume the event, [onPreRotaryScrollEvent]() is called for the focused item. If the event is
 * still not consumed, [onRotaryScrollEvent]() is called on the focused item's parents.
 */
interface RotaryInputModifierNode : DelegatableNode {
    /**
     * This function is called when a [RotaryScrollEvent] is received by this node during the upward
     * pass. While implementing this callback, return true to stop propagation of this event. If you
     * return false, the key event will be sent to this [RotaryInputModifierNode]'s parent.
     */
    fun onRotaryScrollEvent(event: RotaryScrollEvent): Boolean

    /**
     * This function is called when a [RotaryScrollEvent] is received by this node during the
     * downward pass. It gives ancestors of a focused component the chance to intercept an event.
     * Return true to stop propagation of this event. If you return false, the event will be sent
     * to this [RotaryInputModifierNode]'s child. If none of the children consume the event,
     * it will be sent back up to the root using the [onRotaryScrollEvent] function.
     */
    fun onPreRotaryScrollEvent(event: RotaryScrollEvent): Boolean
}
