package com.mozhimen.composek.ui.platform

import androidx.compose.runtime.staticCompositionLocalOf
import com.mozhimen.composek.ui.input.pointer.PointerIconService

/**
 * @ClassName CompositionLocals
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:43
 * @Version 1.0
 */
internal val LocalPointerIconService = staticCompositionLocalOf<PointerIconService?> {
    null
}