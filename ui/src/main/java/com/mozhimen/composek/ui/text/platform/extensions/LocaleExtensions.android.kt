package com.mozhimen.composek.ui.text.platform.extensions

import android.text.style.LocaleSpan
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import com.mozhimen.composek.ui.text.intl.AndroidLocale
import com.mozhimen.composek.ui.text.intl.Locale
import com.mozhimen.composek.ui.text.intl.LocaleList
import com.mozhimen.composek.ui.text.platform.AndroidTextPaint

/**
 * @ClassName LocaleExtensions
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
internal fun Locale.toJavaLocale(): java.util.Locale = (platformLocale as AndroidLocale).javaLocale

/**
 * This class is here to ensure that the classes that use this API will get verified and can be
 * AOT compiled. It is expected that this class will soft-fail verification, but the classes
 * which use this method will pass.
 */
@RequiresApi(24)
internal object LocaleListHelperMethods {
    @RequiresApi(24)
    @DoNotInline
    fun localeSpan(localeList: LocaleList): Any =
        LocaleSpan(
            android.os.LocaleList(*localeList.map { it.toJavaLocale() }.toTypedArray())
        )

    @RequiresApi(24)
    @DoNotInline
    fun setTextLocales(textPaint: AndroidTextPaint, localeList: LocaleList) {
        textPaint.textLocales = android.os.LocaleList(
            *localeList.map { it.toJavaLocale() }.toTypedArray()
        )
    }
}
