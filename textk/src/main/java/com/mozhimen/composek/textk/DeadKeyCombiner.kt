package com.mozhimen.composek.textk

import android.view.KeyCharacterMap
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.utf16CodePoint

/**
 * @ClassName DeadKeyCombiner
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
internal class DeadKeyCombiner {

    private var deadKeyCode: Int? = null

    fun consume(event: KeyEvent): Int? {
        val codePoint = event.utf16CodePoint
        if (codePoint and KeyCharacterMap.COMBINING_ACCENT != 0) {
            deadKeyCode = codePoint and KeyCharacterMap.COMBINING_ACCENT_MASK
            return null
        }

        val localDeadKeyCode = deadKeyCode
        if (localDeadKeyCode != null) {
            deadKeyCode = null
            return KeyCharacterMap.getDeadChar(localDeadKeyCode, codePoint)
                // if the combo doesn't exist, fall back to the current key press
                .takeUnless { it == 0 } ?: codePoint
        }

        return codePoint
    }
}
