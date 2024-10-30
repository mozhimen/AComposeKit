package com.mozhimen.composek.ui.text.platform

/**
 * @ClassName Synchronization
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@PublishedApi
internal /*actual*/ class SynchronizedObject

internal /*actual*/ fun createSynchronizedObject() = SynchronizedObject()

@PublishedApi
internal /*actual*/ inline fun <R> synchronized(lock: SynchronizedObject, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}
