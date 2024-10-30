package com.mozhimen.composek.foundation.text.selection

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import kotlinx.coroutines.launch

/**
 * @ClassName SelectionMagnifier
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
private val UnspecifiedAnimationVector2D = AnimationVector2D(Float.NaN, Float.NaN)

/** Like `Offset.VectorConverter` but propagates [Offset.Unspecified] values. */
internal val UnspecifiedSafeOffsetVectorConverter = TwoWayConverter<Offset, AnimationVector2D>(
    convertToVector = {
        if (it.isSpecified) {
            AnimationVector2D(it.x, it.y)
        } else {
            UnspecifiedAnimationVector2D
        }
    },
    convertFromVector = { Offset(it.v1, it.v2) }
)

internal val OffsetDisplacementThreshold = Offset(
    Spring.DefaultDisplacementThreshold,
    Spring.DefaultDisplacementThreshold
)

internal val MagnifierSpringSpec = SpringSpec(visibilityThreshold = OffsetDisplacementThreshold)

/**
 * The text magnifier follows horizontal dragging exactly, but is vertically clamped to the current
 * line, so when it changes lines we animate it.
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
internal fun Modifier.animatedSelectionMagnifier(
    magnifierCenter: () -> Offset,
    platformMagnifier: (animatedCenter: () -> Offset) -> Modifier
): Modifier = composed {
    val animatedCenter by rememberAnimatedMagnifierPosition(targetCalculation = magnifierCenter)
    return@composed platformMagnifier { animatedCenter }
}

/**
 * Remembers and returns a [State] that will smoothly animate to the result of [targetCalculation]
 * any time the result of [targetCalculation] changes due to any state values it reads change.
 */
@Composable
private fun rememberAnimatedMagnifierPosition(
    targetCalculation: () -> Offset,
): State<Offset> {
    val targetValue by remember { derivedStateOf(targetCalculation) }
    val animatable = remember {
        // Can't use Offset.VectorConverter because we need to handle Unspecified specially.
        Animatable(targetValue, UnspecifiedSafeOffsetVectorConverter, OffsetDisplacementThreshold)
    }
    LaunchedEffect(Unit) {
        val animationScope = this
        snapshotFlow { targetValue }
            .collect { targetValue ->
                // Only animate the position when moving vertically (i.e. jumping between lines),
                // since horizontal movement in a single line should stay as close to the gesture as
                // possible and animation would only add unnecessary lag.
                if (
                    animatable.value.isSpecified &&
                    targetValue.isSpecified &&
                    animatable.value.y != targetValue.y
                ) {
                    // Launch the animation, instead of cancelling and re-starting manually via
                    // collectLatest, so if another animation is started before this one finishes,
                    // the new one will use the correct velocity, e.g. in order to propagate spring
                    // inertia.
                    animationScope.launch {
                        animatable.animateTo(targetValue, MagnifierSpringSpec)
                    }
                } else {
                    animatable.snapTo(targetValue)
                }
            }
    }
    return animatable.asState()
}
