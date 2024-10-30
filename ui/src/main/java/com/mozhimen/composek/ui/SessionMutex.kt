package com.mozhimen.composek.ui

import androidx.annotation.RestrictTo
import androidx.compose.ui.InternalComposeUiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job

/**
 * Helper class for coordinating between mutually-exclusive sessions. A session is represented as
 * an object of type [T] and a coroutine [Job] whose lifetime is tied to the session.
 *
 * Only one session can be active at a time. When a new session is started, the old session will
 * be cancelled and allowed to finish any cancellation tasks (e.g. `finally` blocks) before the
 * new session's coroutine starts.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@InternalComposeUiApi
@JvmInline
value class SessionMutex<T> private constructor(
    private val currentSessionHolder: AtomicReference<Session<T>?>
) {
    constructor() : this(AtomicReference(null))

    /**
     * Returns the current session object.
     */
    val currentSession: T?
        get() = currentSessionHolder.get()?.value

    /**
     * Cancels any existing session and then calls [session].
     * [session] will in turn be cancelled if this method is called again before it returns.
     *
     * @param sessionInitializer Called immediately to create the new session object, before
     * cancelling the previous session. Receives a [CoroutineScope] that has the same context as
     * [session] will get.
     * @param session Called with the return value from [sessionInitializer] after cancelling the
     * previous session.
     */
    suspend fun <R> withSessionCancellingPrevious(
        sessionInitializer: (CoroutineScope) -> T,
        session: suspend (data: T) -> R
    ): R = coroutineScope {
        val newSession = Session(
            job = coroutineContext.job,
            value = sessionInitializer(this)
        )
        currentSessionHolder.getAndSet(newSession)?.job?.cancelAndJoin()
        try {
            return@coroutineScope session(newSession.value)
        } finally {
            // If this session is being interrupted by another session, the holder will already have
            // been changed, so this will fail. If the session is getting cancelled externally or
            // from within, this will ensure we release the session while no session is active.
            currentSessionHolder.compareAndSet(newSession, null)
        }
    }

    private class Session<T>(val job: Job, val value: T)
}