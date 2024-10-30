package com.mozhimen.composek.ui.node

import androidx.compose.ui.viewinterop.InteropView

/**
 * @ClassName InteroperableComposeUiNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:34
 * @Version 1.0
 */
/**
 * This interface allows the layout inspector to access inspect instances of Views that are
 * associated with a Compose UI node. Usage of this API outside of Compose UI artifacts is
 * unsupported.
 */
//@InternalComposeUiApi
sealed interface InteroperableComposeUiNode {
    fun getInteropView(): InteropView?
}
