package com.mozhimen.composek.basic.material3

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.basic.material3.tokens.SwitchTokens

/**
 * @ClassName Switch
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/16 16:19
 * @Version 1.0
 */

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

    internal val ColorScheme.defaultSwitchColors: SwitchColors
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

/**
 * Represents the colors used by a [Switch] in different states
 *
 * @constructor create an instance with arbitrary colors.
 * See [SwitchDefaults.colors] for the default implementation that follows Material
 * specifications.
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
@Immutable
class SwitchColors constructor(
    val checkedThumbColor: Color,
    val checkedTrackColor: Color,
    val checkedBorderColor: Color,
    val checkedIconColor: Color,
    val uncheckedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedBorderColor: Color,
    val uncheckedIconColor: Color,
    val disabledCheckedThumbColor: Color,
    val disabledCheckedTrackColor: Color,
    val disabledCheckedBorderColor: Color,
    val disabledCheckedIconColor: Color,
    val disabledUncheckedThumbColor: Color,
    val disabledUncheckedTrackColor: Color,
    val disabledUncheckedBorderColor: Color,
    val disabledUncheckedIconColor: Color
) {
    /**
     * Returns a copy of this SwitchColors, optionally overriding some of the values.
     * This uses the Color.Unspecified to mean “use the value from the source”
     */
    fun copy(
        checkedThumbColor: Color = this.checkedThumbColor,
        checkedTrackColor: Color = this.checkedTrackColor,
        checkedBorderColor: Color = this.checkedBorderColor,
        checkedIconColor: Color = this.checkedIconColor,
        uncheckedThumbColor: Color = this.uncheckedThumbColor,
        uncheckedTrackColor: Color = this.uncheckedTrackColor,
        uncheckedBorderColor: Color = this.uncheckedBorderColor,
        uncheckedIconColor: Color = this.uncheckedIconColor,
        disabledCheckedThumbColor: Color = this.disabledCheckedThumbColor,
        disabledCheckedTrackColor: Color = this.disabledCheckedTrackColor,
        disabledCheckedBorderColor: Color = this.disabledCheckedBorderColor,
        disabledCheckedIconColor: Color = this.disabledCheckedIconColor,
        disabledUncheckedThumbColor: Color = this.disabledUncheckedThumbColor,
        disabledUncheckedTrackColor: Color = this.disabledUncheckedTrackColor,
        disabledUncheckedBorderColor: Color = this.disabledUncheckedBorderColor,
        disabledUncheckedIconColor: Color = this.disabledUncheckedIconColor,
    ) = SwitchColors(
        checkedThumbColor.takeOrElse { this.checkedThumbColor },
        checkedTrackColor.takeOrElse { this.checkedTrackColor },
        checkedBorderColor.takeOrElse { this.checkedBorderColor },
        checkedIconColor.takeOrElse { this.checkedIconColor },
        uncheckedThumbColor.takeOrElse { this.uncheckedThumbColor },
        uncheckedTrackColor.takeOrElse { this.uncheckedTrackColor },
        uncheckedBorderColor.takeOrElse { this.uncheckedBorderColor },
        uncheckedIconColor.takeOrElse { this.uncheckedIconColor },
        disabledCheckedThumbColor.takeOrElse { this.disabledCheckedThumbColor },
        disabledCheckedTrackColor.takeOrElse { this.disabledCheckedTrackColor },
        disabledCheckedBorderColor.takeOrElse { this.disabledCheckedBorderColor },
        disabledCheckedIconColor.takeOrElse { this.disabledCheckedIconColor },
        disabledUncheckedThumbColor.takeOrElse { this.disabledUncheckedThumbColor },
        disabledUncheckedTrackColor.takeOrElse { this.disabledUncheckedTrackColor },
        disabledUncheckedBorderColor.takeOrElse { this.disabledUncheckedBorderColor },
        disabledUncheckedIconColor.takeOrElse { this.disabledUncheckedIconColor },
    )

    /**
     * Represents the color used for the switch's thumb, depending on [enabled] and [checked].
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Stable
    fun thumbColor(enabled: Boolean, checked: Boolean): Color =
        if (enabled) {
            if (checked) checkedThumbColor else uncheckedThumbColor
        } else {
            if (checked) disabledCheckedThumbColor else disabledUncheckedThumbColor
        }

    /**
     * Represents the color used for the switch's track, depending on [enabled] and [checked].
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Stable
    fun trackColor(enabled: Boolean, checked: Boolean): Color =
        if (enabled) {
            if (checked) checkedTrackColor else uncheckedTrackColor
        } else {
            if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
        }

    /**
     * Represents the color used for the switch's border, depending on [enabled] and [checked].
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Stable
    fun borderColor(enabled: Boolean, checked: Boolean): Color =
        if (enabled) {
            if (checked) checkedBorderColor else uncheckedBorderColor
        } else {
            if (checked) disabledCheckedBorderColor else disabledUncheckedBorderColor
        }

    /**
     * Represents the content color passed to the icon if used
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Stable
    fun iconColor(enabled: Boolean, checked: Boolean): Color =
        if (enabled) {
            if (checked) checkedIconColor else uncheckedIconColor
        } else {
            if (checked) disabledCheckedIconColor else disabledUncheckedIconColor
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is SwitchColors) return false

        if (checkedThumbColor != other.checkedThumbColor) return false
        if (checkedTrackColor != other.checkedTrackColor) return false
        if (checkedBorderColor != other.checkedBorderColor) return false
        if (checkedIconColor != other.checkedIconColor) return false
        if (uncheckedThumbColor != other.uncheckedThumbColor) return false
        if (uncheckedTrackColor != other.uncheckedTrackColor) return false
        if (uncheckedBorderColor != other.uncheckedBorderColor) return false
        if (uncheckedIconColor != other.uncheckedIconColor) return false
        if (disabledCheckedThumbColor != other.disabledCheckedThumbColor) return false
        if (disabledCheckedTrackColor != other.disabledCheckedTrackColor) return false
        if (disabledCheckedBorderColor != other.disabledCheckedBorderColor) return false
        if (disabledCheckedIconColor != other.disabledCheckedIconColor) return false
        if (disabledUncheckedThumbColor != other.disabledUncheckedThumbColor) return false
        if (disabledUncheckedTrackColor != other.disabledUncheckedTrackColor) return false
        if (disabledUncheckedBorderColor != other.disabledUncheckedBorderColor) return false
        if (disabledUncheckedIconColor != other.disabledUncheckedIconColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checkedThumbColor.hashCode()
        result = 31 * result + checkedTrackColor.hashCode()
        result = 31 * result + checkedBorderColor.hashCode()
        result = 31 * result + checkedIconColor.hashCode()
        result = 31 * result + uncheckedThumbColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + uncheckedBorderColor.hashCode()
        result = 31 * result + uncheckedIconColor.hashCode()
        result = 31 * result + disabledCheckedThumbColor.hashCode()
        result = 31 * result + disabledCheckedTrackColor.hashCode()
        result = 31 * result + disabledCheckedBorderColor.hashCode()
        result = 31 * result + disabledCheckedIconColor.hashCode()
        result = 31 * result + disabledUncheckedThumbColor.hashCode()
        result = 31 * result + disabledUncheckedTrackColor.hashCode()
        result = 31 * result + disabledUncheckedBorderColor.hashCode()
        result = 31 * result + disabledUncheckedIconColor.hashCode()
        return result
    }
}


