package com.mozhimen.composek.ui.platform

import androidx.annotation.MainThread
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.ReusableComposition
import com.mozhimen.composek.ui.node.LayoutNode
import com.mozhimen.composek.ui.node.UiApplier

/**
 * @ClassName Wrapper
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
// TODO(chuckj): This is a temporary work-around until subframes exist so that
// nextFrame() inside recompose() doesn't really start a new frame, but a new subframe
// instead.
@MainThread
internal fun createSubcomposition(
    container: LayoutNode,
    parent: CompositionContext
): ReusableComposition = ReusableComposition(
    UiApplier(container),
    parent
)