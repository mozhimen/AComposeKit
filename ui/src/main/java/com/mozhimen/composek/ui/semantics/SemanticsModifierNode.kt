package com.mozhimen.composek.ui.semantics

import androidx.compose.ui.geometry.Rect
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.layout.boundsInRoot
import com.mozhimen.composek.ui.node.DelegatableNode
import com.mozhimen.composek.ui.node.Nodes
import com.mozhimen.composek.ui.node.requireCoordinator
import com.mozhimen.composek.ui.node.requireLayoutNode

/**
 * @ClassName SemanticsModifierNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:13
 * @Version 1.0
 */
/**
 * A [Modifier.Node] that adds semantics key/value for use in testing,
 * accessibility, and similar use cases.
 *
 * This is the [androidx.compose.ui.Modifier.Node] equivalent of
 * [androidx.compose.ui.semantics.SemanticsModifier]
 */
interface SemanticsModifierNode : DelegatableNode {
    /**
     * Clears the semantics of all the descendant nodes and sets new semantics.
     *
     * In the merged semantics tree, this clears the semantic information provided
     * by the node's descendants (but not those of the layout node itself, if any)
     * In the unmerged tree, the semantics node is marked with
     * "[SemanticsConfiguration.isClearingSemantics]", but nothing is actually cleared.
     *
     * Compose's default semantics provide baseline usability for screen-readers, but this can be
     * used to provide a more polished screen-reader experience: for example, clearing the
     * semantics of a group of tiny buttons, and setting equivalent actions on the card
     * containing them.
     */
    @get:Suppress("GetterSetterNames")
    val shouldClearDescendantSemantics: Boolean
        get() = false

    /**
     * Whether the semantic information provided by this node and
     * its descendants should be treated as one logical entity.
     * Most commonly set on screen-reader-focusable items such as buttons or form fields.
     * In the merged semantics tree, all descendant nodes (except those themselves marked
     * [shouldMergeDescendantSemantics]) will disappear from the tree, and their properties
     * will get merged into the parent's configuration (using a merging algorithm that varies based
     * on the type of property -- for example, text properties will get concatenated, separated
     * by commas). In the unmerged semantics tree, the node is simply marked with
     * [SemanticsConfiguration.isMergingSemanticsOfDescendants].
     */
    @get:Suppress("GetterSetterNames")
    val shouldMergeDescendantSemantics: Boolean
        get() = false

    /**
     * Add semantics key/value pairs to the layout node, for use in testing, accessibility, etc.
     *
     * The [SemanticsPropertyReceiver] provides "key = value"-style setters for any
     * [SemanticsPropertyKey]. Additionally, chaining multiple semantics modifiers is
     * also a supported style.
     *
     * The resulting semantics produce two [SemanticsNode] trees:
     *
     * The "unmerged tree" rooted at [SemanticsOwner.unmergedRootSemanticsNode] has one
     * [SemanticsNode] per layout node which has any [SemanticsModifierNode] on it.  This
     * [SemanticsNode] contains all the properties set in all the [SemanticsModifierNode]s on
     * that node.
     *
     * The "merged tree" rooted at [SemanticsOwner.rootSemanticsNode] has equal-or-fewer nodes: it
     * simplifies the structure based on [shouldMergeDescendantSemantics] and
     * [shouldClearDescendantSemantics].  For most purposes (especially accessibility, or the
     * testing of accessibility), the merged semantics tree should be used.
     */
    fun SemanticsPropertyReceiver.applySemantics()
}

fun SemanticsModifierNode.invalidateSemantics() = requireLayoutNode().invalidateSemantics()

internal val SemanticsConfiguration.useMinimumTouchTarget: Boolean
    get() = getOrNull(SemanticsActions.OnClick) != null

internal fun Modifier.Node.touchBoundsInRoot(useMinimumTouchTarget: Boolean): Rect {
    if (!node.isAttached) {
        return Rect.Zero
    }
    if (!useMinimumTouchTarget) {
        return requireCoordinator(Nodes.Semantics).boundsInRoot()
    }

    return requireCoordinator(Nodes.Semantics).touchBoundsInRoot()
}
