package com.mozhimen.composek.material3

import androidx.compose.material3.Switch
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse

/**
 * @ClassName Switch
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/16 16:19
 * @Version 1.0
 */

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
)
{
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


