package com.mozhimen.composek.ui.input

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi

/**
 * @ClassName InputModeManager
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The [InputModeManager] is accessible as a CompositionLocal, that provides the current
 *  [InputMode].
 */
interface InputModeManager {
    /**
     * The current [InputMode].
     */
    val inputMode: InputMode

    /**
     * Send a request to change the [InputMode].
     *
     * @param inputMode The requested [InputMode].
     * @return true if the system is in the requested mode, after processing this request.
     */
    @ExperimentalComposeUiApi
    fun requestInputMode(inputMode: InputMode): Boolean
}

/**
 * This value is used to represent the InputMode that the system is currently in.
 */
@kotlin.jvm.JvmInline
value class InputMode internal constructor(@Suppress("unused") private val value: Int) {
    override fun toString() = when (this) {
        Touch -> "Touch"
        Keyboard -> "Keyboard"
        else -> "Error"
    }

    companion object {
        /**
         * The system is put into [Touch] mode when a user touches the screen.
         */
        val Touch = InputMode(1)

        /**
         * The system is put into [Keyboard] mode when a user presses a hardware key.
         */
        val Keyboard = InputMode(2)
    }
}

internal class InputModeManagerImpl(
    initialInputMode: InputMode,
    private val onRequestInputModeChange: (InputMode) -> Boolean
) : InputModeManager {
    override var inputMode: InputMode by mutableStateOf(initialInputMode)

    @ExperimentalComposeUiApi
    override fun requestInputMode(inputMode: InputMode) = onRequestInputModeChange.invoke(inputMode)
}
