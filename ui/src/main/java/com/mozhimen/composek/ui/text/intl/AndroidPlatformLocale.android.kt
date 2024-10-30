package com.mozhimen.composek.ui.text.intl

import android.os.Build

/**
 * @ClassName AndroidPlatformLocale
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal /*actual*/ fun createPlatformLocaleDelegate() =
    if (Build.VERSION.SDK_INT >= 24) {
        AndroidLocaleDelegateAPI24()
    } else {
        AndroidLocaleDelegateAPI23()
    }
