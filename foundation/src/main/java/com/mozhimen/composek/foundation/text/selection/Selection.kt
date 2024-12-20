package com.mozhimen.composek.foundation.text.selection

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.ResolvedTextDirection

/**
 * @ClassName Selection
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

/**
 * Information about the current Selection.
 */
@Immutable
internal data class Selection(
    /**
     * Information about the start of the selection.
     */
    val start: AnchorInfo,

    /**
     * Information about the end of the selection.
     */
    val end: AnchorInfo,
    /**
     * The flag to show that the selection handles are dragged across each other. After selection
     * is initialized, if user drags one handle to cross the other handle, this is true, otherwise
     * it's false.
     */
    // If selection happens in single widget, checking [TextRange.start] > [TextRange.end] is
    // enough.
    // But when selection happens across multiple widgets, this value needs more complicated
    // calculation. To avoid repeated calculation, making it as a flag is cheaper.
    val handlesCrossed: Boolean = false
) {
    /**
     * Contains information about an anchor (start/end) of selection.
     */
    @Immutable
    internal data class AnchorInfo(
        /**
         * Text direction of the character in selection edge.
         */
        val direction: ResolvedTextDirection,

        /**
         * Character offset for the selection edge. This offset is within individual child text
         * composable.
         */
        val offset: Int,

        /**
         * The id of the [Selectable] which contains this [Selection] Anchor.
         */
        val selectableId: Long
    )

    fun merge(other: Selection?): Selection {
        if (other == null) return this

        val selection = this

        return if (handlesCrossed || other.handlesCrossed) {
            Selection(
                start = if (other.handlesCrossed) other.start else other.end,
                end = if (handlesCrossed) end else start,
                handlesCrossed = true
            )
        } else {
            selection.copy(end = other.end)
        }
    }

    /**
     * Returns the selection offset information as a [TextRange]
     */
    fun toTextRange(): TextRange {
        return TextRange(start.offset, end.offset)
    }
}
