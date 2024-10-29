package com.mozhimen.composek.ui.draganddrop

import android.graphics.Canvas as AndroidCanvas
import android.graphics.Point
import android.view.View
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * @ClassName ComposeDragShadowBuilder
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Draws a drag shadow for a [View.DragShadowBuilder] with the DrawScope lambda
 * provided by [drawDragDecoration].
 */
internal class ComposeDragShadowBuilder(
    private val density: Density,
    private val decorationSize: Size,
    private val drawDragDecoration: DrawScope.() -> Unit,
) : View.DragShadowBuilder() {

    override fun onProvideShadowMetrics(
        outShadowSize: Point,
        outShadowTouchPoint: Point
    ) = with(density) {
        outShadowSize.set(
            decorationSize.width.toDp().roundToPx(),
            decorationSize.height.toDp().roundToPx()
        )
        outShadowTouchPoint.set(
            outShadowSize.x / 2,
            outShadowSize.y / 2
        )
    }

    override fun onDrawShadow(canvas: android.graphics.Canvas) {
        CanvasDrawScope().draw(
            density = density,
            size = decorationSize,
            layoutDirection = LayoutDirection.Ltr,
            canvas = Canvas(canvas),
            block = drawDragDecoration,
        )
    }
}