package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList


/**
 * @ClassName BufferIterator
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 23:57
 * @Version 1.0
 */
internal class BufferIterator<out T>(
    private val buffer: Array<T>,
    index: Int,
    size: Int
) : AbstractListIterator<T>(index, size) {
    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return buffer[index++]
    }

    override fun previous(): T {
        if (!hasPrevious()) {
            throw NoSuchElementException()
        }
        return buffer[--index]
    }
}