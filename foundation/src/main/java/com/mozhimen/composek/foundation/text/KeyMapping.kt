package com.mozhimen.composek.foundation.text

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key

/**
 * @ClassName KeyMapping
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

internal interface KeyMapping {
    fun map(event: KeyEvent): KeyCommand?
}

// each platform can define its own key mapping, on Android its just defaultKeyMapping, but on
// desktop, the value depends on the current OS
internal val platformDefaultKeyMapping = object : KeyMapping {
    override fun map(event: KeyEvent): KeyCommand? = when {
        event.isShiftPressed && event.isAltPressed ->
            when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.SELECT_LINE_LEFT
                MappedKeys.DirectionRight -> KeyCommand.SELECT_LINE_RIGHT
                MappedKeys.DirectionUp -> KeyCommand.SELECT_HOME
                MappedKeys.DirectionDown -> KeyCommand.SELECT_END
                else -> null
            }

        event.isAltPressed ->
            when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.LINE_LEFT
                MappedKeys.DirectionRight -> KeyCommand.LINE_RIGHT
                MappedKeys.DirectionUp -> KeyCommand.HOME
                MappedKeys.DirectionDown -> KeyCommand.END
                else -> null
            }

        else -> null
    } ?: defaultKeyMapping.map(event)
}

/**
 * Copied from [Key] as the constants there are experimental
 */
internal object MappedKeys {
    val A: Key = Key(android.view.KeyEvent.KEYCODE_A)
    val C: Key = Key(android.view.KeyEvent.KEYCODE_C)
    val H: Key = Key(android.view.KeyEvent.KEYCODE_H)
    val V: Key = Key(android.view.KeyEvent.KEYCODE_V)
    val Y: Key = Key(android.view.KeyEvent.KEYCODE_Y)
    val X: Key = Key(android.view.KeyEvent.KEYCODE_X)
    val Z: Key = Key(android.view.KeyEvent.KEYCODE_Z)
    val Backslash: Key = Key(android.view.KeyEvent.KEYCODE_BACKSLASH)
    val DirectionLeft: Key = Key(android.view.KeyEvent.KEYCODE_DPAD_LEFT)
    val DirectionRight: Key = Key(android.view.KeyEvent.KEYCODE_DPAD_RIGHT)
    val DirectionUp: Key = Key(android.view.KeyEvent.KEYCODE_DPAD_UP)
    val DirectionDown: Key = Key(android.view.KeyEvent.KEYCODE_DPAD_DOWN)
    val PageUp: Key = Key(android.view.KeyEvent.KEYCODE_PAGE_UP)
    val PageDown: Key = Key(android.view.KeyEvent.KEYCODE_PAGE_DOWN)
    val MoveHome: Key = Key(android.view.KeyEvent.KEYCODE_MOVE_HOME)
    val MoveEnd: Key = Key(android.view.KeyEvent.KEYCODE_MOVE_END)
    val Insert: Key = Key(android.view.KeyEvent.KEYCODE_INSERT)
    val Enter: Key = Key(android.view.KeyEvent.KEYCODE_ENTER)
    val Backspace: Key = Key(android.view.KeyEvent.KEYCODE_DEL)
    val Delete: Key = Key(android.view.KeyEvent.KEYCODE_FORWARD_DEL)
    val Paste: Key = Key(android.view.KeyEvent.KEYCODE_PASTE)
    val Cut: Key = Key(android.view.KeyEvent.KEYCODE_CUT)
    val Copy: Key = Key(android.view.KeyEvent.KEYCODE_COPY)
    val Tab: Key = Key(android.view.KeyEvent.KEYCODE_TAB)
}

// It's common for all platforms key mapping
internal fun commonKeyMapping(
    shortcutModifier: (KeyEvent) -> Boolean
): KeyMapping {
    return object : KeyMapping {
        override fun map(event: KeyEvent): KeyCommand? {
            return when {
                shortcutModifier(event) && event.isShiftPressed ->
                    when (event.key) {
                        MappedKeys.Z -> KeyCommand.REDO
                        else -> null
                    }
                shortcutModifier(event) ->
                    when (event.key) {
                        MappedKeys.C, MappedKeys.Insert -> KeyCommand.COPY
                        MappedKeys.V -> KeyCommand.PASTE
                        MappedKeys.X -> KeyCommand.CUT
                        MappedKeys.A -> KeyCommand.SELECT_ALL
                        MappedKeys.Y -> KeyCommand.REDO
                        MappedKeys.Z -> KeyCommand.UNDO
                        else -> null
                    }
                event.isCtrlPressed -> null
                event.isShiftPressed ->
                    when (event.key) {
                        MappedKeys.DirectionLeft -> KeyCommand.SELECT_LEFT_CHAR
                        MappedKeys.DirectionRight -> KeyCommand.SELECT_RIGHT_CHAR
                        MappedKeys.DirectionUp -> KeyCommand.SELECT_UP
                        MappedKeys.DirectionDown -> KeyCommand.SELECT_DOWN
                        MappedKeys.PageUp -> KeyCommand.SELECT_PAGE_UP
                        MappedKeys.PageDown -> KeyCommand.SELECT_PAGE_DOWN
                        MappedKeys.MoveHome -> KeyCommand.SELECT_LINE_START
                        MappedKeys.MoveEnd -> KeyCommand.SELECT_LINE_END
                        MappedKeys.Insert -> KeyCommand.PASTE
                        else -> null
                    }
                else ->
                    when (event.key) {
                        MappedKeys.DirectionLeft -> KeyCommand.LEFT_CHAR
                        MappedKeys.DirectionRight -> KeyCommand.RIGHT_CHAR
                        MappedKeys.DirectionUp -> KeyCommand.UP
                        MappedKeys.DirectionDown -> KeyCommand.DOWN
                        MappedKeys.PageUp -> KeyCommand.PAGE_UP
                        MappedKeys.PageDown -> KeyCommand.PAGE_DOWN
                        MappedKeys.MoveHome -> KeyCommand.LINE_START
                        MappedKeys.MoveEnd -> KeyCommand.LINE_END
                        MappedKeys.Enter -> KeyCommand.NEW_LINE
                        MappedKeys.Backspace -> KeyCommand.DELETE_PREV_CHAR
                        MappedKeys.Delete -> KeyCommand.DELETE_NEXT_CHAR
                        MappedKeys.Paste -> KeyCommand.PASTE
                        MappedKeys.Cut -> KeyCommand.CUT
                        MappedKeys.Copy -> KeyCommand.COPY
                        MappedKeys.Tab -> KeyCommand.TAB
                        else -> null
                    }
            }
        }
    }
}

// It's "default" or actually "non macOS" key mapping
internal val defaultKeyMapping: KeyMapping =
    commonKeyMapping(KeyEvent::isCtrlPressed).let { common ->
        object : KeyMapping {
            override fun map(event: KeyEvent): KeyCommand? {
                return when {
                    event.isShiftPressed && event.isCtrlPressed ->
                        when (event.key) {
                            MappedKeys.DirectionLeft -> KeyCommand.SELECT_LEFT_WORD
                            MappedKeys.DirectionRight -> KeyCommand.SELECT_RIGHT_WORD
                            MappedKeys.DirectionUp -> KeyCommand.SELECT_PREV_PARAGRAPH
                            MappedKeys.DirectionDown -> KeyCommand.SELECT_NEXT_PARAGRAPH
                            else -> null
                        }
                    event.isCtrlPressed ->
                        when (event.key) {
                            MappedKeys.DirectionLeft -> KeyCommand.LEFT_WORD
                            MappedKeys.DirectionRight -> KeyCommand.RIGHT_WORD
                            MappedKeys.DirectionUp -> KeyCommand.PREV_PARAGRAPH
                            MappedKeys.DirectionDown -> KeyCommand.NEXT_PARAGRAPH
                            MappedKeys.H -> KeyCommand.DELETE_PREV_CHAR
                            MappedKeys.Delete -> KeyCommand.DELETE_NEXT_WORD
                            MappedKeys.Backspace -> KeyCommand.DELETE_PREV_WORD
                            MappedKeys.Backslash -> KeyCommand.DESELECT
                            else -> null
                        }
                    event.isShiftPressed ->
                        when (event.key) {
                            MappedKeys.MoveHome -> KeyCommand.SELECT_LINE_LEFT
                            MappedKeys.MoveEnd -> KeyCommand.SELECT_LINE_RIGHT
                            else -> null
                        }
                    event.isAltPressed ->
                        when (event.key) {
                            MappedKeys.Backspace -> KeyCommand.DELETE_FROM_LINE_START
                            MappedKeys.Delete -> KeyCommand.DELETE_TO_LINE_END
                            else -> null
                        }
                    else -> null
                } ?: common.map(event)
            }
        }
    }
