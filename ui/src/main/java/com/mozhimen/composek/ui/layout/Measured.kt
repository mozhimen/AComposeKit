package com.mozhimen.composek.ui.layout

/**
 * @ClassName Measured
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * A [Measured] corresponds to a layout that has been measured by its parent layout.
 */
interface Measured {
    /**
     * The measured width of the layout. This might not respect the measurement constraints.
     */
    val measuredWidth: Int

    /**
     * The measured height of the layout. This might not respect the measurement constraints.
     */
    val measuredHeight: Int

    /**
     * Data provided by the [ParentDataModifier] applied to the layout.
     */
    val parentData: Any? get() = null

    /**
     * Returns the position of an [alignment line][AlignmentLine],
     * or [AlignmentLine.Unspecified] if the line is not provided.
     */
    operator fun get(alignmentLine: AlignmentLine): Int
}
