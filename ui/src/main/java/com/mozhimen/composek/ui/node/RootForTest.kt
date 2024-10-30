package com.mozhimen.composek.ui.node

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.semantics.SemanticsOwner
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.Density

/**
 * @ClassName RootForTest
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:00
 * @Version 1.0
 */
/**
 * The marker interface to be implemented by the root backing the composition.
 * To be used in tests.
 */
interface RootForTest {
    /**
     * Current device density.
     */
    val density: Density

    /**
     * Semantics owner for this root. Manages all the semantics nodes.
     */
    val semanticsOwner: SemanticsOwner

    /**
     * The service handling text input.
     */
    val textInputService: TextInputService

    /**
     * Send this [KeyEvent] to the focused component in this [Owner].
     *
     * @return true if the event was consumed. False otherwise.
     */
    fun sendKeyEvent(keyEvent: KeyEvent): Boolean

    /**
     * Requests another layout (measure + placement) pass be performed for any nodes that need it.
     * This doesn't force anything to be remeasured that wouldn't be if `requestLayout` were called.
     * However, unlike `requestLayout`, it doesn't merely _schedule_ another layout pass to be
     * performed, it actually performs it synchronously.
     *
     * This method is used in UI tests to perform layout in between frames when pumping frames as
     * fast as possible (i.e. without waiting for the choreographer to schedule them) in order to
     * get to idle, e.g. during a `waitForIdle` call.
     */
    @ExperimentalComposeUiApi
    fun measureAndLayoutForTest() {}
}
