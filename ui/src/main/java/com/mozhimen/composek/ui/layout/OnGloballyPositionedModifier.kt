package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.node.GlobalPositionAwareModifierNode
import com.mozhimen.composek.ui.node.ModifierNodeElement

/**
 * @ClassName OnGloballyPositionedModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 20:03
 * @Version 1.0
 */
/**
 * Invoke [onGloballyPositioned] with the [LayoutCoordinates] of the element when the
 * global position of the content may have changed.
 * Note that it will be called **after** a composition when the coordinates are finalized.
 *
 * This callback will be invoked at least once when the [LayoutCoordinates] are available, and every
 * time the element's position changes within the window. However, it is not guaranteed to be
 * invoked every time the position _relative to the screen_ of the modified element changes. For
 * example, the system may move the contents inside a window around without firing a callback.
 * If you are using the [LayoutCoordinates] to calculate position on the screen, and not just inside
 * the window, you may not receive a callback.
 *
 * Usage example:
 * @sample androidx.compose.ui.samples.OnGloballyPositioned
 */
@Stable
fun Modifier.onGloballyPositioned(
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) = this then OnGloballyPositionedElement(onGloballyPositioned)

private class OnGloballyPositionedElement(
    val onGloballyPositioned: (LayoutCoordinates) -> Unit
) :
    ModifierNodeElement<OnGloballyPositionedNode>() {
    override fun create(): OnGloballyPositionedNode {
        return OnGloballyPositionedNode(onGloballyPositioned)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OnGloballyPositionedElement) return false
        return onGloballyPositioned == other.onGloballyPositioned
    }

    override fun hashCode(): Int {
        return onGloballyPositioned.hashCode()
    }

    override fun update(node: OnGloballyPositionedNode) {
        node.callback = onGloballyPositioned
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onGloballyPositioned"
        properties["onGloballyPositioned"] = onGloballyPositioned
    }
}

private class OnGloballyPositionedNode(
    var callback: (LayoutCoordinates) -> Unit
) : Modifier.Node(), GlobalPositionAwareModifierNode {
    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        callback(coordinates)
    }
}

/**
 * A modifier whose [onGloballyPositioned] is called with the final LayoutCoordinates of the
 * Layout when the global position of the content may have changed.
 * Note that it will be called after a composition when the coordinates are finalized.
 *
 * Usage example:
 * @sample androidx.compose.ui.samples.OnGloballyPositioned
 */
//@JvmDefaultWithCompatibility
interface OnGloballyPositionedModifier : Modifier.Element {
    /**
     * Called with the final LayoutCoordinates of the Layout after measuring.
     * Note that it will be called after a composition when the coordinates are finalized.
     * The position in the modifier chain makes no difference in either
     * the [LayoutCoordinates] argument or when the [onGloballyPositioned] is called.
     */
    fun onGloballyPositioned(coordinates: LayoutCoordinates)
}