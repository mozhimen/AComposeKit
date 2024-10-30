package com.mozhimen.composek.ui.platform

/**
 * @ClassName JvmActuals
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:30
 * @Version 1.0
 */
internal typealias AtomicInt = java.util.concurrent.atomic.AtomicInteger

internal fun simpleIdentityToString(obj: Any, name: String?): String {
    val className = name ?: if (obj::class.java.isAnonymousClass) {
        obj::class.java.name
    } else {
        obj::class.java.simpleName
    }

    return className + "@" + String.format("%07x", System.identityHashCode(obj))
}

internal fun Any.nativeClass(): Any = this.javaClass

@PublishedApi
internal inline fun <R> synchronized(lock: Any, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}