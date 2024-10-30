package com.mozhimen.composek.runtime

import com.mozhimen.composek.basic.runtime.internal.ThreadMap
import com.mozhimen.composek.basic.runtime.internal.emptyThreadMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @ClassName ActualJvm
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/20 0:17
 * @Version 1.0
 */
internal typealias AtomicReference<V> = java.util.concurrent.atomic.AtomicReference<V>

internal class SnapshotThreadLocal<T> {
    private val map = AtomicReference<ThreadMap>(emptyThreadMap)
    private val writeMutex = Any()

    private var mainThreadValue: T? = null

    @Suppress("UNCHECKED_CAST")
    fun get(): T? {
        val threadId = Thread.currentThread().id
        return if (threadId == MainThreadId) {
            mainThreadValue
        } else {
            map.get().get(Thread.currentThread().id) as T?
        }
    }

    fun set(value: T?) {
        val key = Thread.currentThread().id
        if (key == MainThreadId) {
            mainThreadValue = value
        } else {
            synchronized(writeMutex) {
                val current = map.get()
                if (current.trySet(key, value)) return
                map.set(current.newWith(key, value))
            }
        }
    }
}

internal typealias TestOnly = org.jetbrains.annotations.TestOnly

internal fun identityHashCode(instance: Any?): Int = System.identityHashCode(instance)

internal class AtomicInt constructor(value: Int) : AtomicInteger(value) {
    fun add(amount: Int): Int = addAndGet(amount)

    // These are implemented by Number, but Kotlin fails to resolve them
    override fun toByte(): Byte = toInt().toByte()
    override fun toShort(): Short = toInt().toShort()
    override fun toChar(): Char = toInt().toChar()
}

internal class WeakReference<T : Any> constructor(reference: T) :
    java.lang.ref.WeakReference<T>(reference)