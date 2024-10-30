package com.mozhimen.composek.runtime

import android.os.Looper

/**
 * @ClassName ActualAndroid
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/20 0:24
 * @Version 1.0
 */
internal val MainThreadId: Long =
    try {
        Looper.getMainLooper().thread.id
    } catch (e: Exception) {
        // When linked against Android SDK stubs and running host-side tests, APIs such as
        // Looper.getMainLooper() can throw or return null
        // This branch intercepts that exception and returns default value for such cases.
        -1
    }
