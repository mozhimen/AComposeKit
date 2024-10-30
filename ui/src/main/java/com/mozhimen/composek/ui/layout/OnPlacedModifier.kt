package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.node.LayoutAwareModifierNode
import com.mozhimen.composek.ui.node.ModifierNodeElement

/**
 * @ClassName OnPlacedModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 20:17
 * @Version 1.0
 */
/**
 * Invoke [onPlaced] after the parent [LayoutModifier] and parent layout has been placed and before
 * child [LayoutModifier] is placed. This allows child [LayoutModifier] to adjust its
 * own placement based on where the parent is.
 *
 * @sample androidx.compose.ui.samples.OnPlaced
 */
@Stable
fun Modifier.onPlaced(
    onPlaced: (LayoutCoordinates) -> Unit
) = this then OnPlacedElement(onPlaced)

private data class OnPlacedElement(
    val onPlaced: (LayoutCoordinates) -> Unit
) : ModifierNodeElement<OnPlacedNode>() {
    override fun create() = OnPlacedNode(callback = onPlaced)

    override fun update(node: OnPlacedNode) {
        node.callback = onPlaced
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onPlaced"
        properties["onPlaced"] = onPlaced
    }
}

private class OnPlacedNode(
    var callback: (LayoutCoordinates) -> Unit
) : LayoutAwareModifierNode, Modifier.Node() {

    override fun onPlaced(coordinates: LayoutCoordinates) {
        callback(coordinates)
    }
}

/**
 * A modifier whose [onPlaced] is called after the parent [LayoutModifier] and parent layout has
 * been placed and before child [LayoutModifier] is placed. This allows child
 * [LayoutModifier] to adjust its own placement based on where the parent is.
 *
 * @sample androidx.compose.ui.samples.OnPlaced
 */
//@JvmDefaultWithCompatibility
interface OnPlacedModifier : Modifier.Element {
    /**
     * [onPlaced] is called after parent [LayoutModifier] and parent layout gets placed and
     * before any child [LayoutModifier] is placed.
     *
     * [coordinates] provides [LayoutCoordinates] of the [OnPlacedModifier]. Placement in both
     * parent [LayoutModifier] and parent layout can be calculated using the [LayoutCoordinates].
     */
    fun onPlaced(coordinates: LayoutCoordinates)
}
