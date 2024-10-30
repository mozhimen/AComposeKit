package com.mozhimen.composek.ui.draganddrop

import android.content.ClipData
import android.view.DragEvent
import android.view.View
import androidx.compose.ui.geometry.Offset

/**
 * @ClassName DragAndDrop
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:34
 * @Version 1.0
 */
/**
 * [DragAndDropTransferData] representation for the Android platform.
 * It provides the [ClipData] required for drag and drop.
 */
/*actual*/ class DragAndDropTransferData(
    /**
     * The [ClipData] being transferred.
     */
    val clipData: ClipData,
    /**
     * Optional local state for the DnD operation
     * @see [View.startDragAndDrop]
     */
    val localState: Any? = null,
    /**
     * Flags for the drag and drop operation.
     * @see [View.startDragAndDrop]
     */
    val flags: Int = 0,
)

/**
 * Android [DragAndDropEvent] which delegates to a [DragEvent]
 */
/*actual*/ class DragAndDropEvent(
    internal val dragEvent: DragEvent,
)

/**
 * Returns the backing [DragEvent] to read platform specific data
 */
fun DragAndDropEvent.toAndroidDragEvent(): DragEvent = this.dragEvent

/**
 * The mime types present in a [DragAndDropEvent]
 */
// TODO (TJ) make this expect/actual when desktop implements
fun DragAndDropEvent.mimeTypes(): Set<String> {
    val clipDescription = dragEvent.clipDescription ?: return emptySet()
    return buildSet(clipDescription.mimeTypeCount) {
        for (i in 0 until clipDescription.mimeTypeCount) {
            add(clipDescription.getMimeType(i))
        }
    }
}

internal /*actual*/ val DragAndDropEvent.positionInRoot: Offset
    get() = Offset(
        x = dragEvent.x,
        y = dragEvent.y
    )
