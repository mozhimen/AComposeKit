package com.mozhimen.composek.ui.focus

import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.BeyondBoundsScope
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.Above
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.After
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.Before
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.Below
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.Left
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout.LayoutDirection.Companion.Right
import com.mozhimen.composek.ui.node.Nodes
import com.mozhimen.composek.ui.node.nearestAncestor

/**
 * @ClassName BeyondBoundsLayout
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 22:31
 * @Version 1.0
 */
internal fun <T> FocusTargetNode.searchBeyondBounds(
    direction: FocusDirection,
    block: BeyondBoundsScope.() -> T?
): T? {

    // We only want the focus target of the LazyList to perform a beyond bounds search, and want to
    // prevent intermediate focus targets (within a LazyList's item) from triggering a beyond-bounds
    // layout. LazyLists add their own beyondBoundsLayoutParent, so if a focus target has the same
    // beyondBoundsLayoutParent as its parent, that focusTarget is not a lazylist, and the beyond
    // bounds search needs to be ignored.
    nearestAncestor(Nodes.FocusTarget)?.let {
        if (it.beyondBoundsLayoutParent == beyondBoundsLayoutParent) {
            return null
        }
    }

    return beyondBoundsLayoutParent?.layout(
        direction = when (direction) {
            FocusDirection.Up -> Above
            FocusDirection.Down -> Below
            FocusDirection.Left -> Left
            FocusDirection.Right -> Right
            FocusDirection.Next -> After
            FocusDirection.Previous -> Before
            else -> error("Unsupported direction for beyond bounds layout")
        },
        block = block
    )
}
