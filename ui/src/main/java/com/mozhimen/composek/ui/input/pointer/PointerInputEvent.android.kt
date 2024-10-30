package com.mozhimen.composek.ui.input.pointer

import android.view.MotionEvent

/**
 * @ClassName PointerInputEvent
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:21
 * @Version 1.0
 */
internal /*actual*/ class PointerInputEvent(
    /*actual*/ val uptime: Long,
    /*actual*/ val pointers: List<PointerInputEventData>,
    val motionEvent: MotionEvent
)
