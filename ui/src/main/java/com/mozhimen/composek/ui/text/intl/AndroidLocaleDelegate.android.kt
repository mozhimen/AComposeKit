package com.mozhimen.composek.ui.text.intl

import androidx.annotation.RequiresApi
import com.mozhimen.composek.ui.text.platform.createSynchronizedObject
import java.util.Locale

/**
 * @ClassName AndroidLocaleDelegate
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * An Android implementation of Locale object
 */
internal class AndroidLocale(val javaLocale: Locale) : PlatformLocale {
    override val language: String
        get() = javaLocale.language

    override val script: String
        get() = javaLocale.script

    override val region: String
        get() = javaLocale.country

    override fun toLanguageTag(): String = javaLocale.toLanguageTag()
}

/**
 * An Android implementation of LocaleDelegate object for API 23
 */
internal class AndroidLocaleDelegateAPI23 : PlatformLocaleDelegate {

    override val current: LocaleList
        get() = LocaleList(listOf(Locale(AndroidLocale(Locale.getDefault()))))

    override fun parseLanguageTag(languageTag: String): PlatformLocale =
        AndroidLocale(Locale.forLanguageTag(languageTag))
}

/**
 * An Android implementation of LocaleDelegate object for API 24 and later
 */
@RequiresApi(api = 24)
internal class AndroidLocaleDelegateAPI24 : PlatformLocaleDelegate {
    private var lastPlatformLocaleList: android.os.LocaleList? = null
    private var lastLocaleList: LocaleList? = null
    private val lock = createSynchronizedObject()

    override val current: LocaleList
        get() {
            val platformLocaleList = android.os.LocaleList.getDefault()
            return synchronized(lock) {
                // try to avoid any more allocs
                lastLocaleList?.let {
                    if (platformLocaleList === lastPlatformLocaleList) return it
                }
                // this is faster than adding to an empty mutableList
                val localeList = LocaleList(
                    List(platformLocaleList.size()) { position ->
                        Locale(AndroidLocale(platformLocaleList[position]))
                    }
                )
                // cache the platform result and compose result
                lastPlatformLocaleList = platformLocaleList
                lastLocaleList = localeList
                localeList
            }
        }

    override fun parseLanguageTag(languageTag: String): PlatformLocale =
        AndroidLocale(Locale.forLanguageTag(languageTag))
}
