package com.mozhimen.composek.ui.hapticfeedback

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * @ClassName PlatformHapticFeedback
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Android implementation for [HapticFeedback]
 */
internal class PlatformHapticFeedback(private val view: View) :
    HapticFeedback {

    override fun performHapticFeedback(
        hapticFeedbackType: HapticFeedbackType
    ) {
        when (hapticFeedbackType) {
            HapticFeedbackType.LongPress ->
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            HapticFeedbackType.TextHandleMove ->
                view.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
        }
    }
}

internal /*actual*/ object PlatformHapticFeedbackType {
    /*actual*/ val LongPress: HapticFeedbackType = HapticFeedbackType(
        HapticFeedbackConstants.LONG_PRESS
    )
    /*actual*/ val TextHandleMove: HapticFeedbackType =
        HapticFeedbackType(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
}
