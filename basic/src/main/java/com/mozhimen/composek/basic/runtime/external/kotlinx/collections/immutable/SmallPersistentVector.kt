package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable

import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.AbstractPersistentList
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.BufferIterator
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.MAX_BUFFER_SIZE
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.MAX_BUFFER_SIZE_MINUS_ONE
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.PersistentVector
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.PersistentVectorBuilder
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.presizedBufferWith
import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.internal.ListImplementation
import com.mozhimen.kotlin.utilk.kotlin.collections.UtilKCollections

/**
 * @ClassName SmallPersistentVector
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 23:54
 * @Version 1.0
 */
internal class SmallPersistentVector<E>(private val buffer: Array<Any?>) : ImmutableList<E>, AbstractPersistentList<E>() {

    init {
        assert(buffer.size <= MAX_BUFFER_SIZE)
    }

    override val size: Int
        get() = buffer.size

    private fun bufferOfSize(size: Int): Array<Any?> {
        return arrayOfNulls<Any?>(size)
    }

    override fun add(element: E): PersistentList<E> {
        if (size < MAX_BUFFER_SIZE) {
            val newBuffer = buffer.copyOf(size + 1)
            newBuffer[size] = element
            return SmallPersistentVector(newBuffer)
        }
        val tail = presizedBufferWith(element)
        return PersistentVector(buffer, tail, size + 1, 0)
    }

    override fun addAll(elements: Collection<E>): PersistentList<E> {
        if (size + elements.size <= MAX_BUFFER_SIZE) {
            val newBuffer = buffer.copyOf(size + elements.size)
            // TODO: investigate performance of elements.toArray + copyInto
            var index = size
            for (element in elements) {
                newBuffer[index++] = element
            }
            return SmallPersistentVector(newBuffer)
        }
        return mutate { it.addAll(elements) }
    }

    override fun removeAll(predicate: (E) -> Boolean): PersistentList<E> {
        var newBuffer = buffer
        var newSize = size

        var anyRemoved = false

        for (index in 0 until size) {
            @Suppress("UNCHECKED_CAST")
            val element = buffer[index] as E

            if (predicate(element)) {
                if (!anyRemoved) {
                    newBuffer = buffer.copyOf()
                    newSize = index

                    anyRemoved = true
                }
            } else if (anyRemoved) {
                newBuffer[newSize++] = element
            }
        }
        return when (newSize) {
            size -> this
            0 -> EMPTY
            else -> SmallPersistentVector(newBuffer.copyOfRange(0, newSize))
        }
    }

    override fun addAll(index: Int, c: Collection<E>): PersistentList<E> {
        ListImplementation.checkPositionIndex(index, size)
        if (size + c.size <= MAX_BUFFER_SIZE) {
            val newBuffer = bufferOfSize(size + c.size)
            buffer.copyInto(newBuffer, endIndex = index)
            buffer.copyInto(newBuffer, index + c.size, index, size)
            var position = index
            for (element in c) {
                newBuffer[position++] = element
            }
            return SmallPersistentVector(newBuffer)
        }
        return mutate { it.addAll(index, c) }
    }

    override fun add(index: Int, element: E): PersistentList<E> {
        ListImplementation.checkPositionIndex(index, size)
        if (index == size) {
            return add(element)
        }

        if (size < MAX_BUFFER_SIZE) {
            // TODO: copyOf + one copyInto?
            val newBuffer = bufferOfSize(size + 1)
            buffer.copyInto(newBuffer, endIndex = index)
            buffer.copyInto(newBuffer, index + 1, index, size)
            newBuffer[index] = element
            return SmallPersistentVector(newBuffer)
        }

        val root = buffer.copyOf()
        buffer.copyInto(root, index + 1, index, size - 1)
        root[index] = element
        val tail = presizedBufferWith(buffer[MAX_BUFFER_SIZE_MINUS_ONE])
        return PersistentVector(root, tail, size + 1, 0)
    }

    override fun removeAt(index: Int): PersistentList<E> {
        ListImplementation.checkElementIndex(index, size)
        if (size == 1) {
            return EMPTY
        }
        val newBuffer = buffer.copyOf(size - 1)
        buffer.copyInto(newBuffer, index, index + 1, size)
        return SmallPersistentVector(newBuffer)
    }

    override fun builder(): PersistentList.Builder<E> {
        return PersistentVectorBuilder(this, null, buffer, 0)
    }

    override fun indexOf(element: E): Int {
        return buffer.indexOf(element)
    }

    override fun lastIndexOf(element: E): Int {
        return buffer.lastIndexOf(element)
    }

    override fun listIterator(index: Int): ListIterator<E> {
        ListImplementation.checkPositionIndex(index, size)
        @Suppress("UNCHECKED_CAST")
        return BufferIterator(buffer as Array<E>, index, size)
    }

    override fun get(index: Int): E {
        // TODO: use elementAt(index)?
        ListImplementation.checkElementIndex(index, size)
        @Suppress("UNCHECKED_CAST")
        return buffer[index] as E
    }

    override fun set(index: Int, element: E): PersistentList<E> {
        ListImplementation.checkElementIndex(index, size)
        val newBuffer = buffer.copyOf()
        newBuffer[index] = element
        return SmallPersistentVector(newBuffer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as SmallPersistentVector<*>

        if (!buffer.contentEquals(other.buffer)) return false
        if (size != other.size) return false
        if (!UtilKCollections.listsEqual(this, other)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + buffer.contentHashCode()
        result = 31 * result + size
        return result
    }

    companion object {
        val EMPTY = SmallPersistentVector<Nothing>(emptyArray())
    }
}