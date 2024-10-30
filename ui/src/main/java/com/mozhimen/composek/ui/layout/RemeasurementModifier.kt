package com.mozhimen.composek.ui.layout

import com.mozhimen.composek.ui.Modifier


/**
 * @ClassName RemeasurementModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:32
 * @Version 1.0
 */
/**
 * A [Modifier.Element] that provides a [Remeasurement] object associated with the layout node
 * the modifier is applied to.
 */
//@JvmDefaultWithCompatibility
interface RemeasurementModifier : Modifier.Element {
    /**
     * This method is executed when the modifier is attached to the layout node.
     *
     * @param remeasurement [Remeasurement] object associated with the layout node the modifier is
     * applied to.
     */
    fun onRemeasurementAvailable(remeasurement: Remeasurement)
}

/**
 * This object is associated with a layout node and allows to execute some extra measure/layout
 * actions which are needed for some complex layouts. In most cases you don't need it as
 * measuring and layout should be correctly working automatically for most cases.
 */
interface Remeasurement {
    /**
     * Performs the node remeasuring synchronously even if the node was not marked as needs
     * remeasure before. Useful for cases like when during scrolling you need to re-execute the
     * measure block to consume the scroll offset and remeasure your children in a blocking way.
     */
    fun forceRemeasure()
}
