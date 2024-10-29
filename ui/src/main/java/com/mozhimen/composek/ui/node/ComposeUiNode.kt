package com.mozhimen.composek.ui.node

import androidx.compose.runtime.CompositionLocalMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * @ClassName ComposeUiNode
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Interface extracted from LayoutNode to not mark the whole LayoutNode class as @PublishedApi.
 */
@PublishedApi
internal interface ComposeUiNode {
    var measurePolicy: MeasurePolicy
    var layoutDirection: LayoutDirection
    var density: Density
    var modifier: Modifier
    var viewConfiguration: ViewConfiguration
    var compositionLocalMap: CompositionLocalMap
    @ExperimentalComposeUiApi
    var compositeKeyHash: Int

    /**
     * Object of pre-allocated lambdas used to make use with ComposeNode allocation-less.
     */
    companion object {
        val Constructor: () -> ComposeUiNode = LayoutNode.Constructor
        val VirtualConstructor: () -> ComposeUiNode = { LayoutNode(isVirtual = true) }
        val SetModifier: ComposeUiNode.(Modifier) -> Unit = { this.modifier = it }
        val SetDensity: ComposeUiNode.(Density) -> Unit = { this.density = it }
        val SetResolvedCompositionLocals: ComposeUiNode.(CompositionLocalMap) -> Unit =
            { this.compositionLocalMap = it }
        val SetMeasurePolicy: ComposeUiNode.(MeasurePolicy) -> Unit =
            { this.measurePolicy = it }
        val SetLayoutDirection: ComposeUiNode.(LayoutDirection) -> Unit =
            { this.layoutDirection = it }
        val SetViewConfiguration: ComposeUiNode.(ViewConfiguration) -> Unit =
            { this.viewConfiguration = it }
        @get:ExperimentalComposeUiApi
        @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
        @ExperimentalComposeUiApi
        val SetCompositeKeyHash: ComposeUiNode.(Int) -> Unit =
            { this.compositeKeyHash = it }
    }
}
