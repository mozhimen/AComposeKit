package com.mozhimen.composek.ui.platform

import android.view.View

/**
 * @ClassName PlatformTextInputSession
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/*actual*/ interface PlatformTextInputSession {
    /**
     * The [View] this input session is bound to. This view should be used to obtain and interact
     * with the [InputMethodManager].
     */
    val view: View

    /*actual*/ suspend fun startInputMethod(request: PlatformTextInputMethodRequest): Nothing
}
