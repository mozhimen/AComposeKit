package com.mozhimen.composek.basic.foundation.gestures

import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * @ClassName TapGestureDetector
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
private val NoPressGesture: suspend PressGestureScope.(Offset) -> Unit = { }

/**
 * Shortcut for cases when we only need to get press/click logic, as for cases without long press
 * and double click we don't require channelling or any other complications.
 *
 * Each function parameter receives an [Offset] representing the position relative to the containing
 * element. The [Offset] can be outside the actual bounds of the element itself meaning the numbers
 * can be negative or larger than the element bounds if the touch target is smaller than the
 * [ViewConfiguration.minimumTouchTargetSize].
 */
suspend fun PointerInputScope.detectTapAndPress(
    onPress: suspend PressGestureScope.(Offset) -> Unit = NoPressGesture,
    onTap: ((Offset) -> Unit)? = null
) {
    val pressScope = PressGestureScopeImpl(this)
    coroutineScope {
        awaitEachGesture {
            launch {
                pressScope.reset()
            }

            val down = awaitFirstDown().also { it.consume() }

            if (onPress !== NoPressGesture) {
                launch {
                    pressScope.onPress(down.position)
                }
            }

            val up = waitForUpOrCancellation()
            if (up == null) {
                launch {
                    pressScope.cancel() // tap-up was canceled
                }
            } else {
                up.consume()
                launch {
                    pressScope.release()
                }
                onTap?.invoke(up.position)
            }
        }
    }
}

/**
 * [detectTapGestures]'s implementation of [PressGestureScope].
 */
private class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {
    private var isReleased = false
    private var isCanceled = false
    private val mutex = Mutex(locked = false)

    /**
     * Called when a gesture has been canceled.
     */
    fun cancel() {
        isCanceled = true
        mutex.unlock()
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        mutex.unlock()
    }

    /**
     * Called when a new gesture has started.
     */
    suspend fun reset() {
        mutex.lock()
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (!tryAwaitRelease()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {
        if (!isReleased && !isCanceled) {
            mutex.lock()
            mutex.unlock()
        }
        return isReleased
    }
}
