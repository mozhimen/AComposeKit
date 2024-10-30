package com.mozhimen.composek.ui.platform

import android.graphics.Outline
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.CanvasHolder
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RenderEffect

/**
 * @ClassName DeviceRenderNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * RenderNode on Q+ and RenderNode on M-P devices have different APIs. This interface
 * unifies the access so that [RenderNodeLayer] can be used for both.
 */
internal interface DeviceRenderNode {
    val uniqueId: Long
    val left: Int
    val top: Int
    val right: Int
    val bottom: Int
    val width: Int
    val height: Int
    var scaleX: Float
    var scaleY: Float
    var translationX: Float
    var translationY: Float
    var elevation: Float
    var ambientShadowColor: Int
    var spotShadowColor: Int
    var rotationZ: Float
    var rotationX: Float
    var rotationY: Float
    var cameraDistance: Float
    var pivotX: Float
    var pivotY: Float
    var clipToOutline: Boolean
    var clipToBounds: Boolean
    var alpha: Float
    var renderEffect: RenderEffect?
    val hasDisplayList: Boolean
    var compositingStrategy: CompositingStrategy

    fun setOutline(outline: Outline?)
    fun setPosition(left: Int, top: Int, right: Int, bottom: Int): Boolean
    fun offsetLeftAndRight(offset: Int)
    fun offsetTopAndBottom(offset: Int)
    fun record(
        canvasHolder: CanvasHolder,
        clipPath: Path?,
        drawBlock: (Canvas) -> Unit
    )
    fun getMatrix(matrix: android.graphics.Matrix)
    fun getInverseMatrix(matrix: android.graphics.Matrix)
    fun drawInto(canvas: android.graphics.Canvas)
    fun setHasOverlappingRendering(hasOverlappingRendering: Boolean): Boolean

    /**
     * Debugging method used to dump the underlying parameters of the backing RenderNode
     * on the platform. This is used for testing purposes to avoid having to query the
     * RenderNode directly and potentially crashing on certain multiplatform configurations
     */
    fun dumpRenderNodeData(): DeviceRenderNodeData

    fun discardDisplayList()
}

/**
 * Data class representing the actual parameters of the platform RenderNode in
 * the [DeviceRenderNode] implementation. Before RenderNode was made public API, the parameter
 * inputs were inconsistent across platform versions. This class is used to verify the result
 * of the internal RenderNode values for testing purposes based on the consistent inputs provided
 * as part of the Layer API. For example, [cameraDistance] is negative on RenderNode implementations
 * prior to Q.
 */
internal data class DeviceRenderNodeData(
    val uniqueId: Long,
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val width: Int,
    val height: Int,
    var scaleX: Float,
    var scaleY: Float,
    var translationX: Float,
    var translationY: Float,
    var elevation: Float,
    var ambientShadowColor: Int,
    var spotShadowColor: Int,
    var rotationZ: Float,
    var rotationX: Float,
    var rotationY: Float,
    var cameraDistance: Float,
    var pivotX: Float,
    var pivotY: Float,
    var clipToOutline: Boolean,
    var clipToBounds: Boolean,
    var alpha: Float,
    var renderEffect: RenderEffect?,
    var compositingStrategy: CompositingStrategy
)