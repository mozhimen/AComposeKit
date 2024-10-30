package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.internal

/**
 * @ClassName ListImplementation
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 18:13
 * @Version 1.0
 */
internal object ListImplementation {

    @JvmStatic
    internal fun checkElementIndex(index: Int, size: Int) {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("index: $index, size: $size")
        }
    }

    @JvmStatic
    internal fun checkPositionIndex(index: Int, size: Int) {
        if (index < 0 || index > size) {
            throw IndexOutOfBoundsException("index: $index, size: $size")
        }
    }

    @JvmStatic
    internal fun checkRangeIndexes(fromIndex: Int, toIndex: Int, size: Int) {
        if (fromIndex < 0 || toIndex > size) {
            throw IndexOutOfBoundsException("fromIndex: $fromIndex, toIndex: $toIndex, size: $size")
        }
        if (fromIndex > toIndex) {
            throw IllegalArgumentException("fromIndex: $fromIndex > toIndex: $toIndex")
        }
    }

    @JvmStatic
    internal fun orderedHashCode(c: Collection<*>): Int {
        var hashCode = 1
        for (e in c) {
            hashCode = 31 * hashCode + (e?.hashCode() ?: 0)
        }
        return hashCode
    }

    @JvmStatic
    internal fun orderedEquals(c: Collection<*>, other: Collection<*>): Boolean {
        if (c.size != other.size) return false

        val otherIterator = other.iterator()
        for (elem in c) {
            val elemOther = otherIterator.next()
            if (elem != elemOther) {
                return false
            }
        }
        return true
    }
}