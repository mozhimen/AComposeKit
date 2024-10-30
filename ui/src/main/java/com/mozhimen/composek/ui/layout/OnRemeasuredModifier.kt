package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntSize
import com.mozhimen.composek.ui.Modifier

/**
 * @ClassName OnRemeasuredModifier
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 20:18
 * @Version 1.0
 */
/**
 * Invoked with the size of the modified Compose UI element when the element is first measured or
 * when the size of the element changes.
 *
 * There are no guarantees `onSizeChanged` will not be re-invoked with the same size.
 *
 * Using the `onSizeChanged` size value in a [MutableState] to update layout causes the new size
 * value to be read and the layout to be recomposed in the succeeding frame, resulting in a one
 * frame lag.
 *
 * You can use `onSizeChanged` to affect drawing operations. Use [Layout] or [SubcomposeLayout] to
 * enable the size of one component to affect the size of another.
 *
 * Example usage:
 * @sample androidx.compose.ui.samples.OnSizeChangedSample
 */
@Stable
fun Modifier.onSizeChanged(
    onSizeChanged: (IntSize) -> Unit
) = this.then(
    OnSizeChangedModifier(
        onSizeChanged = onSizeChanged,
        inspectorInfo = debugInspectorInfo {
            name = "onSizeChanged"
            properties["onSizeChanged"] = onSizeChanged
        }
    )
)

private class OnSizeChangedModifier(
    val onSizeChanged: (IntSize) -> Unit,
    inspectorInfo: InspectorInfo.() -> Unit
) : OnRemeasuredModifier, InspectorValueInfo(inspectorInfo) {
    private var previousSize = IntSize(Int.MIN_VALUE, Int.MIN_VALUE)

    override fun onRemeasured(size: IntSize) {
        if (previousSize != size) {
            onSizeChanged(size)
            previousSize = size
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OnSizeChangedModifier) return false

        return onSizeChanged == other.onSizeChanged
    }

    override fun hashCode(): Int {
        return onSizeChanged.hashCode()
    }
}

/**
 * A modifier whose [onRemeasured] is called when the layout content is remeasured. The
 * most common usage is [onSizeChanged].
 *
 * Example usage:
 * @sample androidx.compose.ui.samples.OnSizeChangedSample
 */
//@JvmDefaultWithCompatibility
interface OnRemeasuredModifier : Modifier.Element {
    /**
     * Called after a layout's contents have been remeasured.
     */
    fun onRemeasured(size: IntSize)
}