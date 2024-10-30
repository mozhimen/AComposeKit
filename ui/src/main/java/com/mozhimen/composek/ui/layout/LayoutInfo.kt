package com.mozhimen.composek.ui.layout

import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.mozhimen.composek.ui.Modifier

/**
 * @ClassName LayoutInfo
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:33
 * @Version 1.0
 */
/**
 * The public information about the layouts used internally as nodes in the Compose UI hierarchy.
 */
interface LayoutInfo {

    /**
     * This returns a new List of [Modifier]s and the coordinates and any extra information
     * that may be useful. This is used for tooling to retrieve layout modifier and layer
     * information.
     */
    fun getModifierInfo(): List<ModifierInfo>

    /**
     * The measured width of this layout and all of its modifiers.
     */
    val width: Int

    /**
     * The measured height of this layout and all of its modifiers.
     */
    val height: Int

    /**
     * Coordinates of just the contents of the layout, after being affected by all modifiers.
     */
    val coordinates: LayoutCoordinates

    /**
     * Whether or not this layout and all of its parents have been placed in the hierarchy.
     */
    val isPlaced: Boolean

    /**
     * Parent of this layout.
     */
    val parentInfo: LayoutInfo?

    /**
     * The density in use for this layout.
     */
    val density: Density

    /**
     * The layout direction in use for this layout.
     */
    val layoutDirection: LayoutDirection

    /**
     * The [ViewConfiguration] in use for this layout.
     */
    val viewConfiguration: ViewConfiguration

    /**
     * Returns true if this layout is currently a part of the layout tree.
     */
    val isAttached: Boolean

    /**
     * Unique and stable id representing this node to the semantics system.
     */
    val semanticsId: Int

    /**
     * True if the node is deactivated. For example, the children of
     * [androidx.compose.ui.layout.SubcomposeLayout] which are retained to be reused in future
     * are considered deactivated.
     */
    val isDeactivated: Boolean get() = false
}

/**
 * Used by tooling to examine the modifiers on a [LayoutInfo].
 */
class ModifierInfo(
    val modifier: Modifier,
    val coordinates: LayoutCoordinates,
    val extra: Any? = null
) {
    override fun toString(): String {
        return "ModifierInfo($modifier, $coordinates, $extra)"
    }
}
