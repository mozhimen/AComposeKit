package com.mozhimen.composek.ui.text.font

import android.graphics.Typeface
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.FontStyle

/**
 * @ClassName AndroidFontUtils
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal val FontWeight.Companion.AndroidBold
    get() = W600

/**
 * Lookup style used by [android.graphics.Typeface].
 *
 * May return one of:
 * - [Typeface.BOLD_ITALIC]
 * - [Typeface.BOLD]
 * - [Typeface.ITALIC]
 * - [Typeface.NORMAL]
 */
internal fun getAndroidTypefaceStyle(fontWeight: FontWeight, fontStyle: FontStyle): Int {
    val isBold = fontWeight >= FontWeight.AndroidBold
    val isItalic = fontStyle == FontStyle.Italic
    return getAndroidTypefaceStyle(isBold, isItalic)
}

/**
 * Lookup android typeface style without requiring a [FontWeight] or [FontStyle] object.
 *
 * May return one of:
 * - [Typeface.BOLD_ITALIC]
 * - [Typeface.BOLD]
 * - [Typeface.ITALIC]
 * - [Typeface.NORMAL]
 */
internal fun getAndroidTypefaceStyle(isBold: Boolean, isItalic: Boolean): Int {
    return if (isItalic && isBold) {
        android.graphics.Typeface.BOLD_ITALIC
    } else if (isBold) {
        android.graphics.Typeface.BOLD
    } else if (isItalic) {
        android.graphics.Typeface.ITALIC
    } else {
        android.graphics.Typeface.NORMAL
    }
}

/**
 * This class is here to ensure that the classes that use this API will get verified and can be
 * AOT compiled. It is expected that this class will soft-fail verification, but the classes
 * which use this method will pass.
 */
@RequiresApi(28)
internal object TypefaceHelperMethodsApi28 {
    @RequiresApi(28)
    @DoNotInline
    fun create(typeface: android.graphics.Typeface, finalFontWeight: Int, finalFontStyle: Boolean) =
        android.graphics.Typeface.create(typeface, finalFontWeight, finalFontStyle)
}
