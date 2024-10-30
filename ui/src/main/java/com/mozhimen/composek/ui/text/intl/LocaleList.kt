package com.mozhimen.composek.ui.text.intl

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.util.fastMap

/**
 * @ClassName LocaleList
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Defines a list of [Locale] objects.
 *
 * @see TextStyle
 * @see SpanStyle
 */
@Immutable
class LocaleList constructor(val localeList: List<Locale>) : Collection<Locale> {
    companion object {

        /**
         * Returns Locale object which represents current locale
         */
        val current: LocaleList
            get() = platformLocaleDelegate.current
    }

    /**
     * Create a [LocaleList] object from comma separated language tags.
     *
     * @param languageTags A comma separated [IETF BCP47](https://tools.ietf.org/html/bcp47)
     * compliant language tag.
     */
    constructor(languageTags: String) :
            this(languageTags.split(",").fastMap { it.trim() }.fastMap { Locale(it) })

    /**
     * Creates a [LocaleList] object from a list of [Locale]s.
     */
    constructor(vararg locales: Locale) : this(locales.toList())

    operator fun get(i: Int) = localeList[i]

    // Collection overrides for easy iterations.
    override val size: Int = localeList.size

    override operator fun contains(element: Locale): Boolean = localeList.contains(element)

    override fun containsAll(elements: Collection<Locale>): Boolean =
        localeList.containsAll(elements)

    override fun isEmpty(): Boolean = localeList.isEmpty()

    override fun iterator(): Iterator<Locale> = localeList.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocaleList) return false
        if (localeList != other.localeList) return false
        return true
    }

    override fun hashCode(): Int {
        return localeList.hashCode()
    }

    override fun toString(): String {
        return "LocaleList(localeList=$localeList)"
    }
}
