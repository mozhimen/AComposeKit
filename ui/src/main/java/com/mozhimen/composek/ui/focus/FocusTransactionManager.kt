package com.mozhimen.composek.ui.focus

import androidx.compose.runtime.collection.mutableVectorOf

/**
 * @ClassName FocusTransactionManager
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * This manager provides a way to ensure that only one focus transaction is running at a time.
 * We use this to prevent re-entrant focus operations. Starting a new transaction automatically
 * cancels the previous transaction and reverts any focus state changes made during that
 * transaction.
 */
internal class FocusTransactionManager {

    private val states = mutableMapOf<FocusTargetNode, FocusStateImpl>()
    private val cancellationListener = mutableVectorOf<() -> Unit>()
    private var ongoingTransaction = false

    /**
     * Stars a new transaction, which allows you to change the focus state. Calling this function
     * causes any ongoing focus transaction to be cancelled. If an [onCancelled] lambda is
     * specified, it will be called if this transaction is cancelled by a new invocation to
     * [withNewTransaction].
     */
    inline fun <T> withNewTransaction(
        noinline onCancelled: (() -> Unit)? = null,
        block: () -> T
    ): T = try {
        if (ongoingTransaction) cancelTransaction()
        beginTransaction()
        onCancelled?.let { cancellationListener += it }
        block()
    } finally {
        commitTransaction()
    }

    /**
     * If another transaction is ongoing, this runs the specified [block] within that
     * transaction, and it commits any changes to focus state at the end of that transaction. If
     * there is no ongoing transaction, this will start a new transaction. If an [onCancelled]
     * lambda is specified, it will be called if this transaction is cancelled by a new invocation
     * to [withNewTransaction].
     */
    inline fun <T> withExistingTransaction(
        noinline onCancelled: (() -> Unit)? = null,
        block: () -> T
    ): T {
        onCancelled?.let { cancellationListener += it }
        return if (ongoingTransaction) block() else try {
            beginTransaction()
            block()
        } finally {
            commitTransaction()
        }
    }

    /**
     * The focus state for the specified [node][FocusTargetNode] if the state was changed during
     * the current transaction.
     */
    var FocusTargetNode.uncommittedFocusState: FocusStateImpl?
        get() = states[this]
        set(value) {
            states[this] = checkNotNull(value) { "requires a non-null focus state" }
        }

    private fun beginTransaction() {
        ongoingTransaction = true
    }

    private fun commitTransaction() {
        for (focusTargetNode in states.keys) {
            focusTargetNode.commitFocusState()
        }
        states.clear()
        ongoingTransaction = false
    }

    private fun cancelTransaction() {
        cancellationListener.forEach { it() }
        cancellationListener.clear()
        states.clear()
        ongoingTransaction = false
    }
}
