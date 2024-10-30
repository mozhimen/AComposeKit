package com.mozhimen.composek.ui.focus

import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.node.ModifierNodeElement

/**
 * @ClassName FocusRequesterModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 20:28
 * @Version 1.0
 */
/**
 * A [modifier][Modifier.Element] that is used to pass in a [FocusRequester] that can be used to
 * request focus state changes.
 *
 * @sample androidx.compose.ui.samples.RequestFocusSample
 *
 * @see FocusRequester
 * @see Modifier.focusRequester
 */
@Deprecated("Use FocusRequesterModifierNode instead")
//@JvmDefaultWithCompatibility
interface FocusRequesterModifier : Modifier.Element {
    /**
     * An instance of [FocusRequester], that can be used to request focus state changes.
     *
     * @sample androidx.compose.ui.samples.RequestFocusSample
     */
    val focusRequester: FocusRequester
}

/**
 * Add this modifier to a component to request changes to focus.
 *
 * @sample androidx.compose.ui.samples.RequestFocusSample
 */
fun Modifier.focusRequester(focusRequester: FocusRequester): Modifier =
    this then FocusRequesterElement(focusRequester)

private data class FocusRequesterElement(
    val focusRequester: FocusRequester
) : ModifierNodeElement<FocusRequesterNode>() {
    override fun create() = FocusRequesterNode(focusRequester)

    override fun update(node: FocusRequesterNode) {
        node.focusRequester.focusRequesterNodes -= node
        node.focusRequester = focusRequester
        node.focusRequester.focusRequesterNodes += node
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "focusRequester"
        properties["focusRequester"] = focusRequester
    }
}

private class FocusRequesterNode(
    var focusRequester: FocusRequester
) : FocusRequesterModifierNode, Modifier.Node() {
    override fun onAttach() {
        super.onAttach()
        focusRequester.focusRequesterNodes += this
    }

    override fun onDetach() {
        focusRequester.focusRequesterNodes -= this
        super.onDetach()
    }
}
