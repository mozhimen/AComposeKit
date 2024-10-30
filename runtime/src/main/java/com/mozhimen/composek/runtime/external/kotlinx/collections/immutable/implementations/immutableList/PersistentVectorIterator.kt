package com.mozhimen.composek.runtime.external.kotlinx.collections.immutable.implementations.immutableList

import com.mozhimen.composek.runtime.external.kotlinx.collections.immutable.implementations.immutableList.AbstractListIterator


/**
 * @ClassName PersistentVectorIterator
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/20 0:08
 * @Version 1.0
 */
internal class PersistentVectorIterator<out T>(root: Array<Any?>,
                                               private val tail: Array<T>,
                                               index: Int,
                                               size: Int,
                                               trieHeight: Int) : AbstractListIterator<T>(index, size) {
    private val trieIterator: TrieIterator<T>

    init {
        val trieSize = rootSize(size)
        val trieIndex = index.coerceAtMost(trieSize)
        trieIterator = TrieIterator(root, trieIndex, trieSize, trieHeight)
    }

    override fun next(): T {
        checkHasNext()
        if (trieIterator.hasNext()) {
            index++
            return trieIterator.next()
        }
        return tail[index++ - trieIterator.size]
    }

    override fun previous(): T {
        checkHasPrevious()
        if (index > trieIterator.size) {
            return tail[--index - trieIterator.size]
        }
        index--
        return trieIterator.previous()
    }
}