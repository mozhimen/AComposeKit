package com.mozhimen.composek.ui.platform

/**
 * @ClassName DebugUtils
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal inline fun ifDebug(block: () -> Unit) {
    // Right now, we always run these.  At a later point, we may revisit this
    block()
}

//internal expect fun simpleIdentityToString(obj: Any, name: String?): String
