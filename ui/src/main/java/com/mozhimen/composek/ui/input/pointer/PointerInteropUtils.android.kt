package com.mozhimen.composek.ui.input.pointer

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import androidx.compose.ui.geometry.Offset

/**
 * @ClassName PointerInteropUtils
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:12
 * @Version 1.0
 */
/**
 * Converts to a [MotionEvent] and runs [block] with it.
 *
 * @param offset The offset to be applied to the resulting [MotionEvent].
 * @param block The block to be executed with the resulting [MotionEvent].
 */
internal fun PointerEvent.toMotionEventScope(
    offset: Offset,
    block: (MotionEvent) -> Unit
) {
    toMotionEventScope(offset, block, false)
}

/**
 * Converts to an [MotionEvent.ACTION_CANCEL] [MotionEvent] and runs [block] with it.
 *
 * @param offset The offset to be applied to the resulting [MotionEvent].
 * @param block The block to be executed with the resulting [MotionEvent].
 */
internal fun PointerEvent.toCancelMotionEventScope(
    offset: Offset,
    block: (MotionEvent) -> Unit
) {
    toMotionEventScope(offset, block, true)
}

internal fun emptyCancelMotionEventScope(
    nowMillis: Long = SystemClock.uptimeMillis(),
    block: (MotionEvent) -> Unit
) {
    // Does what ViewGroup does when it needs to send a minimal ACTION_CANCEL event.
    val motionEvent =
        MotionEvent.obtain(nowMillis, nowMillis, ACTION_CANCEL, 0.0f, 0.0f, 0)
    motionEvent.source = InputDevice.SOURCE_UNKNOWN
    block(motionEvent)
    motionEvent.recycle()
}

private fun PointerEvent.toMotionEventScope(
    offset: Offset,
    block: (MotionEvent) -> Unit,
    cancel: Boolean
) {
    val motionEvent = motionEvent
    requireNotNull(motionEvent) {
        "The PointerEvent receiver cannot have a null MotionEvent."
    }

    motionEvent.apply {
        val oldAction = action
        if (cancel) {
            action = ACTION_CANCEL
        }

        offsetLocation(-offset.x, -offset.y)

        block(this)

        offsetLocation(offset.x, offset.y)

        action = oldAction
    }
}
