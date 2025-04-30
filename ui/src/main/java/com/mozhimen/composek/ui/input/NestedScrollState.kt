package com.mozhimen.composek.ui.input

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import com.mozhimen.composek.utils.runtime.debounceChange
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @ClassName NestedScrollState
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/22
 * @Version 1.0
 */
@Stable
open class NestedScrollState constructor(
    protected val _coroutineScope: CoroutineScope,
    offsetSave: Float = 0f,
) : NestedScrollConnection, IUtilK {
    var topBarHeight = 0
        set(value) {
            field = value
        }
    var noPinContentLayoutHeight = 0
        set(value) {
            field = value
        }
    val interval: Int
        get() = noPinContentLayoutHeight - topBarHeight
    private val _offset = Animatable(offsetSave)
    private val _mutatorMutex = MutatorMutex()

    /////////////////////////////////////////////////////////////////////////////

    val offset: Float
        get() = _offset.value

    internal val offsetInterval: Float
        get() = _offset.value

    val scrollState = ScrollableState {
        val needConsumedY = when {
            it > 0 && offsetInterval < 0 -> {
                // drag down
                it
            }

            it < 0 && offsetInterval > -interval -> {
                // drag up
                it.coerceAtLeast(-interval - offsetInterval)
            }

            else -> 0f
        }
        if (needConsumedY == 0f && offsetInterval > 0f) {
            _coroutineScope.launch {
                snapOffsetTo(0f)
            }
        } else {
            safeSnapOffsetTo(needConsumedY)

        }
        needConsumedY
    }

    /////////////////////////////////////////////////////////////////////////////

    suspend fun animateOffsetTo(offset: Float, durationMillis: Int = AnimationConstants.DefaultDurationMillis) {
        _mutatorMutex.mutate {
            _offset.animateTo(offset, animationSpec = tween(durationMillis))
        }
    }

    private fun safeAnimateOffsetTo(needConsumedY: Float) {
        _coroutineScope.launch {
            animateOffsetTo(needConsumedY)
        }
    }

    suspend fun snapOffsetTo(offset: Float) {
        _mutatorMutex.mutate(MutatePriority.UserInput) {
            _offset.snapTo(offset)
        }
    }

    private fun safeSnapOffsetTo(needConsumedY: Float) {
        if (needConsumedY == 0f) return
        _coroutineScope.launch {
            snapOffsetTo(offsetInterval + needConsumedY)
        }
    }

    suspend fun scrollToFold() {
        animateOffsetTo(-interval.toFloat())
    }

    /////////////////////////////////////////////////////////////////////////////

    /**
     * 消费顺序：父onPreScroll->子onPreScroll->子onPostScroll->父onPostScroll
     *
     * 问题记录：
     * 1、如果该connection放在最外层，HorizontalPager是子，connection会先消费onPostScroll，它的逻辑会CancellationException，详情查看[DefaultPagerNestedScrollConnection]
     * 2、如果该connection放在最内层，HorizontalPager是父
     */

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val needConsumedY = when {
            available.y < 0 && offsetInterval > -interval -> {
                // drag up
                available.y.coerceAtLeast(-interval - offsetInterval)
            }

            else -> 0f
        }
        safeSnapOffsetTo(needConsumedY)
        return available.copy(x = 0f, y = needConsumedY)
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        val needConsumedY = when {
            available.y > 0 && offsetInterval < 0 -> {
                // drag down
                available.y.coerceAtMost(-offsetInterval)
            }

            else -> 0f
        }
        safeSnapOffsetTo(needConsumedY)
        return available.copy(x = 0f, y = needConsumedY)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return super.onPostFling(consumed, available)
    }
}

/**
 * 保存滚动进度
 */
class NestedScrollStateSaver(private val coroutineScope: CoroutineScope) : Saver<NestedScrollState, Float> {
    override fun restore(value: Float): NestedScrollState {
        return NestedScrollState(coroutineScope, value)
    }

    override fun SaverScope.save(value: NestedScrollState): Float {
        return value.offsetInterval
    }
}

@Composable
fun rememberNestedScrollState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): NestedScrollState =
    rememberSaveable(saver = NestedScrollStateSaver(coroutineScope)) {
        NestedScrollState(coroutineScope)
    }