package com.mozhimen.composek.basic.runtime

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.StateFactoryMarker
import com.mozhimen.composek.basic.runtime.snapshots.SnapshotStateList

/**
 * @ClassName SnapshotState
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/20 23:32
 * @Version 1.0
 */
/**
 * Create a instance of [MutableList]<T> that is observable and can be snapshot.
 *
 * @sample androidx.compose.runtime.samples.stateListSample
 *
 * @see mutableStateOf
 * @see mutableListOf
 * @see MutableList
 * @see Snapshot.takeSnapshot
 */
@StateFactoryMarker
fun <T> mutableStateListOf() = SnapshotStateList<T>()

/**
 * Create an instance of [MutableList]<T> that is observable and can be snapshot.
 *
 * @see mutableStateOf
 * @see mutableListOf
 * @see MutableList
 * @see Snapshot.takeSnapshot
 */
@StateFactoryMarker
fun <T> mutableStateListOf(vararg elements: T) =
    SnapshotStateList<T>().also { it.addAll(elements.toList()) }