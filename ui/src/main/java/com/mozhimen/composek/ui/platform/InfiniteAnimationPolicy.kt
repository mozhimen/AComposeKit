package com.mozhimen.composek.ui.platform

import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.platform.InfiniteAnimationPolicy
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * @ClassName InfiniteAnimationPolicy
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/9
 * @Version 1.0
 */
/**
 * Like [withFrameNanos], but applies the [InfiniteAnimationPolicy] from the calling
 * [CoroutineContext] if there is one.
 *
 * Note that this is an exact copy of the implementation in the `animation-core` module. We need
 * access to it in this module, but other changes are being considered to this API so we don't want
 * to go moving APIs around now if we might change them anyway. b/230369229 tracks cleaning up this
 * clipboard inheritance.
 */
suspend fun <R> withInfiniteAnimationFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R =
    when (val policy = coroutineContext[InfiniteAnimationPolicy]) {
        null -> withFrameNanos(onFrame)
        else -> policy.onInfiniteOperation { withFrameNanos(onFrame) }
    }
