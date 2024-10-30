package com.mozhimen.composek.ui.platform

import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.platform.URLSpanCache
import androidx.compose.ui.text.platform.extensions.toSpan
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastForEach
import com.mozhimen.composek.ui.text.AnnotatedString
import com.mozhimen.composek.ui.text.SpanStyle
import com.mozhimen.composek.ui.text.font.FontWeight
import com.mozhimen.composek.ui.text.font.getAndroidTypefaceStyle
import com.mozhimen.composek.ui.text.platform.extensions.setBackground
import com.mozhimen.composek.ui.text.platform.extensions.setColor
import com.mozhimen.composek.ui.text.platform.extensions.setFontSize
import com.mozhimen.composek.ui.text.platform.extensions.setLocaleList

/**
 * @ClassName AndroidAccessibilitySpannableString
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@OptIn(ExperimentalTextApi::class)
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@InternalTextApi // used in ui:ui
fun AnnotatedString.toAccessibilitySpannableString(
    density: Density,
    fontFamilyResolver: FontFamily.Resolver,
    urlSpanCache: URLSpanCache
): SpannableString {
    val spannableString = SpannableString(text)
    spanStylesOrNull?.fastForEach { (style, start, end) ->
        // b/232238615 looking up fonts inside of accessibility does not honor overwritten
        // FontFamilyResolver. This is not safe until Font.ResourceLoader is fully removed.
        val noFontStyle = style.copy(fontFamily = null)
        spannableString.setSpanStyle(noFontStyle, start, end, density, fontFamilyResolver)
    }

    getTtsAnnotations(0, length).fastForEach { (ttsAnnotation, start, end) ->
        spannableString.setSpan(
            ttsAnnotation.toSpan(),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    getUrlAnnotations(0, length).fastForEach { (urlAnnotation, start, end) ->
        spannableString.setSpan(
            urlSpanCache.toURLSpan(urlAnnotation),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}

/** Apply the serializable styles to SpannableString. */
private fun SpannableString.setSpanStyle(
    spanStyle: SpanStyle,
    start: Int,
    end: Int,
    density: Density,
    fontFamilyResolver: FontFamily.Resolver
) {
    setColor(spanStyle.color, start, end)

    setFontSize(spanStyle.fontSize, density, start, end)

    if (spanStyle.fontWeight != null || spanStyle.fontStyle != null) {
        // If current typeface is bold, StyleSpan won't change it to normal. The same applies to
        // font style, so use normal as default works here.
        // This is also a bug in framework span. But we can't find a good solution so far.
        val fontWeight: FontWeight = spanStyle.fontWeight ?: FontWeight.Normal
        val fontStyle:FontStyle = spanStyle.fontStyle ?: FontStyle.Normal
        setSpan(
            StyleSpan(getAndroidTypefaceStyle(fontWeight, fontStyle)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    // TypefaceSpan accepts Typeface as parameter only after P. And only font family string can be
    // pass to other thread.
    // Here we try to create TypefaceSpan with font family string if possible.
    if (spanStyle.fontFamily != null) {
        if (spanStyle.fontFamily is GenericFontFamily) {
            setSpan(
                TypefaceSpan(spanStyle.fontFamily.name),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // TODO(b/214587005): Check for async here and uncache
                val typeface = fontFamilyResolver.resolve(
                    fontFamily = spanStyle.fontFamily,
                    fontSynthesis = spanStyle.fontSynthesis ?: FontSynthesis.All
                ).value as Typeface
                setSpan(
                    Api28Impl.createTypefaceSpan(typeface),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    if (spanStyle.textDecoration != null) {
        // This doesn't match how we rendering the styles. When TextDecoration.None is set, it
        // should remove the underline and lineThrough effect on the given range. Here we didn't
        // remove any previously applied spans yet.
        if (TextDecoration.Underline in spanStyle.textDecoration) {
            setSpan(
                UnderlineSpan(),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (TextDecoration.LineThrough in spanStyle.textDecoration) {
            setSpan(
                StrikethroughSpan(),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    if (spanStyle.textGeometricTransform != null) {
        setSpan(
            ScaleXSpan(spanStyle.textGeometricTransform.scaleX),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    setLocaleList(spanStyle.localeList, start, end)

    setBackground(spanStyle.background, start, end)
}

@RequiresApi(28)
private object Api28Impl {
    @DoNotInline
    fun createTypefaceSpan(typeface: Typeface): TypefaceSpan = TypefaceSpan(typeface)
}
