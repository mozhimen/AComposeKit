package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReusableComposeNode
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.geometry.Offset
import com.mozhimen.composek.ui.node.LayoutNode
import com.mozhimen.composek.ui.node.NodeCoordinator

/**
 * @ClassName LookaheadScopeImpl
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * [LookaheadScope] starts a scope in which all layouts scope will receive a lookahead pass
 * preceding the main measure/layout pass. This lookahead pass will calculate the layout
 * size and position for all child layouts, and make the lookahead results available in
 * [Modifier.intermediateLayout]. [Modifier.intermediateLayout] gets invoked in the main
 * pass to allow transient layout changes in the main pass that gradually morph the layout
 * over the course of multiple frames until it catches up with lookahead.
 *
 * @sample androidx.compose.ui.samples.LookaheadLayoutCoordinatesSample
 *
 * @param content The child composable to be laid out.
 */
@UiComposable
@Composable
fun LookaheadScope(content: @Composable @UiComposable LookaheadScope.() -> Unit) {
    val scope = remember { LookaheadScopeImpl() }
    ReusableComposeNode<LayoutNode, Applier<Any>>(
        factory = { LayoutNode(isVirtual = true) },
        update = {
            init { isVirtualLookaheadRoot = true }
            set(scope) { scope ->
                // This internal lambda will be invoked during placement.
                scope.scopeCoordinates = {
                    parent!!.innerCoordinator.coordinates
                }
            }
        },
        content = {
            scope.content()
        }
    )
}

/**
 * [LookaheadScope] provides a receiver scope for all (direct and indirect) child layouts in
 * [LookaheadScope]. This receiver scope allows access to [lookaheadScopeCoordinates] from
 * any child's [Placeable.PlacementScope]. It also allows any child to convert
 * [LayoutCoordinates] (which can be retrieved in [Placeable.PlacementScope]) to
 * [LayoutCoordinates] in lookahead coordinate space using [toLookaheadCoordinates].
 *
 * @sample androidx.compose.ui.samples.LookaheadLayoutCoordinatesSample
 */
interface LookaheadScope {
    /**
     * Converts a [LayoutCoordinates] into a [LayoutCoordinates] in the Lookahead coordinates space.
     * This is only applicable to child layouts within [LookaheadScope].
     */
    @ExperimentalComposeUiApi
    fun LayoutCoordinates.toLookaheadCoordinates(): LayoutCoordinates

    /**
     * Returns the [LayoutCoordinates] of the [LookaheadScope]. This is
     * only accessible from [Placeable.PlacementScope] (i.e. during placement time).
     */
    @ExperimentalComposeUiApi
    val Placeable.PlacementScope.lookaheadScopeCoordinates: LayoutCoordinates

    /**
     * Calculates the localPosition in the Lookahead coordinate space. This is a convenient
     * method for 1) converting the given [LayoutCoordinates] to lookahead coordinates using
     * [toLookaheadCoordinates], and 2) invoking [LayoutCoordinates.localPositionOf] with the
     * converted coordinates.
     */
    @ExperimentalComposeUiApi
    fun LayoutCoordinates.localLookaheadPositionOf(coordinates: LayoutCoordinates) =
        this.toLookaheadCoordinates().localPositionOf(
            coordinates.toLookaheadCoordinates(),
            Offset.Zero
        )
}

@OptIn(ExperimentalComposeUiApi::class)
internal class LookaheadScopeImpl(
    var scopeCoordinates: (() -> LayoutCoordinates)? = null
) : LookaheadScope {
    override fun LayoutCoordinates.toLookaheadCoordinates(): LayoutCoordinates {
        return this as? LookaheadLayoutCoordinates
            ?: (this as NodeCoordinator).let {
                // If the coordinator has no lookahead delegate. Its
                // lookahead coords is the same as its coords
                it.lookaheadDelegate?.lookaheadLayoutCoordinates ?: it
            }
    }

    override val Placeable.PlacementScope.lookaheadScopeCoordinates: LayoutCoordinates
        get() = scopeCoordinates!!()
}