package com.mozhimen.composek.ui.node

import androidx.compose.ui.geometry.MutableRect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import com.mozhimen.composek.ui.graphics.ReusableGraphicsLayerScope

/**
 * @ClassName OwnedLayer
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 23:51
 * @Version 1.0
 */
/**
 * A layer returned by [Owner.createLayer] to separate drawn content.
 */
internal interface OwnedLayer {

    /**
     * Applies the new layer properties and causing this layer to be redrawn.
     */
    fun updateLayerProperties(
        scope: ReusableGraphicsLayerScope,
        layoutDirection: LayoutDirection,
        density: Density,
    )

    /**
     * Returns `false` if [position] is outside the clipped region or `true` if clipping
     * is disabled or it is within the clipped region.
     */
    fun isInLayer(position: Offset): Boolean

    /**
     * Changes the position of the layer contents.
     */
    fun move(position: IntOffset)

    /**
     * Changes the size of the layer's drawn area.
     */
    fun resize(size: IntSize)

    /**
     * Causes the layer to be drawn into [canvas]
     */
    fun drawLayer(canvas: Canvas)

    /**
     * Updates the drawing on the current canvas.
     */
    fun updateDisplayList()

    /**
     * Asks to the layer to redraw itself without forcing all of its parents to redraw.
     */
    fun invalidate()

    /**
     * Indicates that the layer is no longer needed.
     */
    fun destroy()

    /**
     * Transforms [point] to this layer's bounds, returning an [Offset] with the transformed x
     * and y values.
     *
     * @param point the [Offset] to transform to this layer's bounds
     * @param inverse whether to invert this layer's transform [Matrix] first, such as when
     * converting an offset in a parent layer to be in this layer's coordinates.
     */
    fun mapOffset(point: Offset, inverse: Boolean): Offset

    /**
     * Transforms the provided [rect] to this layer's bounds, then updates [rect] to match the
     * new bounds after the transform.
     *
     * @param rect the bounds to transform to this layer's bounds, and then mutate with the
     * resulting value
     * @param inverse whether to invert this layer's transform [Matrix] first, such as when
     * converting bounds in a parent layer to be in this layer's coordinates.
     */
    fun mapBounds(rect: MutableRect, inverse: Boolean)

    /**
     * Reuse this layer after it was [destroy]ed, setting the new
     * [drawBlock] and [invalidateParentLayer] values. The layer will be reinitialized
     * as new after this call.
     */
    fun reuseLayer(drawBlock: (Canvas) -> Unit, invalidateParentLayer: () -> Unit)

    /**
     * Calculates the transform from the parent to the local coordinates and multiplies
     * [matrix] by the transform.
     */
    fun transform(matrix: Matrix)

    /**
     * Calculates the transform from the layer to the parent and multiplies [matrix] by
     * the transform.
     */
    fun inverseTransform(matrix: Matrix)
}
