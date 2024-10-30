package com.mozhimen.composek.foundation.text.selection

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.foundation.text.Handle

/**
 * @ClassName SelectionHandles
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
internal val HandleWidth = 25.dp
internal val HandleHeight = 25.dp

/**
 * [SelectionHandleInfo]s for the nodes representing selection handles. These nodes are in popup
 * windows, and will respond to drag gestures.
 */
internal val SelectionHandleInfoKey =
    SemanticsPropertyKey<SelectionHandleInfo>("SelectionHandleInfo")

/**
 * Information about a single selection handle popup.
 *
 * @param handle Which selection [Handle] this is about.
 * @param position The position that the handle is anchored to relative to the selectable content.
 * This position is not necessarily the position of the popup itself, it's the position that the
 * handle "points" to (so e.g. top-middle for [Handle.Cursor]).
 * @param anchor How the selection handle is anchored to its position
 * @param visible Whether the icon of the handle is actually shown
 */
internal data class SelectionHandleInfo(
    val handle: Handle,
    val position: Offset,
    val anchor: SelectionHandleAnchor,
    val visible: Boolean,
)

/**
 * How the selection handle is anchored to its position
 *
 * In a regular text selection, selection start is anchored to left.
 * Only cursor handle is always anchored at the middle.
 * In a regular text selection, selection end is anchored to right.
 */
internal enum class SelectionHandleAnchor {
    Left,
    Middle,
    Right
}

/**
 * Avoids boxing of [Offset] which is an inline value class.
 */
internal fun interface OffsetProvider {
    fun provide(): Offset
}

/**
 * Adjust coordinates for given text offset.
 *
 * Currently [android.text.Layout.getLineBottom] returns y coordinates of the next
 * line's top offset, which is not included in current line's hit area. To be able to
 * hit current line, move up this y coordinates by 1 pixel.
 */
internal fun getAdjustedCoordinates(position: Offset): Offset {
    return Offset(position.x, position.y - 1f)
}
