package com.mozhimen.composek.ui.text.input

import android.view.KeyEvent
import androidx.compose.ui.text.input.EditCommand
import androidx.compose.ui.text.input.ImeAction

/**
 * @ClassName InputEventCallback2
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * An interface of listening IME events.
 */
internal interface InputEventCallback2 {
    /**
     * Called when IME sends some input events.
     *
     * @param editCommands The list of edit commands.
     */
    fun onEditCommands(editCommands: List<EditCommand>)

    /**
     * Called when IME triggered IME action.
     *
     * @param imeAction An IME action.
     */
    fun onImeAction(imeAction: ImeAction)

    /**
     * Called when IME triggered a KeyEvent
     */
    fun onKeyEvent(event: KeyEvent)

    /**
     * Called when IME requests cursor information updates.
     *
     * @see CursorAnchorInfoController.requestUpdate
     */
    fun onRequestCursorAnchorInfo(
        immediate: Boolean,
        monitor: Boolean,
        includeInsertionMarker: Boolean,
        includeCharacterBounds: Boolean,
        includeEditorBounds: Boolean,
        includeLineBounds: Boolean
    )

    /**
     * Called when IME closed the input connection.
     *
     * @param inputConnection a closed input connection
     */
    fun onConnectionClosed(inputConnection: RecordingInputConnection)
}
