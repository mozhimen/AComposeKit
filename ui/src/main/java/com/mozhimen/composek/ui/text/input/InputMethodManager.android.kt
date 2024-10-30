package com.mozhimen.composek.ui.text.input

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.ExtractedText
import androidx.core.view.SoftwareKeyboardControllerCompat

/**
 * @ClassName InputMethodManager
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal interface InputMethodManager {
    fun isActive(): Boolean

    fun restartInput()

    fun showSoftInput()

    fun hideSoftInput()

    fun updateExtractedText(
        token: Int,
        extractedText: ExtractedText
    )

    fun updateSelection(
        selectionStart: Int,
        selectionEnd: Int,
        compositionStart: Int,
        compositionEnd: Int
    )

    fun updateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo)
}

/**
 * Wrapper class to prevent depending on getSystemService and final InputMethodManager.
 * Let's us test TextInputServiceAndroid class.
 */
internal class InputMethodManagerImpl(private val view: View) : InputMethodManager {

    private val imm by lazy(LazyThreadSafetyMode.NONE) {
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
    }

    private val softwareKeyboardControllerCompat =
        SoftwareKeyboardControllerCompat(view)

    override fun isActive(): Boolean = imm.isActive(view)

    override fun restartInput() {
        imm.restartInput(view)
    }

    override fun showSoftInput() {
        if (DEBUG && !view.hasWindowFocus()) {
            Log.d(TAG, "InputMethodManagerImpl: requesting soft input on non focused field")
        }

        softwareKeyboardControllerCompat.show()
    }

    override fun hideSoftInput() {
        softwareKeyboardControllerCompat.hide()
    }

    override fun updateExtractedText(
        token: Int,
        extractedText: ExtractedText
    ) {
        imm.updateExtractedText(view, token, extractedText)
    }

    override fun updateSelection(
        selectionStart: Int,
        selectionEnd: Int,
        compositionStart: Int,
        compositionEnd: Int
    ) {
        imm.updateSelection(view, selectionStart, selectionEnd, compositionStart, compositionEnd)
    }

    override fun updateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo) {
        imm.updateCursorAnchorInfo(view, cursorAnchorInfo)
    }
}
