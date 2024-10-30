package com.mozhimen.composek.ui.platform

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.ResourceFont
import androidx.core.content.res.ResourcesCompat

/**
 * @ClassName AndroidFontResourceLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Android implementation for [Font.ResourceLoader]. It is designed to load only [ResourceFont].
 */
@Suppress("DEPRECATION", "OverridingDeprecatedMember")
@Deprecated(
    "Replaced with PlatformFontLoader during the introduction of async fonts, all" +
            " usages should be replaced",
    ReplaceWith("PlatformFontLoader"),
)
internal class AndroidFontResourceLoader(
    private val context: Context
) : Font.ResourceLoader {

    @Deprecated(
        "Replaced by FontFamily.Resolver, this method should not be called",
        replaceWith = ReplaceWith("FontFamily.Resolver.resolve(font, )")
    )
    override fun load(font: Font): Typeface {
        return when (font) {
            is ResourceFont ->
                if (Build.VERSION.SDK_INT >= 26) {
                    AndroidFontResourceLoaderHelper.create(context, font.resId)
                } else {
                    ResourcesCompat.getFont(context, font.resId)!!
                }
            else -> throw IllegalArgumentException("Unknown font type: $font")
        }
    }
}

/**
 * This class is here to ensure that the classes that use this API will get verified and can be
 * AOT compiled. It is expected that this class will soft-fail verification, but the classes
 * which use this method will pass.
 */
@RequiresApi(26)
private object AndroidFontResourceLoaderHelper {
    @RequiresApi(26)
    @DoNotInline
    fun create(context: Context, resourceId: Int): Typeface {
        return context.resources.getFont(resourceId)
    }
}