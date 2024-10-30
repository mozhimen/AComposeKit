package com.mozhimen.composek.ui.text.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mozhimen.composek.ui.text.lerpTextUnitInheritable

/**
 * @ClassName TextIndent
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Specify the indentation of a paragraph.
 *
 * @param firstLine the amount of indentation applied to the first line.
 * @param restLine the amount of indentation applied to every line except the first line.
 */
@Immutable
class TextIndent(
    val firstLine: TextUnit = 0.sp,
    val restLine: TextUnit = 0.sp
) {
    companion object {
        /**
         * Constant fot no text indent.
         */
        @Stable
        val None = TextIndent()
    }

    fun copy(
        firstLine: TextUnit = this.firstLine,
        restLine: TextUnit = this.restLine
    ): TextIndent {
        return TextIndent(firstLine, restLine)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextIndent) return false
        if (firstLine != other.firstLine) return false
        if (restLine != other.restLine) return false
        return true
    }

    override fun hashCode(): Int {
        var result = firstLine.hashCode()
        result = 31 * result + restLine.hashCode()
        return result
    }

    override fun toString(): String {
        return "TextIndent(firstLine=$firstLine, restLine=$restLine)"
    }
}

/**
 * Linearly interpolate between two [TextIndent]s.
 *
 * The [fraction] argument represents position on the timeline, with 0.0 meaning
 * that the interpolation has not started, returning [start] (or something
 * equivalent to [start]), 1.0 meaning that the interpolation has finished,
 * returning [stop] (or something equivalent to [stop]), and values in between
 * meaning that the interpolation is at the relevant point on the timeline
 * between [start] and [stop]. The interpolation can be extrapolated beyond 0.0 and
 * 1.0, so negative values and values greater than 1.0 are valid.
 */
fun lerp(start: TextIndent, stop: TextIndent, fraction: Float): TextIndent {
    return TextIndent(
        lerpTextUnitInheritable(start.firstLine, stop.firstLine, fraction),
        lerpTextUnitInheritable(start.restLine, stop.restLine, fraction)
    )
}
