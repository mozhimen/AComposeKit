package com.mozhimen.composek.ui.focus

import androidx.compose.ui.ExperimentalComposeUiApi
import com.mozhimen.composek.ui.focus.FocusRequester.Companion.Default
import com.mozhimen.composek.ui.focus.FocusStateImpl.Active
import com.mozhimen.composek.ui.focus.FocusStateImpl.ActiveParent
import com.mozhimen.composek.ui.focus.FocusStateImpl.Captured
import com.mozhimen.composek.ui.focus.FocusStateImpl.Inactive
import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.layout.BeyondBoundsLayout
import com.mozhimen.composek.ui.layout.ModifierLocalBeyondBoundsLayout
import com.mozhimen.composek.ui.modifier.ModifierLocalModifierNode
import com.mozhimen.composek.ui.node.CompositionLocalConsumerModifierNode
import com.mozhimen.composek.ui.node.ModifierNodeElement
import com.mozhimen.composek.ui.node.Nodes
import com.mozhimen.composek.ui.node.ObserverModifierNode
import com.mozhimen.composek.ui.node.dispatchForKind
import com.mozhimen.composek.ui.node.observeReads
import com.mozhimen.composek.ui.node.requireOwner
import com.mozhimen.composek.ui.node.visitAncestors
import com.mozhimen.composek.ui.node.visitSelfAndAncestors

/**
 * @ClassName FocusTargetNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal class FocusTargetNode :
    CompositionLocalConsumerModifierNode,
    FocusTargetModifierNode,
    ObserverModifierNode,
    ModifierLocalModifierNode,
    Modifier.Node() {

    private var isProcessingCustomExit = false
    private var isProcessingCustomEnter = false

    // During a transaction, changes to the state are stored as uncommitted focus state. At the
    // end of the transaction, this state is stored as committed focus state.
    private var committedFocusState: FocusStateImpl = FocusStateImpl.Inactive

    @OptIn(ExperimentalComposeUiApi::class)
    override var focusState: FocusStateImpl
        get() = focusTransactionManager?.run { uncommittedFocusState } ?: committedFocusState
        set(value) {
            with(requireTransactionManager()) {
                uncommittedFocusState = value
            }
        }

    var previouslyFocusedChildHash: Int = 0

    val beyondBoundsLayoutParent: BeyondBoundsLayout?
        get() = ModifierLocalBeyondBoundsLayout.current

    override fun onObservedReadsChanged() {
        val previousFocusState = focusState
        invalidateFocus()
        if (previousFocusState != focusState) refreshFocusEventNodes()
    }

    /**
     * Clears focus if this focus target has it.
     */
    override fun onReset() {
        when (focusState) {
            // Clear focus from the current FocusTarget.
            // This currently clears focus from the entire hierarchy, but we can change the
            // implementation so that focus is sent to the immediate focus parent.
            Active, Captured -> requireOwner().focusOwner.clearFocus(force = true)
            ActiveParent -> {
                scheduleInvalidationForFocusEvents()
                // This node might be reused, so reset the state to Inactive.
                requireTransactionManager().withNewTransaction { focusState = Inactive }
            }
            Inactive -> scheduleInvalidationForFocusEvents()
        }
    }

    /**
     * Visits parent [FocusPropertiesModifierNode]s and runs
     * [FocusPropertiesModifierNode.applyFocusProperties] on each parent.
     * This effectively collects an aggregated focus state.
     */
    internal fun fetchFocusProperties(): FocusProperties {
        val properties = FocusPropertiesImpl()
        visitSelfAndAncestors(Nodes.FocusProperties, untilType = Nodes.FocusTarget) {
            it.applyFocusProperties(properties)
        }
        return properties
    }

    /**
     * Fetch custom enter destination associated with this [focusTarget].
     *
     * Custom focus enter properties are specified as a lambda. If the user runs code in this
     * lambda that triggers a focus search, or some other focus change that causes focus to leave
     * the sub-hierarchy associated with this node, we could end up in a loop as that operation
     * will trigger another invocation of the lambda associated with the focus exit property.
     * This function prevents that re-entrant scenario by ensuring there is only one concurrent
     * invocation of this lambda.
     */
    internal inline fun fetchCustomEnter(
        focusDirection: FocusDirection,
        block: (FocusRequester) -> Unit
    ) {
        if (!isProcessingCustomEnter) {
            isProcessingCustomEnter = true
            try {
                @OptIn(ExperimentalComposeUiApi::class)
                fetchFocusProperties().enter(focusDirection).also {
                    if (it !== Default) block(it)
                }
            } finally {
                isProcessingCustomEnter = false
            }
        }
    }

    /**
     * Fetch custom exit destination associated with this [focusTarget].
     *
     * Custom focus exit properties are specified as a lambda. If the user runs code in this
     * lambda that triggers a focus search, or some other focus change that causes focus to leave
     * the sub-hierarchy associated with this node, we could end up in a loop as that operation
     * will trigger another invocation of the lambda associated with the focus exit property.
     * This function prevents that re-entrant scenario by ensuring there is only one concurrent
     * invocation of this lambda.
     */
    internal inline fun fetchCustomExit(
        focusDirection: FocusDirection,
        block: (FocusRequester) -> Unit
    ) {
        if (!isProcessingCustomExit) {
            isProcessingCustomExit = true
            try {
                @OptIn(ExperimentalComposeUiApi::class)
                fetchFocusProperties().exit(focusDirection).also {
                    if (it !== Default) block(it)
                }
            } finally {
                isProcessingCustomExit = false
            }
        }
    }

    internal fun commitFocusState() {
        with(requireTransactionManager()) {
            committedFocusState = checkNotNull(uncommittedFocusState) {
                "committing a node that was not updated in the current transaction"
            }
        }
    }

    internal fun invalidateFocus() {
        when (focusState) {
            // Clear focus from the current FocusTarget.
            // This currently clears focus from the entire hierarchy, but we can change the
            // implementation so that focus is sent to the immediate focus parent.
            Active, Captured -> {
                lateinit var focusProperties: FocusProperties
                observeReads {
                    focusProperties = fetchFocusProperties()
                }
                if (!focusProperties.canFocus) {
                    requireOwner().focusOwner.clearFocus(force = true)
                }
            }

            ActiveParent, Inactive -> {}
        }
    }

    internal fun scheduleInvalidationForFocusEvents() {
        // include possibility for ourselves to also be a focus event modifier node in case
        // we are being delegated to
        node.dispatchForKind(Nodes.FocusEvent) { eventNode ->
            eventNode.invalidateFocusEvent()
        }
        // Since this is potentially called while _this_ node is getting detached, it is possible
        // that the nodes above us are already detached, thus, we check for isAttached here.
        // We should investigate changing the order that children.detach() is called relative to
        // actually nulling out / detaching ones self.
        visitAncestors(Nodes.FocusEvent or Nodes.FocusTarget) {
            if (it.isKind(Nodes.FocusTarget)) return@visitAncestors

            if (it.isAttached) {
                it.dispatchForKind(Nodes.FocusEvent) { eventNode ->
                    eventNode.invalidateFocusEvent()
                }
            }
        }
    }

    internal object FocusTargetElement : ModifierNodeElement<FocusTargetNode>() {
        override fun create() = FocusTargetNode()

        override fun update(node: FocusTargetNode) {}

        override fun InspectorInfo.inspectableProperties() {
            name = "focusTarget"
        }

        override fun hashCode() = "focusTarget".hashCode()
        override fun equals(other: Any?) = other === this
    }
}

internal fun FocusTargetNode.requireTransactionManager(): FocusTransactionManager {
    return requireOwner().focusOwner.focusTransactionManager
}

private val FocusTargetNode.focusTransactionManager: FocusTransactionManager?
    get() = node.coordinator?.layoutNode?.owner?.focusOwner?.focusTransactionManager

internal fun FocusTargetNode.invalidateFocusTarget() {
    requireOwner().focusOwner.scheduleInvalidation(this)
}