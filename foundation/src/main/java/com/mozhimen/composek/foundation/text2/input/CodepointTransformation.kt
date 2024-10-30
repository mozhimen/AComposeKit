package com.mozhimen.composek.foundation.text2.input

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Stable
import com.mozhimen.composek.foundation.text.appendCodePointX
import com.mozhimen.composek.foundation.text2.input.internal.OffsetMappingCalculator
import com.mozhimen.composek.foundation.text2.input.internal.charCount
import com.mozhimen.composek.foundation.text2.input.internal.codePointAt

/**
 * @ClassName CodepointTransformation
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

/**
 * Visual transformation interface for input fields.
 *
 * This interface is responsible for 1-to-1 mapping of every codepoint in input state to another
 * codepoint before text is rendered. Visual transformation is useful when the underlying source
 * of input needs to remain but rendered content should look different, e.g. password obscuring.
 */
@ExperimentalFoundationApi
@Stable
fun interface CodepointTransformation {

    /**
     * Transforms a single [codepoint] located at [codepointIndex] to another codepoint.
     *
     * A codepoint is an integer that always maps to a single character. Every codepoint in Unicode
     * is comprised of 16 bits, 2 bytes.
     */
    // TODO: add more codepoint explanation or doc referral
    fun transform(codepointIndex: Int, codepoint: Int): Int

    companion object
}

/**
 * Creates a masking [CodepointTransformation] that maps all codepoints to a specific [character].
 */
@ExperimentalFoundationApi
@Stable
fun CodepointTransformation.Companion.mask(character: Char): CodepointTransformation =
    MaskCodepointTransformation(character)

@OptIn(ExperimentalFoundationApi::class)
private data class MaskCodepointTransformation(val character: Char) : CodepointTransformation {
    override fun transform(codepointIndex: Int, codepoint: Int): Int {
        return character.code
    }
}

/**
 * [CodepointTransformation] that converts all line breaks (\n) into white space(U+0020) and
 * carriage returns(\r) to zero-width no-break space (U+FEFF). This transformation forces any
 * content to appear as single line.
 */
@OptIn(ExperimentalFoundationApi::class)
internal object SingleLineCodepointTransformation : CodepointTransformation {

    private const val LINE_FEED = '\n'.code
    private const val CARRIAGE_RETURN = '\r'.code

    private const val WHITESPACE = ' '.code
    private const val ZERO_WIDTH_SPACE = '\uFEFF'.code

    override fun transform(codepointIndex: Int, codepoint: Int): Int {
        if (codepoint == LINE_FEED) return WHITESPACE
        if (codepoint == CARRIAGE_RETURN) return ZERO_WIDTH_SPACE
        return codepoint
    }

    override fun toString(): String {
        return "SingleLineCodepointTransformation"
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun TextFieldCharSequence.toVisualText(
    codepointTransformation: CodepointTransformation,
    offsetMappingCalculator: OffsetMappingCalculator
): CharSequence {
    val text = this
    var changed = false
    val newText = buildString {
        var charOffset = 0
        var codePointOffset = 0
        while (charOffset < text.length) {
            val codePoint = text.codePointAt(charOffset)
            val newCodePoint = codepointTransformation.transform(codePointOffset, codePoint)
            val charCount = charCount(codePoint)
            if (newCodePoint != codePoint) {
                changed = true
                val newCharCount = charCount(newCodePoint)
                offsetMappingCalculator.recordEditOperation(
                    sourceStart = length,
                    sourceEnd = length + charCount,
                    newLength = newCharCount
                )
            }
            appendCodePointX(newCodePoint)

            charOffset += charCount
            codePointOffset += 1
        }
    }

    // Return the same instance if nothing changed, which signals to the caller that nothing changed
    // and allows the new string to be GC'd earlier.
    return if (changed) newText else this
}
