package com.mozhimen.composek.ui.node

import androidx.compose.ui.util.fastMap
import com.mozhimen.composek.ui.layout.IntrinsicMeasureScope
import com.mozhimen.composek.ui.layout.Measurable
import com.mozhimen.composek.ui.layout.MeasureScope
import com.mozhimen.composek.ui.node.LayoutNode.LayoutState


/**
 * @ClassName MeasureScopeWithLayoutNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal interface MeasureScopeWithLayoutNode : MeasureScope {
    val layoutNode: LayoutNode
}

internal fun getChildrenOfVirtualChildren(scope: IntrinsicMeasureScope): List<List<Measurable>> {
    val layoutNode = (scope as MeasureScopeWithLayoutNode).layoutNode
    val lookahead = layoutNode.isInLookaheadPass()
    return layoutNode.foldedChildren.fastMap {
        if (lookahead) it.childLookaheadMeasurables else it.childMeasurables
    }
}

private fun LayoutNode.isInLookaheadPass(): Boolean {
    return when (layoutState) {
        LayoutState.LookaheadMeasuring, LayoutState.LookaheadLayingOut -> true
        LayoutState.Measuring, LayoutState.LayingOut -> false
        LayoutState.Idle -> {
            // idle means intrinsics are being asked, we need to check the parent
            requireNotNull(parent) { "no parent for idle node" }.isInLookaheadPass()
        }
    }
}
