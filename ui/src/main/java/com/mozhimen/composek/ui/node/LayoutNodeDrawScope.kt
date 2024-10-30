package com.mozhimen.composek.ui.node

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.toSize

/**
 * @ClassName LayoutNodeDrawScope
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 23:57
 * @Version 1.0
 */
/**
 * [ContentDrawScope] implementation that extracts density and layout direction information
 * from the given NodeCoordinator
 */
@OptIn(ExperimentalComposeUiApi::class)
internal class LayoutNodeDrawScope(
    private val canvasDrawScope: CanvasDrawScope = CanvasDrawScope()
) : DrawScope by canvasDrawScope, ContentDrawScope {

    // NOTE, currently a single ComponentDrawScope is shared across composables
    // which done to allocate a single set of Paint objects and re-use them across
    // draw calls for all composables.
    // As a result there could be thread safety concerns here for multi-threaded drawing
    // scenarios, generally a single ComponentDrawScope should be shared for a particular thread
    private var drawNode: DrawModifierNode? = null

    override fun drawContent() {
        drawIntoCanvas { canvas ->
            val drawNode = drawNode!!
            val nextDrawNode = drawNode.nextDrawNode()
            // NOTE(lmr): we only run performDraw directly on the node if the node's coordinator
            // is our own. This seems to work, but we should think about a cleaner way to dispatch
            // the draw pass as with the new modifier.node / coordinator structure this feels
            // somewhat error prone.
            if (nextDrawNode != null) {
                nextDrawNode.dispatchForKind(Nodes.Draw) {
                    it.performDraw(canvas)
                }
            } else {
                // TODO(lmr): this is needed in the case that the drawnode is also a measure node,
                //  but we should think about the right ways to handle this as this is very error
                //  prone i think
                val coordinator = drawNode.requireCoordinator(Nodes.Draw)
                val nextCoordinator = if (coordinator.tail === drawNode.node)
                    coordinator.wrapped!!
                else
                    coordinator
                nextCoordinator.performDraw(canvas)
            }
        }
    }

    // This is not thread safe
    fun DrawModifierNode.performDraw(canvas: Canvas) {
        val coordinator = requireCoordinator(Nodes.Draw)
        val size = coordinator.size.toSize()
        val drawScope = coordinator.layoutNode.mDrawScope
        drawScope.drawDirect(canvas, size, coordinator, this)
    }

    internal fun draw(
        canvas: Canvas,
        size: Size,
        coordinator: NodeCoordinator,
        drawNode: Modifier.Node,
    ) {
        drawNode.dispatchForKind(Nodes.Draw) {
            drawDirect(canvas, size, coordinator, it)
        }
    }

    internal fun drawDirect(
        canvas: Canvas,
        size: Size,
        coordinator: NodeCoordinator,
        drawNode: DrawModifierNode,
    ) {
        val previousDrawNode = this.drawNode
        this.drawNode = drawNode
        canvasDrawScope.draw(
            coordinator,
            coordinator.layoutDirection,
            canvas,
            size
        ) {
            with(drawNode) {
                this@LayoutNodeDrawScope.draw()
            }
        }
        this.drawNode = previousDrawNode
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun DelegatableNode.nextDrawNode(): Modifier.Node? {
    val drawMask = Nodes.Draw.mask
    val measureMask = Nodes.Layout.mask
    val child = node.child ?: return null
    if (child.aggregateChildKindSet and drawMask == 0) return null
    var next: Modifier.Node? = child
    while (next != null) {
        if (next.kindSet and measureMask != 0) return null
        if (next.kindSet and drawMask != 0) {
            return next
        }
        next = next.child
    }
    return null
}