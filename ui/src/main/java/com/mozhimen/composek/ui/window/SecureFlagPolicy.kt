package com.mozhimen.composek.ui.window

import androidx.compose.ui.window.SecureFlagPolicy

/**
 * @ClassName SecureFlagPolicy
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/9
 * @Version 1.0
 */
internal fun SecureFlagPolicy.shouldApplySecureFlag(isSecureFlagSetOnParent: Boolean): Boolean {
    return when (this) {
        SecureFlagPolicy.SecureOff -> false
        SecureFlagPolicy.SecureOn -> true
        SecureFlagPolicy.Inherit -> isSecureFlagSetOnParent
    }
}
