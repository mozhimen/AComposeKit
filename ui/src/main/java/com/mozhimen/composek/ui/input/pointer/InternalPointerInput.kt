package com.mozhimen.composek.ui.input.pointer

import androidx.collection.LongSparseArray
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset

/**
 * @ClassName InternalPointerInput
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:22
 * @Version 1.0
 */
/**
 * The normalized data structure for pointer input event information that is taken in processed by
 * Compose (via the [PointerInputEventProcessor]).
 *
 * All pointer locations are relative to the device screen.
 */
//@InternalCoreApi
//internal expect class PointerInputEvent {
//    val uptime: Long
//    val pointers: List<PointerInputEventData>
//}

/**
 * Data that describes a particular pointer
 *
 * @param positionOnScreen The position of the event relative to the device screen.
 * @param position The position of the event relative to the owner.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal data class PointerInputEventData(
    val id: PointerId,
    val uptime: Long,
    val positionOnScreen: Offset,
    val position: Offset,
    val down: Boolean,
    val pressure: Float,
    val type: PointerType,
    val issuesEnterExit: Boolean = false,
    val historical: List<HistoricalChange> = mutableListOf(),
    val scrollDelta: Offset = Offset.Zero,
    val originalEventPosition: Offset = Offset.Zero,
)

/**
 * Represents a pointer input event internally.
 *
 * [PointerInputChange]s are stored in a map so that as this internal event traverses the tree,
 * it is efficient to split the changes between those that are relevant to the sub tree and those
 * that are not.
 */
//@OptIn(InternalCoreApi::class)
//internal expect class InternalPointerEvent(
//    changes: LongSparseArray<PointerInputChange>,
//    pointerInputEvent: PointerInputEvent
//) {
//    val changes: LongSparseArray<PointerInputChange>
//
//    /**
//     * Embedded Android Views may consume an event and [ProcessResult] should not
//     * return that the position change was consumed because of this.
//     */
//    var suppressMovementConsumption: Boolean
//    fun issuesEnterExitEvent(pointerId: PointerId): Boolean
//}