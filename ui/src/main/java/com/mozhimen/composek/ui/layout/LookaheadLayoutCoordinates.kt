package com.mozhimen.composek.ui.layout

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import com.mozhimen.composek.ui.node.LookaheadDelegate
import com.mozhimen.composek.ui.node.NodeCoordinator

/**
 * @ClassName LookaheadLayoutCoordinates
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal class LookaheadLayoutCoordinates(val lookaheadDelegate: LookaheadDelegate) :
    LayoutCoordinates {
    val coordinator: NodeCoordinator
        get() = lookaheadDelegate.coordinator

    override val size: IntSize
        get() = lookaheadDelegate.let { IntSize(it.width, it.height) }
    override val providedAlignmentLines: Set<AlignmentLine>
        get() = coordinator.providedAlignmentLines

    override val parentLayoutCoordinates: LayoutCoordinates?
        get() {
            check(isAttached) { NodeCoordinator.ExpectAttachedLayoutCoordinates }
            return coordinator.layoutNode.outerCoordinator.wrappedBy?.let {
                it.lookaheadDelegate?.coordinates
            }
        }
    override val parentCoordinates: LayoutCoordinates?
        get() {
            check(isAttached) { NodeCoordinator.ExpectAttachedLayoutCoordinates }
            return coordinator.wrappedBy?.lookaheadDelegate?.coordinates
        }

    override val isAttached: Boolean
        get() = coordinator.isAttached

    private val lookaheadOffset: Offset
        get() = lookaheadDelegate.rootLookaheadDelegate.let {
            localPositionOf(it.coordinates, Offset.Zero) -
                    coordinator.localPositionOf(it.coordinator, Offset.Zero)
        }

    override fun windowToLocal(relativeToWindow: Offset): Offset =
        coordinator.windowToLocal(relativeToWindow) + lookaheadOffset

    override fun localToWindow(relativeToLocal: Offset): Offset =
        coordinator.localToWindow(relativeToLocal + lookaheadOffset)

    override fun localToRoot(relativeToLocal: Offset): Offset =
        coordinator.localToRoot(relativeToLocal + lookaheadOffset)

    override fun localPositionOf(
        sourceCoordinates: LayoutCoordinates,
        relativeToSource: Offset
    ): Offset {
        if (sourceCoordinates is LookaheadLayoutCoordinates) {
            val source = sourceCoordinates.lookaheadDelegate
            source.coordinator.onCoordinatesUsed()
            val commonAncestor = coordinator.findCommonAncestor(source.coordinator)

            return commonAncestor.lookaheadDelegate?.let { ancestor ->
                // Common ancestor is in lookahead
                (source.positionIn(ancestor) + relativeToSource.round() -
                        lookaheadDelegate.positionIn(ancestor)).toOffset()
            } ?: commonAncestor.let {
                // The two coordinates are in two separate LookaheadLayouts
                val sourceRoot = source.rootLookaheadDelegate
                val relativePosition = source.positionIn(sourceRoot) +
                        sourceRoot.position + relativeToSource.round() -
                        with(lookaheadDelegate) {
                            (positionIn(rootLookaheadDelegate) + rootLookaheadDelegate.position)
                        }

                lookaheadDelegate.rootLookaheadDelegate.coordinator.wrappedBy!!.localPositionOf(
                    sourceRoot.coordinator.wrappedBy!!, relativePosition.toOffset()
                )
            }
        } else {
            val rootDelegate = lookaheadDelegate.rootLookaheadDelegate
            // This is a case of mixed coordinates where `this` is lookahead coords, and
            // `sourceCoordinates` isn't. Therefore we'll break this into two parts:
            // local position in lookahead coords space && local position in regular layout coords
            // space.
            return localPositionOf(rootDelegate.lookaheadLayoutCoordinates, relativeToSource) +
                    rootDelegate.coordinator.coordinates.localPositionOf(sourceCoordinates, Offset.Zero)
        }
    }

    override fun localBoundingBoxOf(
        sourceCoordinates: LayoutCoordinates,
        clipBounds: Boolean
    ): Rect = coordinator.localBoundingBoxOf(sourceCoordinates, clipBounds)

    override fun transformFrom(sourceCoordinates: LayoutCoordinates, matrix: Matrix) {
        coordinator.transformFrom(sourceCoordinates, matrix)
    }

    override fun get(alignmentLine: AlignmentLine): Int = lookaheadDelegate.get(alignmentLine)
}

internal val LookaheadDelegate.rootLookaheadDelegate: LookaheadDelegate
    get() {
        var root = layoutNode
        while (root.parent?.lookaheadRoot != null) {
            val lookaheadRoot = root.parent?.lookaheadRoot!!
            if (lookaheadRoot.isVirtualLookaheadRoot) {
                root = root.parent!!
            } else {
                root = root.parent!!.lookaheadRoot!!
            }
        }
        return root.outerCoordinator.lookaheadDelegate!!
    }
