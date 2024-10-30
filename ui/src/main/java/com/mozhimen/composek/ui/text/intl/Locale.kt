package com.mozhimen.composek.ui.text.intl

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle

/**
 * @ClassName Locale
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * A `Locale` object represents a specific geographical, political, or cultural region. An
 * operation that requires a `Locale` to perform its task is called _locale-sensitive_ and uses the
 * `Locale` to tailor information for the user. For example, displaying a number is a
 * locale-sensitive operation— the number should be formatted according to the customs and
 * conventions of the user's native country, region, or culture.
 *
 * @see TextStyle
 * @see SpanStyle
 */
@Immutable
class Locale internal constructor(internal val platformLocale: PlatformLocale) {
    companion object {
        /**
         * Returns a [Locale] object which represents current locale
         */
        val current: Locale get() = platformLocaleDelegate.current[0]
    }

    /**
     * Create Locale object from a language tag.
     *
     * @param languageTag A [IETF BCP47](https://tools.ietf.org/html/bcp47) compliant language tag.
     *
     * @return a locale object
     */
    constructor(languageTag: String) : this(platformLocaleDelegate.parseLanguageTag(languageTag))

    /**
     * The ISO 639 compliant language code.
     */
    val language: String get() = platformLocale.language

    /**
     * The ISO 15924 compliant 4-letter script code.
     */
    val script: String get() = platformLocale.script

    /**
     * The ISO 3166 compliant region code.
     */
    val region: String get() = platformLocale.region

    /**
     * Returns a IETF BCP47 compliant language tag representation of this Locale.
     *
     * @return A IETF BCP47 compliant language tag.
     */
    fun toLanguageTag(): String = platformLocale.toLanguageTag()

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Locale) return false
        if (this === other) return true
        return toLanguageTag() == other.toLanguageTag()
    }

    // We don't use data class since we cannot offer copy function here.
    override fun hashCode(): Int = toLanguageTag().hashCode()

    override fun toString(): String = toLanguageTag()
}
