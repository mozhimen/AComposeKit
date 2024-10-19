package com.mozhimen.composek.textk

import androidx.compose.ui.text.TextRange
import androidx.emoji2.text.EmojiCompat
import java.text.BreakIterator

/**
 * @ClassName StringHelpers
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
/** StringBuilder.appendCodePoint is already defined on JVM so it's called appendCodePointX. */
internal fun StringBuilder.appendCodePointX(codePoint: Int): StringBuilder = this.appendCodePoint(codePoint)

/**
 * Returns the index of the character break preceding [index].
 */
internal fun String.findPrecedingBreak(index: Int): Int {
    val emojiBreak =
        getEmojiCompatIfLoaded()?.getEmojiStart(this, maxOf(0, index - 1))?.takeUnless { it == -1 }
    if (emojiBreak != null) return emojiBreak

    val it = BreakIterator.getCharacterInstance()
    it.setText(this)
    return it.preceding(index)
}

private fun getEmojiCompatIfLoaded(): EmojiCompat? =
    if (EmojiCompat.isConfigured())
        EmojiCompat.get().takeIf { it.loadState == EmojiCompat.LOAD_STATE_SUCCEEDED }
    else null


/**
 * Returns the index of the character break following [index]. Returns -1 if there are no more
 * breaks before the end of the string.
 */
internal  fun String.findFollowingBreak(index: Int): Int{
    val emojiBreak = getEmojiCompatIfLoaded()?.getEmojiEnd(this, index)?.takeUnless { it == -1 }
    if (emojiBreak != null) return emojiBreak

    val it = BreakIterator.getCharacterInstance()
    it.setText(this)
    return it.following(index)
}

internal fun CharSequence.findParagraphStart(startIndex: Int): Int {
    for (index in startIndex downTo 1) {
        if (this[index - 1] == '\n') {
            return index
        }
    }
    return 0
}

internal fun CharSequence.findParagraphEnd(startIndex: Int): Int {
    for (index in startIndex until this.length) {
        if (this[index] == '\n') {
            return index
        }
    }
    return this.length
}

/**
 * Returns the text range of the paragraph at the given character offset.
 *
 * Paragraphs are separated by Line Feed character (\n).
 */
internal fun CharSequence.getParagraphBoundary(index: Int): TextRange {
    return TextRange(findParagraphStart(index), findParagraphEnd(index))
}
