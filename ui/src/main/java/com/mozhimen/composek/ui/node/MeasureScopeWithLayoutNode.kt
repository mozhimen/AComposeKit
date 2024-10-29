package com.mozhimen.composek.ui.node

import com.mozhimen.composek.ui.layout.MeasureScope


/**
 * @ClassName MeasureScopeWithLayoutNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal interface MeasureScopeWithLayoutNode : MeasureScope {
    val layoutNode: LayoutNode
}
