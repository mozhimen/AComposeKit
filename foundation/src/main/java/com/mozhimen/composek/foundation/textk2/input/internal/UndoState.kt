package com.mozhimen.composek.foundation.textk2.input.internal

import androidx.compose.foundation.ExperimentalFoundationApi
import com.mozhimen.composek.textk2.input.TextFieldState

/**
 * @ClassName UndoState
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

/**
 * Defines an interactable undo history.
 */
@ExperimentalFoundationApi
class UndoState internal constructor(private val state: TextFieldState) {

    /**
     * Whether it is possible to execute a meaningful undo action right now. If this value is false,
     * calling `undo` would be a no-op.
     */
    @Suppress("GetterSetterNames")
    @get:Suppress("GetterSetterNames")
    val canUndo: Boolean
        get() = state.textUndoManager.canUndo

    /**
     * Whether it is possible to execute a meaningful redo action right now. If this value is false,
     * calling `redo` would be a no-op.
     */
    @Suppress("GetterSetterNames")
    @get:Suppress("GetterSetterNames")
    val canRedo: Boolean
        get() = state.textUndoManager.canRedo

    /**
     * Reverts the latest edit action or a group of actions that are merged together. Calling it
     * repeatedly can continue undoing the previous actions.
     */
    fun undo() {
        state.textUndoManager.undo(state)
    }

    /**
     * Re-applies a change that was previously reverted via [undo].
     */
    fun redo() {
        state.textUndoManager.redo(state)
    }

    /**
     * Clears all undo and redo history up to this point.
     */
    fun clearHistory() {
        state.textUndoManager.clearHistory()
    }
}
