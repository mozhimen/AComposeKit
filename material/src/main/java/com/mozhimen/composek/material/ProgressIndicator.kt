package com.mozhimen.composek.material

import androidx.annotation.FloatRange
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.foundation.progressSemantics
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max

/**
 * Determinate <a href="https://material.io/components/progress-indicators#circular-progress-indicators" class="external" target="_blank">Material Design circular progress indicator</a>.
 *
 * Progress indicators express an unspecified wait time or display the length of a process.
 *
 * ![Circular progress indicator image](https://developer.android.com/images/reference/androidx/compose/material/circular-progress-indicator.png)
 *
 * By default there is no animation between [progress] values. You can use
 * [ProgressIndicatorDefaults.ProgressAnimationSpec] as the default recommended
 * [AnimationSpec] when animating progress, such as in the following example:
 *
 * @sample androidx.compose.material.samples.CircularProgressIndicatorSample
 *
 * @param progress The progress of this progress indicator, where 0.0 represents no progress and 1.0
 * represents full progress. Values outside of this range are coerced into the range.
 * @param modifier the [Modifier] to be applied to this progress indicator
 * @param color The color of the progress indicator.
 * @param strokeWidth The stroke width for the progress indicator.
 * @param backgroundColor The color of the background behind the indicator, visible when the
 * progress has not reached that area of the overall indicator yet.
 * @param strokeCap stroke cap to use for the ends of this progress indicator
 */
@Composable
fun CircularProgressIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    backgroundColor: Color = Color.Transparent,
    strokeCap: StrokeCap = StrokeCap.Butt,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = strokeCap)
    }
    Canvas(
        modifier
            .progressSemantics(coercedProgress)
            .size(CircularIndicatorDiameter)
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = coercedProgress * 360f
        drawCircularIndicatorBackground(backgroundColor, stroke)
        drawDeterminateCircularIndicator(startAngle, sweep, color, stroke)
    }
}

/**
 * @ClassName ProgressIndicator
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Indeterminate <a href="https://material.io/components/progress-indicators#circular-progress-indicators" class="external" target="_blank">Material Design circular progress indicator</a>.
 *
 * Progress indicators express an unspecified wait time or display the length of a process.
 *
 * ![Circular progress indicator image](https://developer.android.com/images/reference/androidx/compose/material/circular-progress-indicator.png)
 *
 * @param modifier the [Modifier] to be applied to this progress indicator
 * @param color The color of the progress indicator.
 * @param strokeWidth The stroke width for the progress indicator.
 * @param backgroundColor The color of the background behind the indicator, visible when the
 * progress has not reached that area of the overall indicator yet.
 * @param strokeCap stroke cap to use for the ends of this progress indicator
 */
@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    backgroundColor: Color = Color.Transparent,
    strokeCap: StrokeCap = StrokeCap.Square,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = strokeCap)
    }

    val transition = rememberInfiniteTransition()
    // The current rotation around the circle, so we know where to start the rotation from
    val currentRotation by transition.animateValue(
        0,
        RotationsPerCycle,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration * RotationsPerCycle,
                easing = LinearEasing
            )
        )
    )
    // How far forward (degrees) the base point should be from the start point
    val baseRotation by transition.animateFloat(
        0f,
        BaseRotationAngle,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = LinearEasing
            )
        )
    )
    // How far forward (degrees) both the head and tail should be from the base point
    val endAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 using CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
    )

    val startAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration using CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    )
    Canvas(
        modifier
            .progressSemantics()
            .size(CircularIndicatorDiameter)
    ) {
        drawCircularIndicatorBackground(backgroundColor, stroke)

        val currentRotationAngleOffset = (currentRotation * RotationAngleOffset) % 360f

        // How long a line to draw using the start angle as a reference point
        val sweep = abs(endAngle - startAngle)

        // Offset by the constant offset and the per rotation offset
        val offset = StartAngleOffset + currentRotationAngleOffset + baseRotation
        drawIndeterminateCircularIndicator(startAngle + offset, strokeWidth, sweep, color, stroke)
    }
}

@Deprecated("Maintained for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
fun CircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) = CircularProgressIndicator(
    progress,
    modifier,
    color,
    strokeWidth,
    backgroundColor = Color.Transparent,
    strokeCap = StrokeCap.Butt,
)

@Deprecated("Maintained for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) = CircularProgressIndicator(
    modifier,
    color,
    strokeWidth,
    backgroundColor = Color.Transparent,
    strokeCap = StrokeCap.Square,
)

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private fun DrawScope.drawCircularIndicatorBackground(
    color: Color,
    stroke: Stroke
) = drawCircularIndicator(0f, 360f, color, stroke)

private fun DrawScope.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) = drawCircularIndicator(startAngle, sweep, color, stroke)

private fun DrawScope.drawIndeterminateCircularIndicator(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    val strokeCapOffset = if (stroke.cap == StrokeCap.Butt) {
        0f
    } else {
        // Length of arc is angle * radius
        // Angle (radians) is length / radius
        // The length should be the same as the stroke width for calculating the min angle
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f
    }

    // Adding a stroke cap draws half the stroke width behind the start point, so we want to
    // move it forward by that amount so the arc visually appears in the correct place
    val adjustedStartAngle = startAngle + strokeCapOffset

    // When the start and end angles are in the same place, we still want to draw a small sweep, so
    // the stroke caps get added on both ends and we draw the correct minimum length arc
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularIndicator(adjustedStartAngle, adjustedSweep, color, stroke)
}

// LinearProgressIndicator Material specs
// TODO: there are currently 3 fixed widths in Android, should this be flexible? Material says
// the width should be 240dp here.
private val LinearIndicatorHeight = ProgressIndicatorDefaults.StrokeWidth
private val LinearIndicatorWidth = 240.dp

// CircularProgressIndicator Material specs
// Diameter of the indicator circle
private val CircularIndicatorDiameter = 40.dp

// Indeterminate linear indicator transition specs
// Total duration for one cycle
private const val LinearAnimationDuration = 1800

// Duration of the head and tail animations for both lines
private const val FirstLineHeadDuration = 750
private const val FirstLineTailDuration = 850
private const val SecondLineHeadDuration = 567
private const val SecondLineTailDuration = 533

// Delay before the start of the head and tail animations for both lines
private const val FirstLineHeadDelay = 0
private const val FirstLineTailDelay = 333
private const val SecondLineHeadDelay = 1000
private const val SecondLineTailDelay = 1267

private val FirstLineHeadEasing = CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
private val FirstLineTailEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)
private val SecondLineHeadEasing = CubicBezierEasing(0f, 0f, 0.65f, 1f)
private val SecondLineTailEasing = CubicBezierEasing(0.1f, 0f, 0.45f, 1f)

// Indeterminate circular indicator transition specs

// The animation comprises of 5 rotations around the circle forming a 5 pointed star.
// After the 5th rotation, we are back at the beginning of the circle.
private const val RotationsPerCycle = 5

// Each rotation is 1 and 1/3 seconds, but 1332ms divides more evenly
private const val RotationDuration = 1332

// When the rotation is at its beginning (0 or 360 degrees) we want it to be drawn at 12 o clock,
// which means 270 degrees when drawing.
private const val StartAngleOffset = -90f

// How far the base point moves around the circle
private const val BaseRotationAngle = 286f

// How far the head and tail should jump forward during one rotation past the base point
private const val JumpRotationAngle = 290f

// Each rotation we want to offset the start position by this much, so we continue where
// the previous rotation ended. This is the maximum angle covered during one rotation.
private const val RotationAngleOffset = (BaseRotationAngle + JumpRotationAngle) % 360f

// The head animates for the first half of a rotation, then is static for the second half
// The tail is static for the first half and then animates for the second half
private const val HeadAndTailAnimationDuration = (RotationDuration * 0.5).toInt()
private const val HeadAndTailDelayDuration = HeadAndTailAnimationDuration

// The easing for the head and tail jump
private val CircularEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)