package com.mozhimen.composek.material3

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Switch
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.material3.ColorScheme
import com.mozhimen.composek.material3.MaterialTheme
import com.mozhimen.composek.material3.SwitchColors
import com.mozhimen.composek.material3.fromToken
import com.mozhimen.composek.material3.value
import com.mozhimen.composek.material3.tokens.SwitchTokens
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * @ClassName Switch2
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/14
 * @Version 1.0
 */
@Composable
@Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
fun Switch2(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    switchWidth:Dp = SwitchWidth,
    switchHeight:Dp = SwitchHeight,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val uncheckedThumbDiameter = if (thumbContent == null) {
        UncheckedThumbDiameter
    } else {
        ThumbDiameter
    }

    val thumbPaddingStart = (switchHeight - uncheckedThumbDiameter) / 2
    val minBound = with(LocalDensity.current) { thumbPaddingStart.toPx() }
    val maxBound = with(LocalDensity.current) { getThumbPathLength(switchWidth,switchHeight).toPx() }
    val valueToOffset = remember<(Boolean) -> Float>(minBound, maxBound) {
        { value -> if (value) maxBound else minBound }
    }

    val targetValue = valueToOffset(checked)
    val offset = remember { Animatable(targetValue) }
    val scope = rememberCoroutineScope()

    SideEffect {
        // min bound might have changed if the icon is only rendered in checked state.
        offset.updateBounds(lowerBound = minBound)
    }

    DisposableEffect(checked) {
        if (offset.targetValue != targetValue) {
            scope.launch {
                offset.animateTo(targetValue, AnimationSpec)
            }
        }
        onDispose { }
    }

    // TODO: Add Swipeable modifier b/223797571
    val toggleableModifier =
        if (onCheckedChange != null) {
            Modifier.toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null
            )
        } else {
            Modifier
        }

    Box(
        modifier
            .then(
                if (onCheckedChange != null) {
                    Modifier.minimumInteractiveComponentSize()
                } else {
                    Modifier
                }
            )
            .then(toggleableModifier)
            .wrapContentSize(Alignment.Center)
            .requiredSize(switchWidth, switchHeight)
    ) {
        Switch2Impl(
            checked = checked,
            enabled = enabled,
            switchWidth = switchWidth,
            switchHeight = switchHeight,
            colors = colors,
            thumbValue = offset.asState(),
            interactionSource = interactionSource,
            thumbShape = CircleShape,
            uncheckedThumbDiameter = uncheckedThumbDiameter,
            minBound = thumbPaddingStart,
            maxBound = getThumbPathLength(switchWidth,switchHeight)/*ThumbPathLength*/,
            thumbContent = thumbContent,
        )
    }
}

@Composable
@Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
private fun BoxScope.Switch2Impl(
    checked: Boolean,
    enabled: Boolean,
    switchWidth:Dp = SwitchWidth,
    switchHeight:Dp = SwitchHeight,
    colors: SwitchColors,
    thumbValue: State<Float>,
    thumbContent: (@Composable () -> Unit)?,
    interactionSource: InteractionSource,
    thumbShape: Shape,
    uncheckedThumbDiameter: Dp,
    minBound: Dp,
    maxBound: Dp,
) {
    val trackColor = colors.trackColor(enabled, checked)
    val isPressed by interactionSource.collectIsPressedAsState()

    val thumbValueDp = with(LocalDensity.current) { thumbValue.value.toDp() }
    val thumbSizeDp = if (isPressed) {
        28.0.dp//SwitchTokens.PressedHandleWidth
    } else {
        uncheckedThumbDiameter + (ThumbDiameter - uncheckedThumbDiameter) *
                ((thumbValueDp - minBound) / (maxBound - minBound))
    }

    val thumbOffset = if (isPressed) {
        with(LocalDensity.current) {
            if (checked) {
                getThumbPathLength(switchWidth,switchHeight) - 2.0.dp//SwitchTokens.TrackOutlineWidth
            } else {
                2.0.dp//SwitchTokens.TrackOutlineWidth
            }.toPx()
        }
    } else {
        thumbValue.value
    }

    val trackShape = CircleShape//SwitchTokens.TrackShape.value
    val modifier = Modifier
        .align(Alignment.Center)
        .width(switchWidth)
        .height(switchHeight)
        .border(
            2.0.dp,//SwitchTokens.TrackOutlineWidth,
            colors.borderColor(enabled, checked),
            trackShape
        )
        .background(trackColor, trackShape)

    Box(modifier) {
        val resolvedThumbColor = colors.thumbColor(enabled, checked)
        @Suppress("DEPRECATION_ERROR")
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(thumbOffset.roundToInt(), 0) }
                .indication(
                    interactionSource = interactionSource,
                    indication = androidx.compose.material.ripple.rememberRipple(
                        bounded = false,
                        40.0.dp/*SwitchTokens.StateLayerSize*/ / 2
                    )
                )
                .requiredSize(thumbSizeDp)
                .background(resolvedThumbColor, thumbShape),
            contentAlignment = Alignment.Center
        ) {
            if (thumbContent != null) {
                val iconColor = colors.iconColor(enabled, checked)
                CompositionLocalProvider(
                    LocalContentColor provides iconColor,
                    content = thumbContent
                )
            }
        }
    }
}

internal val ThumbDiameter = 24.0.dp
internal val UncheckedThumbDiameter = 16.0.dp
public val SwitchWidth = 60.0.dp
public val SwitchHeight = 28.0.dp
private val ThumbPadding = (SwitchHeight - ThumbDiameter) / 2
fun getThumbPadding(switchHeight: Dp):Dp =
    (switchHeight - ThumbDiameter) / 2
private val ThumbPathLength = (SwitchWidth - ThumbDiameter) - ThumbPadding
fun getThumbPathLength(switchWidth: Dp,switchHeight: Dp):Dp =
    (switchWidth - ThumbDiameter) - getThumbPadding(switchHeight)
private val AnimationSpec = TweenSpec<Float>(durationMillis = 100)


/**
 * Contains the default values used by [Switch]
 */
object SwitchDefaults {
    /**
     * Creates a [SwitchColors] that represents the different colors used in a [Switch] in
     * different states.
     */
    @Composable
    fun colors() = MaterialTheme.colorScheme.defaultSwitchColors

    /**
     * Creates a [SwitchColors] that represents the different colors used in a [Switch] in
     * different states.
     *
     * @param checkedThumbColor the color used for the thumb when enabled and checked
     * @param checkedTrackColor the color used for the track when enabled and checked
     * @param checkedBorderColor the color used for the border when enabled and checked
     * @param checkedIconColor the color used for the icon when enabled and checked
     * @param uncheckedThumbColor the color used for the thumb when enabled and unchecked
     * @param uncheckedTrackColor the color used for the track when enabled and unchecked
     * @param uncheckedBorderColor the color used for the border when enabled and unchecked
     * @param uncheckedIconColor the color used for the icon when enabled and unchecked
     * @param disabledCheckedThumbColor the color used for the thumb when disabled and checked
     * @param disabledCheckedTrackColor the color used for the track when disabled and checked
     * @param disabledCheckedBorderColor the color used for the border when disabled and checked
     * @param disabledCheckedIconColor the color used for the icon when disabled and checked
     * @param disabledUncheckedThumbColor the color used for the thumb when disabled and unchecked
     * @param disabledUncheckedTrackColor the color used for the track when disabled and unchecked
     * @param disabledUncheckedBorderColor the color used for the border when disabled and unchecked
     * @param disabledUncheckedIconColor the color used for the icon when disabled and unchecked
     */
    @Composable
    fun colors(
        checkedThumbColor: Color = SwitchTokens.SelectedHandleColor.value,
        checkedTrackColor: Color = SwitchTokens.SelectedTrackColor.value,
        checkedBorderColor: Color = Color.Transparent,
        checkedIconColor: Color = SwitchTokens.SelectedIconColor.value,
        uncheckedThumbColor: Color = SwitchTokens.UnselectedHandleColor.value,
        uncheckedTrackColor: Color = SwitchTokens.UnselectedTrackColor.value,
        uncheckedBorderColor: Color = SwitchTokens.UnselectedFocusTrackOutlineColor.value,
        uncheckedIconColor: Color = SwitchTokens.UnselectedIconColor.value,
        disabledCheckedThumbColor: Color = SwitchTokens.DisabledSelectedHandleColor.value
            .copy(alpha = SwitchTokens.DisabledSelectedHandleOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
        disabledCheckedTrackColor: Color = SwitchTokens.DisabledSelectedTrackColor.value
            .copy(alpha = SwitchTokens.DisabledTrackOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
        disabledCheckedBorderColor: Color = Color.Transparent,
        disabledCheckedIconColor: Color = SwitchTokens.DisabledSelectedIconColor.value
            .copy(alpha = SwitchTokens.DisabledSelectedIconOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
        disabledUncheckedThumbColor: Color = SwitchTokens.DisabledUnselectedHandleColor.value
            .copy(alpha = SwitchTokens.DisabledUnselectedHandleOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
        disabledUncheckedTrackColor: Color = SwitchTokens.DisabledUnselectedTrackColor.value
            .copy(alpha = SwitchTokens.DisabledTrackOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
        disabledUncheckedBorderColor: Color =
            SwitchTokens.DisabledUnselectedTrackOutlineColor.value
                .copy(alpha = SwitchTokens.DisabledTrackOpacity)
                .compositeOver(MaterialTheme.colorScheme.surface),
        disabledUncheckedIconColor: Color = SwitchTokens.DisabledUnselectedIconColor.value
            .copy(alpha = SwitchTokens.DisabledUnselectedIconOpacity)
            .compositeOver(MaterialTheme.colorScheme.surface),
    ): SwitchColors = SwitchColors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedBorderColor = checkedBorderColor,
        checkedIconColor = checkedIconColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedBorderColor = uncheckedBorderColor,
        uncheckedIconColor = uncheckedIconColor,
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledCheckedBorderColor = disabledCheckedBorderColor,
        disabledCheckedIconColor = disabledCheckedIconColor,
        disabledUncheckedThumbColor = disabledUncheckedThumbColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        disabledUncheckedBorderColor = disabledUncheckedBorderColor,
        disabledUncheckedIconColor = disabledUncheckedIconColor
    )

    val ColorScheme.defaultSwitchColors: SwitchColors
        get() {
            return defaultSwitchColorsCached ?: SwitchColors(
                checkedThumbColor = fromToken(SwitchTokens.SelectedHandleColor),
                checkedTrackColor = fromToken(SwitchTokens.SelectedTrackColor),
                checkedBorderColor = Color.Transparent,
                checkedIconColor = fromToken(SwitchTokens.SelectedIconColor),
                uncheckedThumbColor = fromToken(SwitchTokens.UnselectedHandleColor),
                uncheckedTrackColor = fromToken(SwitchTokens.UnselectedTrackColor),
                uncheckedBorderColor = fromToken(SwitchTokens.UnselectedFocusTrackOutlineColor),
                uncheckedIconColor = fromToken(SwitchTokens.UnselectedIconColor),
                disabledCheckedThumbColor = fromToken(SwitchTokens.DisabledSelectedHandleColor)
                    .copy(alpha = SwitchTokens.DisabledSelectedHandleOpacity)
                    .compositeOver(surface),
                disabledCheckedTrackColor = fromToken(SwitchTokens.DisabledSelectedTrackColor)
                    .copy(alpha = SwitchTokens.DisabledTrackOpacity)
                    .compositeOver(surface),
                disabledCheckedBorderColor = Color.Transparent,
                disabledCheckedIconColor = fromToken(SwitchTokens.DisabledSelectedIconColor)
                    .copy(alpha = SwitchTokens.DisabledSelectedIconOpacity)
                    .compositeOver(surface),
                disabledUncheckedThumbColor = fromToken(SwitchTokens.DisabledUnselectedHandleColor)
                    .copy(alpha = SwitchTokens.DisabledUnselectedHandleOpacity)
                    .compositeOver(surface),
                disabledUncheckedTrackColor = fromToken(SwitchTokens.DisabledUnselectedTrackColor)
                    .copy(alpha = SwitchTokens.DisabledTrackOpacity)
                    .compositeOver(surface),
                disabledUncheckedBorderColor =
                fromToken(SwitchTokens.DisabledUnselectedTrackOutlineColor)
                    .copy(alpha = SwitchTokens.DisabledTrackOpacity)
                    .compositeOver(surface),
                disabledUncheckedIconColor = fromToken(SwitchTokens.DisabledUnselectedIconColor)
                    .copy(alpha = SwitchTokens.DisabledUnselectedIconOpacity)
                    .compositeOver(surface),
            ).also {
                defaultSwitchColorsCached = it
            }
        }

    /**
     * Icon size to use for `thumbContent`
     */
    val IconSize = 16.dp
}