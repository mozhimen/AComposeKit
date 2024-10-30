package com.mozhimen.composek.ui.node

import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEach

/**
 * @ClassName LayoutTreeConsistencyChecker
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 2:11
 * @Version 1.0
 */
/**
 * There are some contracts between the tree of LayoutNodes and the state of AndroidComposeView
 * which is hard to enforce but important to maintain. This method is intended to do the
 * work only during our tests and will iterate through the tree to validate the states consistency.
 */
internal class LayoutTreeConsistencyChecker(
    private val root: LayoutNode,
    private val relayoutNodes: DepthSortedSetsForDifferentPasses,
    private val postponedMeasureRequests: List<MeasureAndLayoutDelegate.PostponedRequest>
) {
    fun assertConsistent() {
        val inconsistencyFound = !isTreeConsistent(root)
        if (inconsistencyFound) {
            println(logTree())
            throw IllegalStateException("Inconsistency found!")
        }
    }

    private fun isTreeConsistent(node: LayoutNode): Boolean {
        if (!node.consistentLayoutState()) {
            return false
        }
        node.children.fastForEach {
            if (!isTreeConsistent(it)) {
                return@isTreeConsistent false
            }
        }
        return true
    }

    private fun LayoutNode.consistentLayoutState(): Boolean {
        val parent = this.parent
        val parentLayoutState = parent?.layoutState
        if (isPlaced ||
            placeOrder != LayoutNode.NotPlacedPlaceOrder && parent?.isPlaced == true
        ) {
            if (measurePending && postponedMeasureRequests
                    .fastFirstOrNull { it.node == this && !it.isLookahead } != null
            ) {
                // this node is waiting to be measured by parent or if this will not happen
                // `onRequestMeasure` will be called for all items in `postponedMeasureRequests`
                return true
            }
            // remeasure or relayout is scheduled
            if (measurePending) {
                return relayoutNodes.contains(this) ||
                        layoutState == LayoutNode.LayoutState.LookaheadMeasuring ||
                        parent?.measurePending == true ||
                        parent?.lookaheadMeasurePending == true ||
                        parentLayoutState == LayoutNode.LayoutState.Measuring
            }
            if (layoutPending) {
                return relayoutNodes.contains(this) ||
                        parent == null ||
                        parent.measurePending ||
                        parent.layoutPending ||
                        parentLayoutState == LayoutNode.LayoutState.Measuring ||
                        parentLayoutState == LayoutNode.LayoutState.LayingOut
            }
        }
        if (isPlacedInLookahead == true) {
            if (lookaheadMeasurePending && postponedMeasureRequests
                    .fastFirstOrNull { it.node == this && it.isLookahead } != null
            ) {
                // this node is waiting to be lookahead measured by parent or if this will not
                // happen `onRequestLookaheadMeasure` will be called for all items in
                // `postponedLookaheadMeasureRequests`
                return true
            }
            if (lookaheadMeasurePending) {
                return relayoutNodes.contains(this, true) ||
                        parent?.lookaheadMeasurePending == true ||
                        parentLayoutState == LayoutNode.LayoutState.LookaheadMeasuring ||
                        (parent?.measurePending == true && lookaheadRoot == this)
            }
            if (lookaheadLayoutPending) {
                return relayoutNodes.contains(this, true) ||
                        parent == null ||
                        parent.lookaheadMeasurePending ||
                        parent.lookaheadLayoutPending ||
                        parentLayoutState == LayoutNode.LayoutState.LookaheadMeasuring ||
                        parentLayoutState == LayoutNode.LayoutState.LookaheadLayingOut ||
                        (parent.layoutPending && lookaheadRoot == this)
            }
        }
        return true
    }

    private fun nodeToString(node: LayoutNode): String {
        return with(StringBuilder()) {
            append(node)
            append("[${node.layoutState}]")
            if (!node.isPlaced) append("[!isPlaced]")
            append("[measuredByParent=${node.measuredByParent}]")
            if (!node.consistentLayoutState()) {
                append("[INCONSISTENT]")
            }
            toString()
        }
    }

    /** Prints the nodes tree into the logs. */
    private fun logTree(): String {
        val stringBuilder = StringBuilder()
        fun printSubTree(node: LayoutNode, depth: Int) {
            var childrenDepth = depth
            val nodeRepresentation = nodeToString(node)
            if (nodeRepresentation.isNotEmpty()) {
                for (i in 0 until depth) {
                    stringBuilder.append("..")
                }
                stringBuilder.appendLine(nodeRepresentation)
                childrenDepth += 1
            }
            node.children.fastForEach { printSubTree(it, childrenDepth) }
        }
        stringBuilder.appendLine("Tree state:")
        printSubTree(root, 0)
        return stringBuilder.toString()
    }
}
