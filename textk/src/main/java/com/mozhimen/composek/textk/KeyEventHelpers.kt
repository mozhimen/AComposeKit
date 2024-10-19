package com.mozhimen.composek.textk

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.type

/**
 * @ClassName KeyEventHelpers
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
internal fun KeyEvent.cancelsTextSelection(): Boolean {
    return nativeKeyEvent.keyCode == NativeKeyEvent.KEYCODE_BACK && type == KeyEventType.KeyUp
}

// It's platform-specific behavior, Android doesn't have such a concept
internal fun showCharacterPalette() { }