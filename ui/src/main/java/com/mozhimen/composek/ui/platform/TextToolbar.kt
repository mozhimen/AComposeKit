package com.mozhimen.composek.ui.platform

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbarStatus

/**
 * @ClassName TextToolbar
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Interface for text-related toolbar.
 */
//@JvmDefaultWithCompatibility
interface TextToolbar {
    /**
     * Show the floating toolbar(post-M) or primary toolbar(pre-M) for copying, cutting and pasting
     * text.
     * @param rect region of interest. The selected region around which the floating toolbar
     * should show. This rect is in global coordinates system.
     * @param onCopyRequested callback to copy text into ClipBoardManager.
     * @param onPasteRequested callback to get text from ClipBoardManager and paste it.
     * @param onCutRequested callback to cut text and copy the text into ClipBoardManager.
     */
    fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)? = null,
        onPasteRequested: (() -> Unit)? = null,
        onCutRequested: (() -> Unit)? = null,
        onSelectAllRequested: (() -> Unit)? = null
    )

    /**
     * Hide the floating toolbar(post-M) or primary toolbar(pre-M).
     */
    fun hide()

    /**
     * Return the [TextToolbarStatus] to check if the toolbar is shown or hidden.
     *
     * @return [TextToolbarStatus] of [TextToolbar].
     */
    val status: TextToolbarStatus
}
