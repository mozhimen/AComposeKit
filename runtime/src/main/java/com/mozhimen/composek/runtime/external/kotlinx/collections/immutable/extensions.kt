package com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable

import com.mozhimen.composek.basic.runtime.external.kotlinx.collections.immutable.implementations.immutableList.persistentVectorOf


/**
 * @ClassName extensions
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/19 23:53
 * @Version 1.0
 */
/**
 * Returns the result of applying the provided modifications on this list.
 *
 * The mutable list passed to the [mutator] closure has the same contents as this persistent list.
 *
 * @return a new persistent list with the provided modifications applied;
 * or this instance if no modifications were made in the result of this operation.
 */
internal inline fun <T> PersistentList<T>.mutate(mutator: (MutableList<T>) -> Unit): PersistentList<T> = builder().apply(mutator).build()

/**
 * Returns an empty persistent list.
 */
fun <E> persistentListOf(): PersistentList<E> = persistentVectorOf()

