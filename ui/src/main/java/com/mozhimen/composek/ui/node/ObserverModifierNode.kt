package com.mozhimen.composek.ui.node

import com.mozhimen.composek.ui.Modifier


/**
 * @ClassName ObserverModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * [Modifier.Node]s that implement ObserverNode can provide their own implementation of
 * [onObservedReadsChanged] that will be called in response to changes to snapshot objects
 * read within an [observeReads] block.
 */
interface ObserverModifierNode : DelegatableNode {

    /**
     * This callback is called when any values that are read within the [observeReads] block
     * change. It is called after the snapshot is committed. [onObservedReadsChanged] is called on
     * the UI thread, and only called once in response to snapshot observation. To continue
     * observing further updates, you need to call [observeReads] again.
     */
    fun onObservedReadsChanged()
}

internal class ObserverNodeOwnerScope(
    internal val observerNode: ObserverModifierNode
) : OwnerScope {
    override val isValidOwnerScope: Boolean
        get() = observerNode.node.isAttached

    companion object {
        internal val OnObserveReadsChanged: (ObserverNodeOwnerScope) -> Unit = {
            if (it.isValidOwnerScope) it.observerNode.onObservedReadsChanged()
        }
    }
}

/**
 * Use this function to observe snapshot reads for any target within the specified [block].
 * [onDrawCacheReadsChanged] is called when any of the observed values within the snapshot change.
 */
fun <T> T.observeReads(block: () -> Unit) where T : Modifier.Node, T : ObserverModifierNode {
    val target = ownerScope ?: ObserverNodeOwnerScope(this).also { ownerScope = it }
    requireOwner().snapshotObserver.observeReads(
        target = target,
        onChanged = ObserverNodeOwnerScope.OnObserveReadsChanged,
        block = block
    )
}
