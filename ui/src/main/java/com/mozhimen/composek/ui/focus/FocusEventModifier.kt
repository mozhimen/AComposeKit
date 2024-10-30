package com.mozhimen.composek.ui.focus

import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.node.ModifierNodeElement

/**
 * @ClassName FocusEventModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 20:33
 * @Version 1.0
 */
/**
 * A [modifier][Modifier.Element] that can be used to observe focus state events.
 */
@Deprecated("Use FocusEventModifierNode instead")
//@JvmDefaultWithCompatibility
interface FocusEventModifier : Modifier.Element {
    /**
     * A callback that is called whenever the focus system raises events.
     */
    fun onFocusEvent(focusState: FocusState)
}

/**
 * Add this modifier to a component to observe focus state events.
 */
fun Modifier.onFocusEvent(
    onFocusEvent: (FocusState) -> Unit
): Modifier = this then FocusEventElement(onFocusEvent)

private data class FocusEventElement(
    val onFocusEvent: (FocusState) -> Unit
) : ModifierNodeElement<FocusEventNode>() {
    override fun create() = FocusEventNode(onFocusEvent)

    override fun update(node: FocusEventNode) {
        node.onFocusEvent = onFocusEvent
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onFocusEvent"
        properties["onFocusEvent"] = onFocusEvent
    }
}

private class FocusEventNode(
    var onFocusEvent: (FocusState) -> Unit
) : FocusEventModifierNode, Modifier.Node() {

    override fun onFocusEvent(focusState: FocusState) {
        this.onFocusEvent.invoke(focusState)
    }
}