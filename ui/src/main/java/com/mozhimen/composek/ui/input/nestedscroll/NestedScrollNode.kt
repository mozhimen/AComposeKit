package com.mozhimen.composek.ui.input.nestedscroll

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Velocity
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.modifier.ModifierLocalModifierNode
import com.mozhimen.composek.ui.modifier.modifierLocalMapOf
import com.mozhimen.composek.ui.modifier.modifierLocalOf
import com.mozhimen.composek.ui.node.DelegatableNode
import kotlinx.coroutines.CoroutineScope

/**
 * @ClassName NestedScrollNode
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 0:04
 * @Version 1.0
 */
internal val ModifierLocalNestedScroll = modifierLocalOf<NestedScrollNode?> { null }

/**
 * This creates a Nested Scroll Modifier node that can be delegated to. In most case you should
 * use [Modifier.nestedScroll] since that implementation also uses this. Use this factory to create
 * nodes that can be delegated to.
 */
fun nestedScrollModifierNode(
    connection: NestedScrollConnection,
    dispatcher: NestedScrollDispatcher?
): DelegatableNode {
    return NestedScrollNode(connection, dispatcher)
}

/**
 * NestedScroll using ModifierLocal as implementation.
 */
internal class NestedScrollNode(
    var connection: NestedScrollConnection,
    dispatcher: NestedScrollDispatcher?
) : ModifierLocalModifierNode, NestedScrollConnection, DelegatableNode, Modifier.Node() {

    // Resolved dispatcher for re-use in case of null dispatcher is passed.
    private var resolvedDispatcher: NestedScrollDispatcher

    init {
        resolvedDispatcher = dispatcher ?: NestedScrollDispatcher() // Resolve null dispatcher
    }

    private val parentModifierLocal: NestedScrollNode?
        get() = if (isAttached) ModifierLocalNestedScroll.current else null

    private val parentConnection: NestedScrollConnection?
        get() = if (isAttached) ModifierLocalNestedScroll.current else null

    // Avoid get() to prevent constant allocations for static map.
    override val providedValues = modifierLocalMapOf(entry = ModifierLocalNestedScroll to this)

    private val nestedCoroutineScope: CoroutineScope
        get() = parentModifierLocal?.nestedCoroutineScope
            ?: resolvedDispatcher.scope
            ?: throw IllegalStateException(
                "in order to access nested coroutine scope you need to attach dispatcher to the " +
                        "`Modifier.nestedScroll` first."
            )

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val parentPreConsumed = parentConnection?.onPreScroll(available, source) ?: Offset.Zero
        val selfPreConsumed = connection.onPreScroll(available - parentPreConsumed, source)
        return parentPreConsumed + selfPreConsumed
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val selfConsumed = connection.onPostScroll(consumed, available, source)
        val parentConsumed = parentConnection?.onPostScroll(
            consumed + selfConsumed,
            available - selfConsumed,
            source
        ) ?: Offset.Zero
        return selfConsumed + parentConsumed
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val parentPreConsumed = parentConnection?.onPreFling(available) ?: Velocity.Zero
        val selfPreConsumed = connection.onPreFling(available - parentPreConsumed)
        return parentPreConsumed + selfPreConsumed
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

        val selfConsumed = connection.onPostFling(consumed, available)
        val parentConsumed = parentConnection?.onPostFling(
            consumed + selfConsumed,
            available - selfConsumed
        ) ?: Velocity.Zero
        return selfConsumed + parentConsumed
    }

    // On receiving a new dispatcher, re-setting fields
    private fun updateDispatcher(newDispatcher: NestedScrollDispatcher?) {
        resetDispatcherFields() // Reset fields of current dispatcher.

        // Update dispatcher associated with this node.
        if (newDispatcher == null) {
            resolvedDispatcher = NestedScrollDispatcher()
        } else if (newDispatcher != resolvedDispatcher) {
            resolvedDispatcher = newDispatcher
        }

        // Update fields of the newly set dispatcher.
        if (isAttached) {
            updateDispatcherFields()
        }
    }

    override fun onAttach() {
        // NOTE: It is possible for the dispatcher of a yet-to-be-removed node above this one in the
        // chain is being used here where the dispatcher's modifierLocalNode will not be null. As a
        // result, we should not check to see if the dispatcher's node is null, we should just set
        // it assuming that it is not going to be used by the previous node anymore.
        updateDispatcherFields()
    }

    override fun onDetach() {
        resetDispatcherFields()
    }

    /**
     * If the node changes (onAttach) or if the dispatcher changes (node.update). We'll need
     * to reset the dispatcher properties accordingly.
     */
    private fun updateDispatcherFields() {
        resolvedDispatcher.modifierLocalNode = this
        resolvedDispatcher.calculateNestedScrollScope = { nestedCoroutineScope }
        resolvedDispatcher.scope = coroutineScope
    }

    private fun resetDispatcherFields() {
        // only null this out if the modifier local node is what we set it to, since it is possible
        // it has already been reused in a different node
        if (resolvedDispatcher.modifierLocalNode === this)
            resolvedDispatcher.modifierLocalNode = null
    }

    internal fun updateNode(
        connection: NestedScrollConnection,
        dispatcher: NestedScrollDispatcher?
    ) {
        this.connection = connection
        updateDispatcher(dispatcher)
    }
}
