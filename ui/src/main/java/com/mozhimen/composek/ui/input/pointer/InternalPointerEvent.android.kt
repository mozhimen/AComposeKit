package com.mozhimen.composek.ui.input.pointer

import android.view.MotionEvent
import androidx.collection.LongSparseArray
import androidx.compose.ui.node.InternalCoreApi
import androidx.compose.ui.util.fastFirstOrNull

/**
 * @ClassName InternalPointerEvent
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:21
 * @Version 1.0
 */
@OptIn(InternalCoreApi::class)
internal /*actual*/ class InternalPointerEvent /*actual*/ constructor(
    /*actual*/ val changes: LongSparseArray<PointerInputChange>,
    val pointerInputEvent: PointerInputEvent
) {
    val motionEvent: MotionEvent
        get() = pointerInputEvent.motionEvent

    /*actual*/ fun issuesEnterExitEvent(pointerId: PointerId): Boolean =
        pointerInputEvent.pointers.fastFirstOrNull {
            it.id == pointerId
        }?.issuesEnterExit ?: false

    /*actual*/ var suppressMovementConsumption: Boolean = false
}
