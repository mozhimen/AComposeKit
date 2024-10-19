package com.mozhimen.composek.textk

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import com.mozhimen.composek.basic.foundation.gestures.detectTapAndPress

/**
 * @ClassName TextFieldPressGestureFilter
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

/**
 * Required for the press and tap [MutableInteractionSource] consistency for TextField.
 */
internal fun Modifier.tapPressTextFieldModifier(
    interactionSource: MutableInteractionSource?,
    enabled: Boolean = true,
    onTap: (Offset) -> Unit
): Modifier = if (enabled) composed {
    val scope = rememberCoroutineScope()
    val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }
    val onTapState = rememberUpdatedState(onTap)
    DisposableEffect(interactionSource) {
        onDispose {
            pressedInteraction.value?.let { oldValue ->
                val interaction = PressInteraction.Cancel(oldValue)
                interactionSource?.tryEmit(interaction)
                pressedInteraction.value = null
            }
        }
    }
    Modifier.pointerInput(interactionSource) {
        detectTapAndPress(
            onPress = {
                scope.launch {
                    // Remove any old interactions if we didn't fire stop / cancel properly
                    pressedInteraction.value?.let { oldValue ->
                        val interaction = PressInteraction.Cancel(oldValue)
                        interactionSource?.emit(interaction)
                        pressedInteraction.value = null
                    }
                    val interaction = PressInteraction.Press(it)
                    interactionSource?.emit(interaction)
                    pressedInteraction.value = interaction
                }
                val success = tryAwaitRelease()
                scope.launch {
                    pressedInteraction.value?.let { oldValue ->
                        val interaction =
                            if (success) {
                                PressInteraction.Release(oldValue)
                            } else {
                                PressInteraction.Cancel(oldValue)
                            }
                        interactionSource?.emit(interaction)
                        pressedInteraction.value = null
                    }
                }
            },
            onTap = { onTapState.value.invoke(it) }
        )
    }
} else this
