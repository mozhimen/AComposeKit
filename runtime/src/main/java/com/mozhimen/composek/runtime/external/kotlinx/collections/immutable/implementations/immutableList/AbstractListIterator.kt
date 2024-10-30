package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList

/**
 * @ClassName AbstractListIterator
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 23:58
 * @Version 1.0
 */
internal abstract class AbstractListIterator<out E>(var index: Int, var size: Int) : ListIterator<E> {
    override fun hasNext(): Boolean {
        return index < size
    }

    override fun hasPrevious(): Boolean {
        return index > 0
    }

    override fun nextIndex(): Int {
        return index
    }

    override fun previousIndex(): Int {
        return index - 1
    }

    internal fun checkHasNext() {
        if (!hasNext())
            throw NoSuchElementException()
    }

    internal fun checkHasPrevious() {
        if (!hasPrevious())
            throw NoSuchElementException()
    }
}


internal class SingleElementListIterator<E>(private val element: E, index: Int): AbstractListIterator<E>(index, 1) {
    override fun next(): E {
        checkHasNext()
        index++
        return element
    }

    override fun previous(): E {
        checkHasPrevious()
        index--
        return element
    }
}