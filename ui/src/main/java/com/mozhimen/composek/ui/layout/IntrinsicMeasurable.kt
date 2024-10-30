package com.mozhimen.composek.ui.layout

/**
 * @ClassName IntrinsicMeasurable
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:20
 * @Version 1.0
 */
/**
 * A part of the composition that can be measured. This represents a layout.
 * The instance should never be stored.
 */
interface IntrinsicMeasurable {
    /**
     * Data provided by the [ParentDataModifier].
     */
    val parentData: Any?

    /**
     * Calculates the minimum width that the layout can be such that
     * the content of the layout will be painted correctly.
     */
    fun minIntrinsicWidth(height: Int): Int

    /**
     * Calculates the smallest width beyond which increasing the width never
     * decreases the height.
     */
    fun maxIntrinsicWidth(height: Int): Int

    /**
     * Calculates the minimum height that the layout can be such that
     * the content of the layout will be painted correctly.
     */
    fun minIntrinsicHeight(width: Int): Int

    /**
     * Calculates the smallest height beyond which increasing the height never
     * decreases the width.
     */
    fun maxIntrinsicHeight(width: Int): Int
}
