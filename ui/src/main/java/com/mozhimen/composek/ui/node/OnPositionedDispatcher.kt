package com.mozhimen.composek.ui.node

import androidx.compose.runtime.collection.mutableVectorOf

/**
 * @ClassName OnPositionedDispatcher
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 2:10
 * @Version 1.0
 */
/**
 * Tracks the nodes being positioned and dispatches OnPositioned callbacks when we finished
 * the measure/layout pass.
 */
internal class OnPositionedDispatcher {
    private val layoutNodes = mutableVectorOf<LayoutNode>()
    private var cachedNodes: Array<LayoutNode?>? = null

    fun isNotEmpty() = layoutNodes.isNotEmpty()

    fun onNodePositioned(node: LayoutNode) {
        layoutNodes += node
        node.needsOnPositionedDispatch = true
    }

    fun onRootNodePositioned(rootNode: LayoutNode) {
        layoutNodes.clear()
        layoutNodes += rootNode
        rootNode.needsOnPositionedDispatch = true
    }

    fun dispatch() {
        // sort layoutNodes so that the root is at the end and leaves are at the front
        layoutNodes.sortWith(DepthComparator)
        val cache: Array<LayoutNode?>
        val size = layoutNodes.size
        val cachedNodes = this.cachedNodes
        if (cachedNodes == null || cachedNodes.size < size) {
            cache = arrayOfNulls(maxOf(MinArraySize, layoutNodes.size))
        } else {
            cache = cachedNodes
        }
        this.cachedNodes = null

        // copy to cache to prevent reentrancy being a problem
        for (i in 0 until size) {
            cache[i] = layoutNodes[i]
        }
        layoutNodes.clear()
        for (i in size - 1 downTo 0) {
            val layoutNode = cache[i]!!
            if (layoutNode.needsOnPositionedDispatch) {
                dispatchHierarchy(layoutNode)
            }
        }
        this.cachedNodes = cache
    }

    private fun dispatchHierarchy(layoutNode: LayoutNode) {
        // TODO(lmr): investigate a non-recursive version of this that leverages
        //  node traversal
        layoutNode.dispatchOnPositionedCallbacks()
        layoutNode.needsOnPositionedDispatch = false

        layoutNode.forEachChild { child ->
            dispatchHierarchy(child)
        }
    }

    internal companion object {
        private const val MinArraySize = 16

        private object DepthComparator : Comparator<LayoutNode> {
            override fun compare(a: LayoutNode, b: LayoutNode): Int {
                val depthDiff = b.depth.compareTo(a.depth)
                if (depthDiff != 0) {
                    return depthDiff
                }
                return a.hashCode().compareTo(b.hashCode())
            }
        }
    }
}
