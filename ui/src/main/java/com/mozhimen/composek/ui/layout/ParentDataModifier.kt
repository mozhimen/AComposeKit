package com.mozhimen.composek.ui.layout

import com.mozhimen.composek.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Density

/**
 * @ClassName ParentDataModifier
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * A [Modifier] that provides data to the parent [Layout]. This can be read from within the
 * the [Layout] during measurement and positioning, via [IntrinsicMeasurable.parentData].
 * The parent data is commonly used to inform the parent how the child [Layout] should be measured
 * and positioned.
 */
//@JvmDefaultWithCompatibility
interface ParentDataModifier : Modifier.Element {
    /**
     * Provides a parentData, given the [parentData] already provided through the modifier's chain.
     */
    fun Density.modifyParentData(parentData: Any?): Any?
}
