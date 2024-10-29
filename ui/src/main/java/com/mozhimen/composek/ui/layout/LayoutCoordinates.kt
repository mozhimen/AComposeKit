package com.mozhimen.composek.ui.layout

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntSize

/**
 * @ClassName LayoutCoordinates
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * A holder of the measured bounds for the [Layout].
 */
interface LayoutCoordinates {
    /**
     * The size of this layout in the local coordinates space.
     */
    val size: IntSize

    /**
     * The alignment lines provided for this layout, not including inherited lines.
     */
    val providedAlignmentLines: Set<AlignmentLine>

    /**
     * The coordinates of the parent layout. Null if there is no parent.
     */
    val parentLayoutCoordinates: LayoutCoordinates?

    /**
     * The coordinates of the parent layout modifier or parent layout if there is no
     * parent layout modifier, or `null` if there is no parent.
     */
    val parentCoordinates: LayoutCoordinates?

    /**
     * Returns false if the corresponding layout was detached from the hierarchy.
     */
    val isAttached: Boolean

    /**
     * Converts [relativeToWindow] relative to the window's origin into an [Offset] relative to
     * this layout.
     */
    fun windowToLocal(relativeToWindow: Offset): Offset

    /**
     * Converts [relativeToLocal] position within this layout into an [Offset] relative to the
     * window's origin.
     */
    fun localToWindow(relativeToLocal: Offset): Offset

    /**
     * Converts a local position within this layout into an offset from the root composable.
     */
    fun localToRoot(relativeToLocal: Offset): Offset

    /**
     * Converts an [relativeToSource] in [sourceCoordinates] space into local coordinates.
     * [sourceCoordinates] may be any [LayoutCoordinates] that belong to the same
     * compose layout hierarchy.
     */
    fun localPositionOf(sourceCoordinates: LayoutCoordinates, relativeToSource: Offset): Offset

    /**
     * Returns the bounding box of [sourceCoordinates] in the local coordinates.
     * If [clipBounds] is `true`, any clipping that occurs between [sourceCoordinates] and
     * this layout will affect the returned bounds, and can even result in an empty rectangle
     * if clipped regions do not overlap. If [clipBounds] is false, the bounding box of
     * [sourceCoordinates] will be converted to local coordinates irrespective of any clipping
     * applied between the layouts.
     *
     * When rotation or scaling is applied, the bounding box of the rotated or scaled value
     * will be computed in the local coordinates. For example, if a 40 pixels x 20 pixel layout
     * is rotated 90 degrees, the bounding box will be 20 pixels x 40 pixels in its parent's
     * coordinates.
     */
    fun localBoundingBoxOf(sourceCoordinates: LayoutCoordinates, clipBounds: Boolean = true): Rect

    /**
     * Modifies [matrix] to be a transform to convert a coordinate in [sourceCoordinates]
     * to a coordinate in `this` [LayoutCoordinates].
     */
    @Suppress("DocumentExceptions")
    fun transformFrom(sourceCoordinates: LayoutCoordinates, matrix: Matrix) {
        throw UnsupportedOperationException(
            "transformFrom is not implemented on this LayoutCoordinates"
        )
    }

    /**
     * Returns the position in pixels of an [alignment line][AlignmentLine],
     * or [AlignmentLine.Unspecified] if the line is not provided.
     */
    operator fun get(alignmentLine: AlignmentLine): Int
}
