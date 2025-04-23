package com.mozhimen.composek.ui.input

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @ClassName SwipeRefreshState
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/22
 * @Version 1.0
 */
@Stable
open class OverScrollState constructor(
    protected val _coroutineScope: CoroutineScope,
    offsetSave: Float = 0f,
    protected val _dampingFactor: Float = 0.5f, // 可配置的阻尼系数
) : NestedScrollConnection,IUtilK {
    protected val _offset = Animatable(offsetSave.also { UtilKLogWrapper.d(TAG, "_offset: $it") })
    protected val _mutatorMutex = MutatorMutex()

    ////////////////////////////////////////////////////////////////////////////////////////

    val offset: Float
        get() = _offset.value

    ////////////////////////////////////////////////////////////////////////////////////////

    open suspend fun animateOffsetTo(offset: Float, durationMillis: Int = AnimationConstants.DefaultDurationMillis) {
        _mutatorMutex.mutate {
            _offset.animateTo(offset, animationSpec = tween(durationMillis))
        }
    }

    open suspend fun snapOffsetTo(offset: Float) {
        _mutatorMutex.mutate(MutatePriority.UserInput) {
            _offset.snapTo(offset)
        }
    }

    open fun safeSnapOffsetTo(needConsumedY: Float) {
        if (needConsumedY == 0f) return
        _coroutineScope.launch {
            snapOffsetTo(offset + needConsumedY)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return when {
            available.y < 0 -> {
                val canConsumed = (available.y * _dampingFactor).coerceAtLeast(0 - offset)
                safeSnapOffsetTo(canConsumed)
                available.copy(x = 0f, y = canConsumed / _dampingFactor)
            }

            else -> Offset.Zero
        }
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        return when {
            available.y > 0 -> {
                val canConsumed = available.y * _dampingFactor
                safeSnapOffsetTo(canConsumed)
                if (source == NestedScrollSource.Fling) {
                    throw CancellationException()
                }
                available.copy(x = 0f, y = canConsumed / _dampingFactor)
            }

            else -> Offset.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (offset > 0) {
            UtilKLogWrapper.d(TAG, "onPostFling: offset $offset consumed ${consumed.y} available ${available.y}")
            animateOffsetTo(0f)
            return available
        }
        return super.onPostFling(consumed, available)
    }
}

/**
 * 保存滚动进度
 */
class OverScrollStateSaver(private val coroutineScope: CoroutineScope) : Saver<OverScrollState, Float> {
    override fun restore(value: Float): OverScrollState {
        return OverScrollState(coroutineScope, value)
    }

    override fun SaverScope.save(value: OverScrollState): Float {
        return value.offset
    }
}

@Composable
fun rememberOverScrollState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): OverScrollState =
    rememberSaveable(saver = OverScrollStateSaver(coroutineScope)) {
        OverScrollState(coroutineScope)
    }

