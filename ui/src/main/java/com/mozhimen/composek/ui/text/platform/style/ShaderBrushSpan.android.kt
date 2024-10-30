package com.mozhimen.composek.ui.text.platform.style

import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.ShaderBrush
import com.mozhimen.composek.ui.text.platform.setAlpha

/**
 * @ClassName ShaderBrushSpan
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * A span that applies [ShaderBrush] to TextPaint after receiving a specified size
 */
internal class ShaderBrushSpan(
    val shaderBrush: ShaderBrush,
    val alpha: Float
) : CharacterStyle(), UpdateAppearance {

    var size: Size by mutableStateOf(Size.Unspecified)

    private val shaderState: State<Shader?> = derivedStateOf {
        if (size.isUnspecified || size.isEmpty()) {
            null
        } else {
            shaderBrush.createShader(size)
        }
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.setAlpha(alpha)
        textPaint.shader = shaderState.value
    }
}
