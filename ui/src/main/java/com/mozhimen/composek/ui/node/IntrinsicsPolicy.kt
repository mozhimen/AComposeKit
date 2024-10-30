package com.mozhimen.composek.ui.node

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mozhimen.composek.ui.layout.MeasurePolicy

/**
 * @ClassName IntrinsicsPolicy
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 19:19
 * @Version 1.0
 */
/**
 * Calculates intrinsic measurements. The queries are backed by state depending on the layout
 * node's [MeasurePolicy], such that when the policy is changing, ancestors depending on the
 * result of these intrinsic measurements have their own layout recalculated.
 */
internal class IntrinsicsPolicy(val layoutNode: LayoutNode) {
    private var measurePolicyState: MeasurePolicy? by mutableStateOf(null)

    fun updateFrom(measurePolicy: MeasurePolicy) {
        measurePolicyState = measurePolicy
    }

    fun minIntrinsicWidth(height: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.minIntrinsicWidth(layoutNode.childMeasurables, height)
    }

    fun minIntrinsicHeight(width: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.minIntrinsicHeight(layoutNode.childMeasurables, width)
    }

    fun maxIntrinsicWidth(height: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.maxIntrinsicWidth(layoutNode.childMeasurables, height)
    }

    fun maxIntrinsicHeight(width: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.maxIntrinsicHeight(layoutNode.childMeasurables, width)
    }

    fun minLookaheadIntrinsicWidth(height: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.minIntrinsicWidth(
            layoutNode.childLookaheadMeasurables,
            height
        )
    }

    fun minLookaheadIntrinsicHeight(width: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.minIntrinsicHeight(
            layoutNode.childLookaheadMeasurables,
            width
        )
    }

    fun maxLookaheadIntrinsicWidth(height: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.maxIntrinsicWidth(
            layoutNode.childLookaheadMeasurables,
            height
        )
    }

    fun maxLookaheadIntrinsicHeight(width: Int) = with(measurePolicyFromState()) {
        layoutNode.outerCoordinator.maxIntrinsicHeight(
            layoutNode.childLookaheadMeasurables,
            width
        )
    }

    private fun measurePolicyFromState(): MeasurePolicy {
        return measurePolicyState ?: error(NoPolicyError)
    }

    private companion object {
        private const val NoPolicyError =
            "Intrinsic size is queried but there is no measure policy in place."
    }
}
