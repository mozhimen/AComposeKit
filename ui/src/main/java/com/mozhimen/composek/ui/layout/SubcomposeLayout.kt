package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNodeLifecycleCallback
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.ReusableComposition
import androidx.compose.runtime.ReusableContentHost
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeLayoutState.PrecomposedSlotHandle
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastForEach
import com.mozhimen.composek.ui.node.LayoutNode
import com.mozhimen.composek.ui.node.LayoutNode.LayoutState
import com.mozhimen.composek.ui.platform.createSubcomposition

/**
 * @ClassName SubcomposeLayout
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * The inner state containing all the information about active slots and their compositions.
 * It is stored inside LayoutNode object as in fact we need to keep 1-1 mapping between this state
 * and the node: when we compose a slot we first create a virtual LayoutNode child to this node
 * and then save the extra information inside this state.
 * Keeping this state inside LayoutNode also helps us to retain the pool of reusable slots even
 * when a new SubcomposeLayoutState is applied to SubcomposeLayout and even when the
 * SubcomposeLayout's LayoutNode is reused via the ReusableComposeNode mechanism.
 */
internal class LayoutNodeSubcompositionsState(
    private val root: LayoutNode,
    slotReusePolicy: SubcomposeSlotReusePolicy
) : ComposeNodeLifecycleCallback {
    var compositionContext: CompositionContext? = null

    var slotReusePolicy: SubcomposeSlotReusePolicy = slotReusePolicy
        set(value) {
            if (field !== value) {
                field = value
                // the new policy will be applied after measure
                markActiveNodesAsReused(deactivate = false)
                root.requestRemeasure()
            }
        }

    private var currentIndex = 0
    private var currentPostLookaheadIndex = 0
    private val nodeToNodeState = hashMapOf<LayoutNode, NodeState>()

    // this map contains active slotIds (without precomposed or reusable nodes)
    private val slotIdToNode = hashMapOf<Any?, LayoutNode>()
    private val scope = Scope()
    private val postLookaheadMeasureScope = PostLookaheadMeasureScopeImpl()

    private val precomposeMap = hashMapOf<Any?, LayoutNode>()
    private val reusableSlotIdsSet = SubcomposeSlotReusePolicy.SlotIdsSet()
    // SlotHandles precomposed in the post-lookahead pass.
    private val postLookaheadPrecomposeSlotHandleMap = mutableMapOf<Any?, PrecomposedSlotHandle>()
    // Slot ids _composed_ in post-lookahead. The valid slot ids are stored between 0 and
    // currentPostLookaheadIndex - 1, beyond index currentPostLookaheadIndex are obsolete ids.
    private val postLookaheadComposedSlotIds = mutableVectorOf<Any?>()

    /**
     * `root.foldedChildren` list consist of:
     * 1) all the active children (used during the last measure pass)
     * 2) `reusableCount` nodes in the middle of the list which were active and stopped being
     * used. now we keep them (up to `maxCountOfSlotsToReuse`) in order to reuse next time we
     * will need to compose a new item
     * 4) `precomposedCount` nodes in the end of the list which were precomposed and
     * are waiting to be used during the next measure passes.
     */
    private var reusableCount = 0
    private var precomposedCount = 0

    override fun onReuse() {
        markActiveNodesAsReused(deactivate = false)
    }

    override fun onDeactivate() {
        markActiveNodesAsReused(deactivate = true)
    }

    override fun onRelease() {
        disposeCurrentNodes()
    }

    fun subcompose(slotId: Any?, content: @Composable () -> Unit): List<Measurable> {
        makeSureStateIsConsistent()
        val layoutState = root.layoutState
        check(
            layoutState == LayoutState.Measuring || layoutState == LayoutState.LayingOut ||
                    layoutState == LayoutState.LookaheadMeasuring ||
                    layoutState == LayoutState.LookaheadLayingOut
        ) {
            "subcompose can only be used inside the measure or layout blocks"
        }

        val node = slotIdToNode.getOrPut(slotId) {
            val precomposed = precomposeMap.remove(slotId)
            if (precomposed != null) {
                @Suppress("ExceptionMessage")
                check(precomposedCount > 0)
                precomposedCount--
                precomposed
            } else {
                takeNodeFromReusables(slotId)
                    ?: createNodeAt(currentIndex)
            }
        }

        if (root.foldedChildren.getOrNull(currentIndex) !== node) {
            // the node has a new index in the list
            val itemIndex = root.foldedChildren.indexOf(node)
            require(itemIndex >= currentIndex) {
                "Key \"$slotId\" was already used. If you are using LazyColumn/Row please make " +
                        "sure you provide a unique key for each item."
            }
            if (currentIndex != itemIndex) {
                move(itemIndex, currentIndex)
            }
        }
        currentIndex++

        subcompose(node, slotId, content)

        return if (layoutState == LayoutState.Measuring || layoutState == LayoutState.LayingOut) {
            node.childMeasurables
        } else {
            node.childLookaheadMeasurables
        }
    }

    private fun subcompose(node: LayoutNode, slotId: Any?, content: @Composable () -> Unit) {
        val nodeState = nodeToNodeState.getOrPut(node) {
            NodeState(slotId, {})
        }
        val hasPendingChanges = nodeState.composition?.hasInvalidations ?: true
        if (nodeState.content !== content || hasPendingChanges || nodeState.forceRecompose) {
            nodeState.content = content
            subcompose(node, nodeState)
            nodeState.forceRecompose = false
        }
    }

    private fun subcompose(node: LayoutNode, nodeState: NodeState) {
        Snapshot.withoutReadObservation {
            ignoreRemeasureRequests {
                val content = nodeState.content
                nodeState.composition = subcomposeInto(
                    existing = nodeState.composition,
                    container = node,
                    parent = compositionContext ?: error("parent composition reference not set"),
                    reuseContent = nodeState.forceReuse,
                    composable = {
                        ReusableContentHost(nodeState.active, content)
                    }
                )
                nodeState.forceReuse = false
            }
        }
    }

    private fun subcomposeInto(
        existing: ReusableComposition?,
        container: LayoutNode,
        reuseContent: Boolean,
        parent: CompositionContext,
        composable: @Composable () -> Unit
    ): ReusableComposition {
        return if (existing == null || existing.isDisposed) {
            createSubcomposition(container, parent)
        } else {
            existing
        }
            .apply {
                if (!reuseContent) {
                    setContent(composable)
                } else {
                    setContentWithReuse(composable)
                }
            }
    }

    private fun getSlotIdAtIndex(index: Int): Any? {
        val node = root.foldedChildren[index]
        return nodeToNodeState[node]!!.slotId
    }

    fun disposeOrReuseStartingFromIndex(startIndex: Int) {
        reusableCount = 0
        val lastReusableIndex = root.foldedChildren.size - precomposedCount - 1
        var needApplyNotification = false
        if (startIndex <= lastReusableIndex) {
            // construct the set of available slot ids
            reusableSlotIdsSet.clear()
            for (i in startIndex..lastReusableIndex) {
                val slotId = getSlotIdAtIndex(i)
                reusableSlotIdsSet.add(slotId)
            }

            slotReusePolicy.getSlotsToRetain(reusableSlotIdsSet)
            // iterating backwards so it is easier to remove items
            var i = lastReusableIndex
            Snapshot.withoutReadObservation {
                while (i >= startIndex) {
                    val node = root.foldedChildren[i]
                    val nodeState = nodeToNodeState[node]!!
                    val slotId = nodeState.slotId
                    if (reusableSlotIdsSet.contains(slotId)) {
                        reusableCount++
                        if (nodeState.active) {
                            node.resetLayoutState()
                            nodeState.active = false
                            needApplyNotification = true
                        }
                    } else {
                        ignoreRemeasureRequests {
                            nodeToNodeState.remove(node)
                            nodeState.composition?.dispose()
                            root.removeAt(i, 1)
                        }
                    }
                    // remove it from slotIdToNode so it is not considered active
                    slotIdToNode.remove(slotId)
                    i--
                }
            }
        }

        if (needApplyNotification) {
            Snapshot.sendApplyNotifications()
        }

        makeSureStateIsConsistent()
    }

    private fun markActiveNodesAsReused(deactivate: Boolean) {
        precomposedCount = 0
        precomposeMap.clear()

        val childCount = root.foldedChildren.size
        if (reusableCount != childCount) {
            reusableCount = childCount
            Snapshot.withoutReadObservation {
                for (i in 0 until childCount) {
                    val node = root.foldedChildren[i]
                    val nodeState = nodeToNodeState[node]
                    if (nodeState != null && nodeState.active) {
                        node.resetLayoutState()
                        if (deactivate) {
                            nodeState.composition?.deactivate()
                            nodeState.activeState = mutableStateOf(false)
                        } else {
                            nodeState.active = false
                        }
                        // create a new instance to avoid change notifications
                        nodeState.slotId = ReusedSlotId
                    }
                }
            }
            slotIdToNode.clear()
        }

        makeSureStateIsConsistent()
    }

    private fun disposeCurrentNodes() {
        root.ignoreRemeasureRequests {
            nodeToNodeState.values.forEach {
                it.composition?.dispose()
            }
            root.removeAll()
        }

        nodeToNodeState.clear()
        slotIdToNode.clear()
        precomposedCount = 0
        reusableCount = 0
        precomposeMap.clear()

        makeSureStateIsConsistent()
    }

    fun makeSureStateIsConsistent() {
        val childrenCount = root.foldedChildren.size
        require(nodeToNodeState.size == childrenCount) {
            "Inconsistency between the count of nodes tracked by the state " +
                    "(${nodeToNodeState.size}) and the children count on the SubcomposeLayout" +
                    " ($childrenCount). Are you trying to use the state of the" +
                    " disposed SubcomposeLayout?"
        }
        require(childrenCount - reusableCount - precomposedCount >= 0) {
            "Incorrect state. Total children $childrenCount. Reusable children " +
                    "$reusableCount. Precomposed children $precomposedCount"
        }
        require(precomposeMap.size == precomposedCount) {
            "Incorrect state. Precomposed children $precomposedCount. Map size " +
                    "${precomposeMap.size}"
        }
    }

    private fun LayoutNode.resetLayoutState() {
        measurePassDelegate.measuredByParent = UsageByParent.NotUsed
        lookaheadPassDelegate?.let {
            it.measuredByParent = UsageByParent.NotUsed
        }
    }

    private fun takeNodeFromReusables(slotId: Any?): LayoutNode? {
        if (reusableCount == 0) {
            return null
        }
        val reusableNodesSectionEnd = root.foldedChildren.size - precomposedCount
        val reusableNodesSectionStart = reusableNodesSectionEnd - reusableCount
        var index = reusableNodesSectionEnd - 1
        var chosenIndex = -1
        // first try to find a node with exactly the same slotId
        while (index >= reusableNodesSectionStart) {
            if (getSlotIdAtIndex(index) == slotId) {
                // we have a node with the same slotId
                chosenIndex = index
                break
            } else {
                index--
            }
        }
        if (chosenIndex == -1) {
            // try to find a first compatible slotId from the end of the section
            index = reusableNodesSectionEnd - 1
            while (index >= reusableNodesSectionStart) {
                val node = root.foldedChildren[index]
                val nodeState = nodeToNodeState[node]!!
                if (
                    nodeState.slotId === ReusedSlotId ||
                    slotReusePolicy.areCompatible(slotId, nodeState.slotId)
                ) {
                    nodeState.slotId = slotId
                    chosenIndex = index
                    break
                }
                index--
            }
        }
        return if (chosenIndex == -1) {
            // no compatible nodes found
            null
        } else {
            if (index != reusableNodesSectionStart) {
                // we need to rearrange the items
                move(index, reusableNodesSectionStart, 1)
            }
            reusableCount--
            val node = root.foldedChildren[reusableNodesSectionStart]
            val nodeState = nodeToNodeState[node]!!
            // create a new instance to avoid change notifications
            nodeState.activeState = mutableStateOf(true)
            nodeState.forceReuse = true
            nodeState.forceRecompose = true
            node
        }
    }

    fun createMeasurePolicy(
        block: SubcomposeMeasureScope.(Constraints) -> MeasureResult
    ): MeasurePolicy {
        return object : LayoutNode.NoIntrinsicsMeasurePolicy(error = NoIntrinsicsMessage) {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                scope.layoutDirection = layoutDirection
                scope.density = density
                scope.fontScale = fontScale
                if (!isLookingAhead && root.lookaheadRoot != null) {
                    currentPostLookaheadIndex = 0
                    val result = postLookaheadMeasureScope.block(constraints)
                    val indexAfterMeasure = currentPostLookaheadIndex
                    return createMeasureResult(result) {
                        currentPostLookaheadIndex = indexAfterMeasure
                        result.placeChildren()
                        // dispose
                        disposeUnusedSlotsInPostLookahead()
                    }
                } else {
                    currentIndex = 0
                    val result = scope.block(constraints)
                    val indexAfterMeasure = currentIndex
                    return createMeasureResult(result) {
                        currentIndex = indexAfterMeasure
                        result.placeChildren()
                        disposeOrReuseStartingFromIndex(currentIndex)
                    }
                }
            }
        }
    }

    private fun disposeUnusedSlotsInPostLookahead() {
        postLookaheadPrecomposeSlotHandleMap.entries.removeAll { (slotId, handle) ->
            val id = postLookaheadComposedSlotIds.indexOf(slotId)
            if (id < 0 || id >= currentPostLookaheadIndex) {
                // Slot was not used in the latest pass of post-lookahead.
                handle.dispose()
                true
            } else {
                false
            }
        }
    }

    private inline fun createMeasureResult(
        result: MeasureResult,
        crossinline placeChildrenBlock: () -> Unit
    ) = object : MeasureResult by result {
        override fun placeChildren() {
            placeChildrenBlock()
        }
    }

    private val NoIntrinsicsMessage = "Asking for intrinsic measurements of SubcomposeLayout " +
            "layouts is not supported. This includes components that are built on top of " +
            "SubcomposeLayout, such as lazy lists, BoxWithConstraints, TabRow, etc. To mitigate " +
            "this:\n" +
            "- if intrinsic measurements are used to achieve 'match parent' sizing, consider " +
            "replacing the parent of the component with a custom layout which controls the order in " +
            "which children are measured, making intrinsic measurement not needed\n" +
            "- adding a size modifier to the component, in order to fast return the queried " +
            "intrinsic measurement."

    fun precompose(slotId: Any?, content: @Composable () -> Unit): PrecomposedSlotHandle {
        if (!root.isAttached) {
            return object : PrecomposedSlotHandle {
                override fun dispose() {}
            }
        }
        makeSureStateIsConsistent()
        if (!slotIdToNode.containsKey(slotId)) {
            // Yield ownership of PrecomposedHandle from postLookahead to the caller of precompose
            postLookaheadPrecomposeSlotHandleMap.remove(slotId)
            val node = precomposeMap.getOrPut(slotId) {
                val reusedNode = takeNodeFromReusables(slotId)
                if (reusedNode != null) {
                    // now move this node to the end where we keep precomposed items
                    val nodeIndex = root.foldedChildren.indexOf(reusedNode)
                    move(nodeIndex, root.foldedChildren.size, 1)
                    precomposedCount++
                    reusedNode
                } else {
                    createNodeAt(root.foldedChildren.size).also {
                        precomposedCount++
                    }
                }
            }
            subcompose(node, slotId, content)
        }
        return object : PrecomposedSlotHandle {
            override fun dispose() {
                makeSureStateIsConsistent()
                val node = precomposeMap.remove(slotId)
                if (node != null) {
                    check(precomposedCount > 0) { "No pre-composed items to dispose" }
                    val itemIndex = root.foldedChildren.indexOf(node)
                    check(itemIndex >= root.foldedChildren.size - precomposedCount) {
                        "Item is not in pre-composed item range"
                    }
                    // move this item into the reusable section
                    reusableCount++
                    precomposedCount--
                    val reusableStart = root.foldedChildren.size - precomposedCount - reusableCount
                    move(itemIndex, reusableStart, 1)
                    disposeOrReuseStartingFromIndex(reusableStart)
                }
            }

            override val placeablesCount: Int
                get() = precomposeMap[slotId]?.children?.size ?: 0

            override fun premeasure(index: Int, constraints: Constraints) {
                val node = precomposeMap[slotId]
                if (node != null && node.isAttached) {
                    val size = node.children.size
                    if (index < 0 || index >= size) {
                        throw IndexOutOfBoundsException(
                            "Index ($index) is out of bound of [0, $size)"
                        )
                    }
                    require(!node.isPlaced) { "Pre-measure called on node that is not placed" }
                    root.ignoreRemeasureRequests {
                        node.requireOwner().measureAndLayout(node.children[index], constraints)
                    }
                }
            }
        }
    }

    fun forceRecomposeChildren() {
        val childCount = root.foldedChildren.size
        if (reusableCount != childCount) {
            // only invalidate children if there are any non-reused ones
            // in other cases, all of them are going to be invalidated later anyways
            nodeToNodeState.forEach { (_, nodeState) ->
                nodeState.forceRecompose = true
            }

            if (!root.measurePending) {
                root.requestRemeasure()
            }
        }
    }

    private fun createNodeAt(index: Int) = LayoutNode(isVirtual = true).also { node ->
        ignoreRemeasureRequests {
            root.insertAt(index, node)
        }
    }

    private fun move(from: Int, to: Int, count: Int = 1) {
        ignoreRemeasureRequests {
            root.move(from, to, count)
        }
    }

    private inline fun ignoreRemeasureRequests(block: () -> Unit) =
        root.ignoreRemeasureRequests(block)

    private class NodeState(
        var slotId: Any?,
        var content: @Composable () -> Unit,
        var composition: ReusableComposition? = null
    ) {
        var forceRecompose = false
        var forceReuse = false
        var activeState = mutableStateOf(true)
        var active: Boolean
            get() = activeState.value
            set(value) { activeState.value = value }
    }

    private inner class Scope : SubcomposeMeasureScope {
        // MeasureScope delegation
        override var layoutDirection: LayoutDirection = LayoutDirection.Rtl
        override var density: Float = 0f
        override var fontScale: Float = 0f
        override val isLookingAhead: Boolean
            get() = root.layoutState == LayoutState.LookaheadLayingOut ||
                    root.layoutState == LayoutState.LookaheadMeasuring

        override fun subcompose(slotId: Any?, content: @Composable () -> Unit) =
            this@LayoutNodeSubcompositionsState.subcompose(slotId, content)

        override fun layout(
            width: Int,
            height: Int,
            alignmentLines: Map<AlignmentLine, Int>,
            placementBlock: Placeable.PlacementScope.() -> Unit
        ): MeasureResult {
            checkMeasuredSize(width, height)
            return object : MeasureResult {
                override val width: Int
                    get() = width
                override val height: Int
                    get() = height
                override val alignmentLines: Map<AlignmentLine, Int>
                    get() = alignmentLines

                override fun placeChildren() {
                    if (isLookingAhead) {
                        val delegate = root.innerCoordinator.lookaheadDelegate
                        if (delegate != null) {
                            delegate.placementScope.placementBlock()
                            return
                        }
                    }
                    root.innerCoordinator.placementScope.placementBlock()
                }
            }
        }
    }

    private inner class PostLookaheadMeasureScopeImpl :
        SubcomposeMeasureScope, MeasureScope by scope {
        /**
         * This function retrieves [Measurable]s created for [slotId] based on
         * the subcomposition that happened in the lookahead pass. If [slotId] was not subcomposed
         * in the lookahead pass, [subcompose] will return an [emptyList].
         */
        override fun subcompose(slotId: Any?, content: @Composable () -> Unit): List<Measurable> {
            val measurables = slotIdToNode[slotId]?.childMeasurables
            if (measurables != null) {
                return measurables
            }
            return postLookaheadSubcompose(slotId, content)
        }
    }

    private fun postLookaheadSubcompose(
        slotId: Any?,
        content: @Composable () -> Unit
    ): List<Measurable> {
        require(postLookaheadComposedSlotIds.size >= currentPostLookaheadIndex) {
            "Error: currentPostLookaheadIndex cannot be greater than the size of the" +
                    "postLookaheadComposedSlotIds list."
        }
        if (postLookaheadComposedSlotIds.size == currentPostLookaheadIndex) {
            postLookaheadComposedSlotIds.add(slotId)
        } else {
            postLookaheadComposedSlotIds[currentPostLookaheadIndex] = slotId
        }
        currentPostLookaheadIndex++
        if (!precomposeMap.contains(slotId)) {
            // Not composed yet
            precompose(slotId, content).also {
                postLookaheadPrecomposeSlotHandleMap[slotId] = it
            }
            if (root.layoutState == LayoutState.LayingOut) {
                root.requestLookaheadRelayout(true)
            } else {
                root.requestLookaheadRemeasure(true)
            }
        }

        return precomposeMap[slotId]?.run {
            measurePassDelegate.childDelegates.also {
                it.fastForEach { delegate -> delegate.markDetachedFromParentLookaheadPass() }
            }
        } ?: emptyList()
    }
}

/**
 * This policy allows [SubcomposeLayout] to retain some of slots which we were used but not
 * used anymore instead of disposing them. Next time when you try to compose a new slot instead of
 * creating a completely new slot the layout would reuse the kept slot. This allows to do less
 * work especially if the slot contents are similar.
 */
interface SubcomposeSlotReusePolicy {
    /**
     * This function will be called with [slotIds] set populated with the slot ids available to
     * reuse. In the implementation you can remove slots you don't want to retain.
     */
    fun getSlotsToRetain(slotIds: SlotIdsSet)

    /**
     * Returns true if the content previously composed with [reusableSlotId] is compatible with
     * the content which is going to be composed for [slotId].
     * Slots could be considered incompatible if they display completely different types of the UI.
     */
    fun areCompatible(slotId: Any?, reusableSlotId: Any?): Boolean

    /**
     * Set containing slot ids currently available to reuse. Used by [getSlotsToRetain].
     *
     * This class works exactly as [MutableSet], but doesn't allow to add new items in it.
     */
    class SlotIdsSet internal constructor(
        private val set: MutableSet<Any?> = mutableSetOf()
    ) : Collection<Any?> by set {

        internal fun add(slotId: Any?) = set.add(slotId)

        override fun iterator(): MutableIterator<Any?> = set.iterator()

        /**
         * Removes a [slotId] from this set, if it is present.
         *
         * @return `true` if the slot id was removed, `false` if the set was not modified.
         */
        fun remove(slotId: Any?): Boolean = set.remove(slotId)

        /**
         * Removes all slot ids from [slotIds] that are also contained in this set.
         *
         * @return `true` if any slot id was removed, `false` if the set was not modified.
         */
        fun removeAll(slotIds: Collection<Any?>): Boolean = set.remove(slotIds)

        /**
         * Removes all slot ids that match the given [predicate].
         *
         * @return `true` if any slot id was removed, `false` if the set was not modified.
         */
        fun removeAll(predicate: (Any?) -> Boolean): Boolean = set.removeAll(predicate)

        /**
         * Retains only the slot ids that are contained in [slotIds].
         *
         * @return `true` if any slot id was removed, `false` if the set was not modified.
         */
        fun retainAll(slotIds: Collection<Any?>): Boolean = set.retainAll(slotIds)

        /**
         * Retains only slotIds that match the given [predicate].
         *
         * @return `true` if any slot id was removed, `false` if the set was not modified.
         */
        fun retainAll(predicate: (Any?) -> Boolean): Boolean = set.retainAll(predicate)

        /**
         * Removes all slot ids from this set.
         */
        fun clear() = set.clear()
    }
}

/**
 * Creates [SubcomposeSlotReusePolicy] which retains the fixed amount of slots.
 *
 * @param maxSlotsToRetainForReuse the [SubcomposeLayout] will retain up to this amount of slots.
 */
fun SubcomposeSlotReusePolicy(maxSlotsToRetainForReuse: Int): SubcomposeSlotReusePolicy =
    FixedCountSubcomposeSlotReusePolicy(maxSlotsToRetainForReuse)

private val ReusedSlotId = object {
    override fun toString(): String = "ReusedSlotId"
}

private class FixedCountSubcomposeSlotReusePolicy(
    private val maxSlotsToRetainForReuse: Int
) : SubcomposeSlotReusePolicy {

    override fun getSlotsToRetain(slotIds: SubcomposeSlotReusePolicy.SlotIdsSet) {
        if (slotIds.size > maxSlotsToRetainForReuse) {
            var count = 0
            with(slotIds.iterator()) {
                // keep first maxSlotsToRetainForReuse items
                while (hasNext()) {
                    next()
                    count++
                    if (count > maxSlotsToRetainForReuse) {
                        remove()
                    }
                }
            }
        }
    }

    override fun areCompatible(slotId: Any?, reusableSlotId: Any?): Boolean = true
}