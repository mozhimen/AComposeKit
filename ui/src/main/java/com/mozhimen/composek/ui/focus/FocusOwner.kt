package com.mozhimen.composek.ui.focus

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.LayoutDirection
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.input.rotary.RotaryScrollEvent
import com.mozhimen.composek.ui.node.FocusPropertiesModifierNode

/**
 * @ClassName FocusOwner
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * The focus owner provides some internal APIs that are not exposed by focus manager.
 */
internal interface FocusOwner : FocusManager {

    /**
     * A [Modifier] that can be added to the [Owners][androidx.compose.ui.node.Owner] modifier
     * list that contains the modifiers required by the focus system. (Eg, a root focus modifier).
     */
    val modifier: Modifier

    /**
     * The owner sets the layoutDirection that is then used during focus search.
     */
    var layoutDirection: LayoutDirection

    /**
     * This manager provides a way to ensure that only one focus transaction is running at a time.
     * We use this to prevent re-entrant focus operations. Starting a new transaction automatically
     * cancels the previous transaction and reverts any focus state changes made during that
     * transaction.
     */
    val focusTransactionManager: FocusTransactionManager

    /**
     * The [Owner][androidx.compose.ui.node.Owner] calls this function when it gains focus. This
     * informs the [focus manager][FocusOwnerImpl] that the
     * [Owner][androidx.compose.ui.node.Owner] gained focus, and that it should propagate this
     * focus to one of the focus modifiers in the component hierarchy.
     */
    fun takeFocus()

    /**
     * The [Owner][androidx.compose.ui.node.Owner] calls this function when it loses focus. This
     * informs the [focus manager][FocusOwnerImpl] that the
     * [Owner][androidx.compose.ui.node.Owner] lost focus, and that it should clear focus from
     * all the focus modifiers in the component hierarchy.
     */
    fun releaseFocus()

    /**
     * Call this function to set the focus to the root focus modifier.
     *
     * @param force: Whether we should forcefully clear focus regardless of whether we have
     * any components that have captured focus.
     *
     * @param refreshFocusEvents: Whether we should send an event up the hierarchy to update
     * the associated onFocusEvent nodes.
     *
     * This could be used to clear focus when a user clicks on empty space outside a focusable
     * component.
     */
    fun clearFocus(force: Boolean, refreshFocusEvents: Boolean)

    /**
     * Searches for the currently focused item, and returns its coordinates as a rect.
     */
    fun getFocusRect(): Rect?

    /**
     * Dispatches a key event through the compose hierarchy.
     */
    fun dispatchKeyEvent(keyEvent: KeyEvent): Boolean

    /**
     * Dispatches an intercepted soft keyboard key event through the compose hierarchy.
     */
    fun dispatchInterceptedSoftKeyboardEvent(keyEvent: KeyEvent): Boolean

    /**
     * Dispatches a rotary scroll event through the compose hierarchy.
     */
    fun dispatchRotaryEvent(event: RotaryScrollEvent): Boolean

    /**
     * Schedule a FocusTarget node to be invalidated after onApplyChanges.
     */
    fun scheduleInvalidation(node: FocusTargetNode)

    /**
     * Schedule a FocusEvent node to be invalidated after onApplyChanges.
     */
    fun scheduleInvalidation(node: FocusEventModifierNode)

    /**
     * Schedule a FocusProperties node to be invalidated after onApplyChanges.
     */
    fun scheduleInvalidation(node: FocusPropertiesModifierNode)
}
