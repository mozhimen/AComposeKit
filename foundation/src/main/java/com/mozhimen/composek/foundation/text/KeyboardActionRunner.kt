package com.mozhimen.composek.foundation.text

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Default
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Go
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.ImeAction.Companion.None
import androidx.compose.ui.text.input.ImeAction.Companion.Previous
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.text.input.ImeAction.Companion.Send

/**
 * @ClassName KeyboardActionRunner
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
/**
 * This class can be used to run keyboard actions when the user triggers an IME action.
 */
internal class KeyboardActionRunner(
    private val keyboardController: SoftwareKeyboardController?
) : KeyboardActionScope {

    /**
     * The developer specified [KeyboardActions].
     */
    lateinit var keyboardActions: KeyboardActions

    /**
     * A reference to the [FocusManager] composition local.
     */
    lateinit var focusManager: FocusManager

    /**
     * Run the keyboard action corresponding to the specified imeAction. If a keyboard action is
     * not specified, use the default implementation provided by [defaultKeyboardAction].
     */
    fun runAction(imeAction: ImeAction) {
        val keyboardAction = when (imeAction) {
            Done -> keyboardActions.onDone
            Go -> keyboardActions.onGo
            Next -> keyboardActions.onNext
            Previous -> keyboardActions.onPrevious
            Search -> keyboardActions.onSearch
            Send -> keyboardActions.onSend
            Default, None -> null
            else -> error("invalid ImeAction")
        }
        keyboardAction?.invoke(this) ?: defaultKeyboardAction(imeAction)
    }

    /**
     * Default implementations for [KeyboardActions].
     */
    override fun defaultKeyboardAction(imeAction: ImeAction) {
        when (imeAction) {
            Next -> focusManager.moveFocus(FocusDirection.Next)
            Previous -> focusManager.moveFocus(FocusDirection.Previous)
            Done -> keyboardController?.hide()
            // Note: Don't replace this with an else. These are specified explicitly so that we
            // don't forget to update this when statement when new imeActions are added.
            Go, Search, Send, Default, None -> Unit // Do Nothing.
        }
    }
}