package com.mozhimen.composek.ui.semantics

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.util.fastForEach
import com.mozhimen.composek.ui.node.LayoutNode
import com.mozhimen.composek.ui.node.Nodes

/**
 * @ClassName SemanticsOwner
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:25
 * @Version 1.0
 */
/**
 * Owns [SemanticsNode] objects and notifies listeners of changes to the
 * semantics tree
 */
@OptIn(ExperimentalComposeUiApi::class)
class SemanticsOwner internal constructor(private val rootNode: LayoutNode) {
    /**
     * The root node of the semantics tree.  Does not contain any unmerged data.
     * May contain merged data.
     */
    val rootSemanticsNode: SemanticsNode
        get() {
            return SemanticsNode(rootNode, mergingEnabled = true)
        }

    val unmergedRootSemanticsNode: SemanticsNode
        get() {
            return SemanticsNode(
                outerSemanticsNode = rootNode.nodes.head(Nodes.Semantics)!!.node,
                layoutNode = rootNode,
                mergingEnabled = false,
                // Forcing an empty SemanticsConfiguration here since the root node will always
                // have an empty config, but if we don't pass this in explicitly here it will try
                // to call `rootNode.collapsedSemantics` which will fail because the LayoutNode
                // is not yet attached when this getter is first called.
                unmergedConfig = SemanticsConfiguration()
            )
        }
}

/**
 * Finds all [SemanticsNode]s in the tree owned by this [SemanticsOwner]. Return the results in a
 * list.
 *
 * @param mergingEnabled set to true if you want the data to be merged.
 * @param skipDeactivatedNodes set to false if you want to collect the nodes which are deactivated.
 * For example, the children of [androidx.compose.ui.layout.SubcomposeLayout] which are retained
 * to be reused in future are considered deactivated.
 */
fun SemanticsOwner.getAllSemanticsNodes(
    mergingEnabled: Boolean,
    skipDeactivatedNodes: Boolean = true
): List<SemanticsNode> {
    return getAllSemanticsNodesToMap(
        useUnmergedTree = !mergingEnabled,
        skipDeactivatedNodes = skipDeactivatedNodes
    ).values.toList()
}

@Deprecated(message = "Use a new overload instead", level = DeprecationLevel.HIDDEN)
fun SemanticsOwner.getAllSemanticsNodes(mergingEnabled: Boolean) =
    getAllSemanticsNodes(mergingEnabled, true)

/**
 * Finds all [SemanticsNode]s in the tree owned by this [SemanticsOwner]. Return the results in a
 * map.
 */
internal fun SemanticsOwner.getAllSemanticsNodesToMap(
    useUnmergedTree: Boolean = false,
    skipDeactivatedNodes: Boolean = true
): Map<Int, SemanticsNode> {
    val nodes = mutableMapOf<Int, SemanticsNode>()

    fun findAllSemanticNodesRecursive(currentNode: SemanticsNode) {
        if (!skipDeactivatedNodes || !currentNode.layoutInfo.isDeactivated) {
            nodes[currentNode.id] = currentNode
            currentNode.children.fastForEach { child ->
                findAllSemanticNodesRecursive(child)
            }
        }
    }

    val root = if (useUnmergedTree) unmergedRootSemanticsNode else rootSemanticsNode
    findAllSemanticNodesRecursive(root)
    return nodes
}
