package com.mozhimen.composek.ui.node

import androidx.compose.runtime.snapshots.SnapshotStateObserver

/**
 * @ClassName OwnerSnapshotObserver
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 23:53
 * @Version 1.0
 */
/**
 * Performs snapshot observation for blocks like draw and layout which should be re-invoked
 * automatically when the snapshot value has been changed.
 */
@Suppress("CallbackName") // TODO rename this and SnapshotStateObserver. b/173401548
internal class OwnerSnapshotObserver(onChangedExecutor: (callback: () -> Unit) -> Unit) {

    private val observer = SnapshotStateObserver(onChangedExecutor)

    private val onCommitAffectingLookaheadMeasure: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestLookaheadRemeasure()
        }
    }

    private val onCommitAffectingMeasure: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestRemeasure()
        }
    }

    private val onCommitAffectingSemantics: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.invalidateSemantics()
        }
    }

    private val onCommitAffectingLayout: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestRelayout()
        }
    }

    private val onCommitAffectingLayoutModifier: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestRelayout()
        }
    }

    private val onCommitAffectingLayoutModifierInLookahead: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestLookaheadRelayout()
        }
    }

    private val onCommitAffectingLookahead: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValidOwnerScope) {
            layoutNode.requestLookaheadRelayout()
        }
    }

    /**
     * Observe snapshot reads during layout of [node], executed in [block].
     */
    internal fun observeLayoutSnapshotReads(
        node: LayoutNode,
        affectsLookahead: Boolean = true,
        block: () -> Unit
    ) {
        if (affectsLookahead && node.lookaheadRoot != null) {
            observeReads(node, onCommitAffectingLookahead, block)
        } else {
            observeReads(node, onCommitAffectingLayout, block)
        }
    }

    /**
     * Observe snapshot reads during layout of [node]'s LayoutModifiers, executed in [block].
     */
    internal fun observeLayoutModifierSnapshotReads(
        node: LayoutNode,
        affectsLookahead: Boolean = true,
        block: () -> Unit
    ) {
        if (affectsLookahead && node.lookaheadRoot != null) {
            observeReads(node, onCommitAffectingLayoutModifierInLookahead, block)
        } else {
            observeReads(node, onCommitAffectingLayoutModifier, block)
        }
    }

    /**
     * Observe snapshot reads during measure of [node], executed in [block].
     */
    internal fun observeMeasureSnapshotReads(
        node: LayoutNode,
        affectsLookahead: Boolean = true,
        block: () -> Unit
    ) {
        if (affectsLookahead && node.lookaheadRoot != null) {
            observeReads(node, onCommitAffectingLookaheadMeasure, block)
        } else {
            observeReads(node, onCommitAffectingMeasure, block)
        }
    }

    internal fun observeSemanticsReads(
        node: LayoutNode,
        block: () -> Unit
    ) {
        observeReads(node, onCommitAffectingSemantics, block)
    }

    /**
     * Observe snapshot reads for any target, allowing consumers to determine how to respond
     * to state changes.
     */
    internal fun <T : OwnerScope> observeReads(
        target: T,
        onChanged: (T) -> Unit,
        block: () -> Unit
    ) {
        observer.observeReads(target, onChanged, block)
    }

    internal fun clearInvalidObservations() {
        observer.clearIf { !(it as OwnerScope).isValidOwnerScope }
    }

    internal fun clear(target: Any) {
        observer.clear(target)
    }

    internal fun startObserving() {
        observer.start()
    }

    internal fun stopObserving() {
        observer.stop()
        observer.clear()
    }
}
