package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList

import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.ImmutableList
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.PersistentList
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.mutate


/**
 * @ClassName AbstractPersistentList
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 23:55
 * @Version 1.0
 */
internal abstract class AbstractPersistentList<E> : PersistentList<E>, AbstractList<E>() {
    override fun subList(fromIndex: Int, toIndex: Int): ImmutableList<E> {
        return super<PersistentList>.subList(fromIndex, toIndex)
    }

    override fun addAll(elements: Collection<E>): PersistentList<E> {
        return mutate { it.addAll(elements) }
    }

    override fun addAll(index: Int, c: Collection<E>): PersistentList<E> {
        return mutate { it.addAll(index, c) }
    }

    override fun remove(element: E): PersistentList<E> {
        val index = this.indexOf(element)
        if (index != -1) {
            return this.removeAt(index)
        }
        return this
    }

    override fun removeAll(elements: Collection<E>): PersistentList<E> {
        return removeAll { elements.contains(it) }
    }

    override fun retainAll(elements: Collection<E>): PersistentList<E> {
        return removeAll { !elements.contains(it) }
    }

    override fun clear(): PersistentList<E> {
        return persistentVectorOf()
    }

    override fun contains(element: E): Boolean {
        return this.indexOf(element) != -1
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return elements.all { this.contains(it) }
    }

    override fun iterator(): Iterator<E> {
        return this.listIterator()
    }

    override fun listIterator(): ListIterator<E> {
        return this.listIterator(0)
    }
}