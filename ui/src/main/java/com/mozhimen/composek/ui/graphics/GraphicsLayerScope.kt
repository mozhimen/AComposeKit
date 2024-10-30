package com.mozhimen.composek.ui.graphics

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.DefaultCameraDistance
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Density

/**
 * @ClassName GraphicsLayerScope
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 23:49
 * @Version 1.0
 */
internal object Fields {
    const val ScaleX: Int = 0b1 shl 0
    const val ScaleY: Int = 0b1 shl 1
    const val Alpha: Int = 0b1 shl 2
    const val TranslationX: Int = 0b1 shl 3
    const val TranslationY: Int = 0b1 shl 4
    const val ShadowElevation: Int = 0b1 shl 5
    const val AmbientShadowColor: Int = 0b1 shl 6
    const val SpotShadowColor: Int = 0b1 shl 7
    const val RotationX: Int = 0b1 shl 8
    const val RotationY: Int = 0b1 shl 9
    const val RotationZ: Int = 0b1 shl 10
    const val CameraDistance: Int = 0b1 shl 11
    const val TransformOrigin: Int = 0b1 shl 12
    const val Shape: Int = 0b1 shl 13
    const val Clip: Int = 0b1 shl 14
    const val CompositingStrategy: Int = 0b1 shl 15
    const val RenderEffect: Int = 0b1 shl 17

    const val MatrixAffectingFields = ScaleX or
            ScaleY or
            TranslationX or
            TranslationY or
            TransformOrigin or
            RotationX or
            RotationY or
            RotationZ or
            CameraDistance
}

internal class ReusableGraphicsLayerScope : GraphicsLayerScope {
    internal var mutatedFields: Int = 0

    override var scaleX: Float = 1f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.ScaleX
                field = value
            }
        }
    override var scaleY: Float = 1f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.ScaleY
                field = value
            }
        }
    override var alpha: Float = 1f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.Alpha
                field = value
            }
        }
    override var translationX: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.TranslationX
                field = value
            }
        }
    override var translationY: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.TranslationY
                field = value
            }
        }
    override var shadowElevation: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.ShadowElevation
                field = value
            }
        }
    override var ambientShadowColor: Color = DefaultShadowColor
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.AmbientShadowColor
                field = value
            }
        }
    override var spotShadowColor: Color = DefaultShadowColor
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.SpotShadowColor
                field = value
            }
        }
    override var rotationX: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.RotationX
                field = value
            }
        }
    override var rotationY: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.RotationY
                field = value
            }
        }
    override var rotationZ: Float = 0f
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.RotationZ
                field = value
            }
        }
    override var cameraDistance: Float = DefaultCameraDistance
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.CameraDistance
                field = value
            }
        }
    override var transformOrigin: TransformOrigin = TransformOrigin.Center
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.TransformOrigin
                field = value
            }
        }
    override var shape: Shape = RectangleShape
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.Shape
                field = value
            }
        }
    override var clip: Boolean = false
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.Clip
                field = value
            }
        }
    override var compositingStrategy: CompositingStrategy = CompositingStrategy.Auto
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.CompositingStrategy
                field = value
            }
        }
    override var size: Size = Size.Unspecified

    internal var graphicsDensity: Density = Density(1.0f)

    override val density: Float
        get() = graphicsDensity.density

    override val fontScale: Float
        get() = graphicsDensity.fontScale

    override var renderEffect: RenderEffect? = null
        set(value) {
            if (field != value) {
                mutatedFields = mutatedFields or Fields.RenderEffect
                field = value
            }
        }

    fun reset() {
        scaleX = 1f
        scaleY = 1f
        alpha = 1f
        translationX = 0f
        translationY = 0f
        shadowElevation = 0f
        ambientShadowColor = DefaultShadowColor
        spotShadowColor = DefaultShadowColor
        rotationX = 0f
        rotationY = 0f
        rotationZ = 0f
        cameraDistance = DefaultCameraDistance
        transformOrigin = TransformOrigin.Center
        shape = RectangleShape
        clip = false
        renderEffect = null
        compositingStrategy = CompositingStrategy.Auto
        size = Size.Unspecified
        // mutatedFields should be reset last as all the setters above modify it.
        mutatedFields = 0
    }
}