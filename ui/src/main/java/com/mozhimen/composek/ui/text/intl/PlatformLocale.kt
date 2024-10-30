package com.mozhimen.composek.ui.text.intl

/**
 * @ClassName PlatformLocale
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Interface for providing platform dependent locale object.
 */
internal interface PlatformLocale {

    /**
     * Implementation must give ISO 639 compliant language code.
     */
    val language: String

    /**
     * Implementation must give ISO 15924 compliant 4-letter script code.
     */
    val script: String

    /**
     * Implementation must give ISO 3166 compliant region code.
     */
    val region: String

    /**
     * Implementation must return IETF BCP47 compliant language tag representation of this Locale.
     */
    fun toLanguageTag(): String
}

/**
 * Interface for providing platform dependent locale non-instance helper functions.
 *
 */
internal interface PlatformLocaleDelegate {
    /**
     * Returns the list of current locales.
     *
     * The implementation must return at least one locale.
     */
    val current: LocaleList

    /**
     * Parse the IETF BCP47 compliant language tag.
     *
     * @return The locale
     */
    fun parseLanguageTag(languageTag: String): PlatformLocale
}

//internal expect fun createPlatformLocaleDelegate(): PlatformLocaleDelegate

internal val platformLocaleDelegate = createPlatformLocaleDelegate()
