package com.mozhimen.composek.ui.focus

import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InspectorInfo
import com.mozhimen.composek.ui.Modifier
import com.mozhimen.composek.ui.node.FocusPropertiesModifierNode
import com.mozhimen.composek.ui.node.ModifierNodeElement
import com.mozhimen.composek.ui.node.Nodes
import com.mozhimen.composek.ui.node.requireLayoutNode
import com.mozhimen.composek.ui.node.visitChildren
import com.mozhimen.composek.ui.node.currentValueOf
/**
 * @ClassName FocusRestorer
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
@Suppress("ConstPropertyName")
private const val PrevFocusedChild = "previouslyFocusedChildHash"

@ExperimentalComposeUiApi
internal fun FocusTargetNode.saveFocusedChild(): Boolean {
    if (!focusState.hasFocus) return false
    visitChildren(Nodes.FocusTarget) {
        if (it.focusState.hasFocus) {
            previouslyFocusedChildHash = it.requireLayoutNode().compositeKeyHash
            currentValueOf(LocalSaveableStateRegistry)
                ?.registerProvider(PrevFocusedChild) { previouslyFocusedChildHash }
            return true
        }
    }
    return false
}

@ExperimentalComposeUiApi
internal fun FocusTargetNode.restoreFocusedChild(): Boolean {
    if (previouslyFocusedChildHash == 0) {
        val savableStateRegistry = currentValueOf(LocalSaveableStateRegistry)
        savableStateRegistry?.consumeRestored(PrevFocusedChild)?.let {
            previouslyFocusedChildHash = it as Int
        }
    }
    if (previouslyFocusedChildHash == 0) return false
    visitChildren(Nodes.FocusTarget) {
        if (it.requireLayoutNode().compositeKeyHash == previouslyFocusedChildHash) {
            return it.restoreFocusedChild() || it.requestFocus()
        }
    }
    return false
}

// TODO: Move focusRestorer to foundation after saveFocusedChild and restoreFocusedChild are stable.
/**
 * This modifier can be used to save and restore focus to a focus group.
 * When focus leaves the focus group, it stores a reference to the item that was previously focused.
 * Then when focus re-enters this focus group, it restores focus to the previously focused item.
 *
 * @param onRestoreFailed callback provides a lambda that is invoked if focus restoration fails.
 * This lambda can be used to return a custom fallback item by providing a [FocusRequester]
 * attached to that item. This can be used to customize the initially focused item.
 *
 * @sample androidx.compose.ui.samples.FocusRestorerSample
 * @sample androidx.compose.ui.samples.FocusRestorerCustomFallbackSample
 */
@ExperimentalComposeUiApi
fun Modifier.focusRestorer(
    onRestoreFailed: (() -> FocusRequester)? = null
): Modifier = this then FocusRestorerElement(onRestoreFailed)

internal class FocusRestorerNode(
    var onRestoreFailed: (() -> FocusRequester)?
) : FocusPropertiesModifierNode, FocusRequesterModifierNode, Modifier.Node() {
    private val onExit: (FocusDirection) -> FocusRequester = {
        @OptIn(ExperimentalComposeUiApi::class)
        saveFocusedChild()
        FocusRequester.Default
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private val onEnter: (FocusDirection) -> FocusRequester = {
        @OptIn(ExperimentalComposeUiApi::class)
        if (restoreFocusedChild()) {
            FocusRequester.Cancel
        } else {
            onRestoreFailed?.invoke() ?: FocusRequester.Default
        }
    }

    override fun applyFocusProperties(focusProperties: FocusProperties) {
        @OptIn(ExperimentalComposeUiApi::class)
        focusProperties.enter = onEnter
        @OptIn(ExperimentalComposeUiApi::class)
        focusProperties.exit = onExit
    }
}

private data class FocusRestorerElement(
    val onRestoreFailed: (() -> FocusRequester)?
) : ModifierNodeElement<FocusRestorerNode>() {
    override fun create() = FocusRestorerNode(onRestoreFailed)

    override fun update(node: FocusRestorerNode) {
        node.onRestoreFailed = onRestoreFailed
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "focusRestorer"
        properties["onRestoreFailed"] = onRestoreFailed
    }
}
