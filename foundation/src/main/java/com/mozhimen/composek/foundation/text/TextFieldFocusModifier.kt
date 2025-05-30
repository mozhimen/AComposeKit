package com.mozhimen.composek.foundation.text

import android.view.InputDevice.SOURCE_DPAD
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection.Companion.Down
import androidx.compose.ui.focus.FocusDirection.Companion.Left
import androidx.compose.ui.focus.FocusDirection.Companion.Right
import androidx.compose.ui.focus.FocusDirection.Companion.Up
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

/**
 * @ClassName TextFieldFocusModifier
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
/**
 * TextField consumes the D-pad keys, due to which we can't move focus once a TextField is focused.
 * To prevent this, this modifier can be used to intercept D-pad key events before they are sent to
 * the TextField. It intercepts and handles the directional (Up, Down, Left, Right & Center) D-pad
 * key presses, to move the focus between TextField and other focusable items on the screen.
 *
 * NOTE: Key events from non-dpad sources or virtual keyboards are ignored.
 */
internal fun Modifier.interceptDPadAndMoveFocus(
    state: TextFieldState,
    focusManager: FocusManager
): Modifier {
    return this
        .onPreviewKeyEvent { keyEvent ->
            val device = keyEvent.nativeKeyEvent.device
            when {
                device == null -> false

                // Ignore key events from non-dpad sources
                !device.supportsSource(SOURCE_DPAD) -> false

                // Ignore key events from virtual keyboards
                device.isVirtual -> false

                // Ignore key release events
                keyEvent.type != KeyDown -> false

                keyEvent.isKeyCode(KEYCODE_DPAD_UP) -> focusManager.moveFocus(Up)
                keyEvent.isKeyCode(KEYCODE_DPAD_DOWN) -> focusManager.moveFocus(Down)
                keyEvent.isKeyCode(KEYCODE_DPAD_LEFT) -> focusManager.moveFocus(Left)
                keyEvent.isKeyCode(KEYCODE_DPAD_RIGHT) -> focusManager.moveFocus(Right)
                keyEvent.isKeyCode(KEYCODE_DPAD_CENTER) -> {
                    // Enable keyboard on center key press
                    state.keyboardController?.show()
                    true
                }
                else -> false
            }
        }
}

private fun KeyEvent.isKeyCode(keyCode: Int): Boolean =
    this.key.nativeKeyCode == keyCode