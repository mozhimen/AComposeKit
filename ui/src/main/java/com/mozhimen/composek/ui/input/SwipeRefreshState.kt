package com.mozhimen.composek.ui.input

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope

/**
 * @ClassName SwipeRefreshState
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/22
 * @Version 1.0
 */
@Stable
class SwipeRefreshState(coroutineScope: CoroutineScope) : OverScrollState(coroutineScope) {
    var headerHeight = 0f
    var enableRefresh = true
    var isRefreshing by mutableStateOf(false)
    var animateIsOver by mutableStateOf(true)

    ////////////////////////////////////////////////////////////////////////////////////////

    fun isLoading() = !animateIsOver || isRefreshing

    ////////////////////////////////////////////////////////////////////////////////////////

    suspend fun animateOffsetTo(offset: Float) {
        animateOffsetTo(offset, AnimationConstants.DefaultDurationMillis)
    }

    override suspend fun animateOffsetTo(offset: Float, durationMillis: Int) {
        _mutatorMutex.mutate {
            _offset.animateTo(offset, animationSpec = tween(durationMillis)) {
                if (this.value == 0f) {
                    animateIsOver = true
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        return when {
            enableRefresh && available.y > 0 -> {
                val canConsumed = available.y * 0.5f
                safeSnapOffsetTo(canConsumed)
                if (source == NestedScrollSource.Fling && offset > headerHeight) {
                    throw CancellationException()
                }
                available.copy(x = 0f, y = canConsumed / 0.5f)
            }

            else -> Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (offset >= headerHeight) {
            if (!isLoading()) {
                isRefreshing = true
                animateOffsetTo(headerHeight)
                return available
            }
        }
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (offset > 0) {
            if (isRefreshing && offset > headerHeight) {
                animateOffsetTo(headerHeight)
            } else if (!isRefreshing) {
                animateOffsetTo(0f)
            }
            return available
        }
        return Velocity.Zero
    }
}

@Composable
fun rememberSwipeRefreshState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): SwipeRefreshState {
    return remember { SwipeRefreshState(coroutineScope) }
}
