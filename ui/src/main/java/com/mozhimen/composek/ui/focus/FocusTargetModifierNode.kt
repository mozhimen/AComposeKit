package com.mozhimen.composek.ui.focus

import androidx.compose.ui.ExperimentalComposeUiApi
import com.mozhimen.composek.ui.node.DelegatableNode

/**
 * @ClassName FocusTargetModifierNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * This modifier node can be used to create a modifier that makes a component focusable.
 */
sealed interface FocusTargetModifierNode : DelegatableNode {
    /**
     * The [FocusState] associated with this [FocusTargetModifierNode]. When you implement a
     * [FocusTargetModifierNode], instead of implementing [FocusEventModifierNode], you can get the
     * state by accessing this variable.
     */
    @ExperimentalComposeUiApi
    val focusState: FocusState
}