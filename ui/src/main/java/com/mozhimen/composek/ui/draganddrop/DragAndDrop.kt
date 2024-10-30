package com.mozhimen.composek.ui.draganddrop

import androidx.compose.ui.geometry.Offset

/**
 * @ClassName DragAndDrop
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:37
 * @Version 1.0
 */
/**
 * Definition for a type representing transferable data. It could be a remote URI,
 * rich text data on the clip board, a local file, or more.
 */
//expect class DragAndDropTransferData

/**
 * A representation of an event sent by the platform during a drag and drop operation.
 */
//expect class DragAndDropEvent

/**
 * Returns the position of this [DragAndDropEvent] relative to the root Compose View in the
 * layout hierarchy.
 */
//internal expect val DragAndDropEvent.positionInRoot: Offset

/**
 * Provides a means of receiving a transfer data from a drag and drop session.
 */
interface DragAndDropTarget {

    /**
     * An item has been dropped inside this [DragAndDropTarget].
     *
     * @return true to indicate that the [DragAndDropEvent] was consumed; false indicates it was
     * rejected.
     */
    fun onDrop(event: DragAndDropEvent): Boolean

    /** A drag and drop session has just been started and this [DragAndDropTarget] is eligible
     * to receive it. This gives an opportunity to set the state for a [DragAndDropTarget] in
     * preparation for consuming a drag and drop session.
     */
    fun onStarted(event: DragAndDropEvent) = Unit

    /**
     * An item being dropped has entered into the bounds of this [DragAndDropTarget].
     */
    fun onEntered(event: DragAndDropEvent) = Unit

    /**
     * An item being dropped has moved within the bounds of this [DragAndDropTarget].
     */
    fun onMoved(event: DragAndDropEvent) = Unit

    /**
     * An item being dropped has moved outside the bounds of this [DragAndDropTarget].
     */
    fun onExited(event: DragAndDropEvent) = Unit

    /**
     * An event in the current drag and drop session has changed within this [DragAndDropTarget]
     * bounds. Perhaps a modifier key has been pressed or released.
     */
    fun onChanged(event: DragAndDropEvent) = Unit

    /**
     * The drag and drop session has been completed. All [DragAndDropTarget] instances in the
     * hierarchy that previously received an [onStarted] event will receive this event. This gives
     * an opportunity to reset the state for a [DragAndDropTarget].
     */
    fun onEnded(event: DragAndDropEvent) = Unit
}
