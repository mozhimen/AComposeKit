package com.mozhimen.composek.ui.platform

import androidx.compose.runtime.collection.mutableVectorOf
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * @ClassName WeakCache
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * A simple collection that keeps values as [WeakReference]s.
 * Elements are added with [push] and removed with [pop].
 */
internal class WeakCache<T> {
    private val values = mutableVectorOf<Reference<T>>()
    private val referenceQueue = ReferenceQueue<T>()

    /**
     * Add [element] to the collection as a [WeakReference]. It will be removed when
     * garbage collected or from [pop].
     */
    fun push(element: T) {
        clearWeakReferences()
        values += WeakReference(element, referenceQueue)
    }

    /**
     * Remove an element from the collection and return it. If no element is
     * available, `null` is returned.
     */
    fun pop(): T? {
        clearWeakReferences()

        while (values.isNotEmpty()) {
            val item = values.removeAt(values.lastIndex).get()
            if (item != null) {
                return item
            }
        }
        return null
    }

    /**
     * The number of elements currently in the collection. This may change between
     * calls if the references have been garbage collected.
     */
    val size: Int
        get() {
            clearWeakReferences()
            return values.size
        }

    private fun clearWeakReferences() {
        do {
            val item: Reference<out T>? = referenceQueue.poll()
            if (item != null) {
                @Suppress("UNCHECKED_CAST")
                values.remove(item as Reference<T>)
            }
        } while (item != null)
    }
}
