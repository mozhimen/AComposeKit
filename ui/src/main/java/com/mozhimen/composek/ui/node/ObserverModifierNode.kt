package com.mozhimen.composek.ui.node

import androidx.compose.ui.node.ObserverModifierNode

/**
 * @ClassName ObserverModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
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