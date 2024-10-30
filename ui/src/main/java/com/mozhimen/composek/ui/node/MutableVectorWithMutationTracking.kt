package com.mozhimen.composek.ui.node

import androidx.compose.runtime.collection.MutableVector

/**
 * @ClassName MutableVectorWithMutationTracking
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:36
 * @Version 1.0
 */
/**
 * This class tracks the mutation to the provided [vector] through the provided methods.
 * On mutation, the [onVectorMutated] lambda will be invoked.
 */
internal class MutableVectorWithMutationTracking<T>(
    val vector: MutableVector<T>,
    val onVectorMutated: () -> Unit,
) {
    val size: Int
        get() = vector.size

    fun clear() {
        vector.clear()
        onVectorMutated()
    }

    fun add(index: Int, element: T) {
        vector.add(index, element)
        onVectorMutated()
    }

    fun removeAt(index: Int): T {
        return vector.removeAt(index).also {
            onVectorMutated()
        }
    }

    inline fun forEach(block: (T) -> Unit) = vector.forEach(block)

    fun asList(): List<T> = vector.asMutableList()

    operator fun get(index: Int): T = vector[index]
}
