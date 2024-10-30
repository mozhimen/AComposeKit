package com.mozhimen.composek.ui.input.key

import androidx.compose.ui.ExperimentalComposeUiApi
import com.mozhimen.composek.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import com.mozhimen.composek.ui.node.DelegatableNode

/**
 * @ClassName SoftKeyboardInterceptionModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Implement this interface to create a [Modifier.Node] that can intercept hardware Key events
 * before they are sent to the software keyboard. This can be used to intercept key input from a
 * DPad, or physical keyboard connected to the device and is not applicable to input that is sent
 * to the soft keyboard via spell check or autocomplete.
 *
 * The event is routed to the focused item. Before reaching the focused item,
 * [onPreInterceptKeyBeforeSoftKeyboard] is called for parents of the focused item.
 * If the parents don't consume the event, [onPreSoftwareKeyboardKeyEvent]() is
 * called for the focused item. If the event is still not consumed,
 * [onInterceptKeyBeforeSoftKeyboard] is called on the focused item's parents.
 */
@ExperimentalComposeUiApi
interface SoftKeyboardInterceptionModifierNode : DelegatableNode {
    /**
     * This function is called when a [KeyEvent] is received by this node during the upward
     * pass. While implementing this callback, return true to stop propagation of this event.
     * If you return false, the key event will be sent to this
     * [SoftKeyboardInterceptionModifierNode]'s parent.
     */
    fun onInterceptKeyBeforeSoftKeyboard(event: KeyEvent): Boolean

    /**
     * This function is called when a [KeyEvent] is received by this node during the
     * downward pass. It gives ancestors of a focused component the chance to intercept an event.
     * Return true to stop propagation of this event. If you return false, the event will be sent
     * to this [SoftKeyboardInterceptionModifierNode]'s child. If none of the children consume
     * the event, it will be sent back up to the root using the [onPreInterceptKeyBeforeSoftKeyboard]
     * function.
     */
    fun onPreInterceptKeyBeforeSoftKeyboard(event: KeyEvent): Boolean
}
