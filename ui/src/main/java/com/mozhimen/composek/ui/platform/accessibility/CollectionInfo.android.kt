package com.mozhimen.composek.ui.platform.accessibility

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastReduce
import androidx.compose.ui.util.fastZipWithNext
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.mozhimen.composek.ui.semantics.CollectionInfo
import com.mozhimen.composek.ui.semantics.CollectionItemInfo
import com.mozhimen.composek.ui.semantics.SemanticsNode
import com.mozhimen.composek.ui.semantics.SemanticsProperties
import com.mozhimen.composek.ui.semantics.getOrNull
import kotlin.math.abs

/**
 * @ClassName CollectionInfo
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:05
 * @Version 1.0
 */
internal fun setCollectionInfo(node: SemanticsNode, info: AccessibilityNodeInfoCompat) {
    // prioritise collection info provided by developer
    val collectionInfo = node.config.getOrNull(SemanticsProperties.CollectionInfo)
    if (collectionInfo != null) {
        info.setCollectionInfo(collectionInfo.toAccessibilityCollectionInfo())
        return
    }

    // if no collection info is provided, we'll check the 'SelectableGroup'
    val groupedChildren = mutableListOf<SemanticsNode>()

    if (node.config.getOrNull(SemanticsProperties.SelectableGroup) != null) {
        node.replacedChildren.fastForEach { childNode ->
            // we assume that Tabs and RadioButtons are not mixed under a single group
            if (childNode.config.contains(SemanticsProperties.Selected)) {
                groupedChildren.add(childNode)
            }
        }
    }

    if (groupedChildren.isNotEmpty()) {
        val isHorizontal = calculateIfHorizontallyStacked(groupedChildren)
        info.setCollectionInfo(
            AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(
                if (isHorizontal) 1 else groupedChildren.count(),
                if (isHorizontal) groupedChildren.count() else 1,
                false,
                AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_NONE
            )
        )
    }
}

internal fun setCollectionItemInfo(node: SemanticsNode, info: AccessibilityNodeInfoCompat) {
    // prioritise collection item info provided by developer
    val collectionItemInfo = node.config.getOrNull(SemanticsProperties.CollectionItemInfo)
    if (collectionItemInfo != null) {
        info.setCollectionItemInfo(collectionItemInfo.toAccessibilityCollectionItemInfo(node))
    }

    // if no collection item info is provided, we'll check the 'SelectableGroup'
    val parentNode = node.parent ?: return
    if (parentNode.config.getOrNull(SemanticsProperties.SelectableGroup) != null) {
        // first check if parent has a CollectionInfo. If it does and any of the counters is
        // unknown, then we assume that it is a lazy collection so we won't provide
        // collectionItemInfo using `SelectableGroup`
        val collectionInfo = parentNode.config.getOrNull(SemanticsProperties.CollectionInfo)
        if (collectionInfo != null && collectionInfo.isLazyCollection) return

        // `SelectableGroup` designed for selectable elements
        if (!node.config.contains(SemanticsProperties.Selected)) return

        val groupedChildren = mutableListOf<SemanticsNode>()

        // find all siblings to calculate the index
        var index = 0
        parentNode.replacedChildren.fastForEach { childNode ->
            if (childNode.config.contains(SemanticsProperties.Selected)) {
                groupedChildren.add(childNode)
                // Grouped children is ordered preferring zIndex
                if (childNode.layoutNode.placeOrder < node.layoutNode.placeOrder) {
                    index++
                }
            }
        }

        if (groupedChildren.isNotEmpty()) {
            val isHorizontal = calculateIfHorizontallyStacked(groupedChildren)
            val itemInfo = AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(
                if (isHorizontal) 0 else index,
                1,
                if (isHorizontal) index else 0,
                1,
                false,
                node.config.getOrElse(SemanticsProperties.Selected) { false }
            )
            if (itemInfo != null) {
                info.setCollectionItemInfo(itemInfo)
            }
        }
    }
}

internal fun SemanticsNode.hasCollectionInfo() =
    config.getOrNull(SemanticsProperties.CollectionInfo) != null ||
            config.getOrNull(SemanticsProperties.SelectableGroup) != null

/** A naïve algorithm to determine if elements are stacked vertically or horizontally */
private fun calculateIfHorizontallyStacked(items: List<SemanticsNode>): Boolean {
    if (items.count() < 2) return true

    val deltas = items.fastZipWithNext { el1, el2 ->
        Offset(
            abs(el1.boundsInRoot.center.x - el2.boundsInRoot.center.x),
            abs(el1.boundsInRoot.center.y - el2.boundsInRoot.center.y)
        )
    }
    val (deltaX, deltaY) = when (deltas.count()) {
        1 -> deltas.first()
        else -> deltas.fastReduce { result, element -> result + element }
    }
    return deltaY < deltaX
}

private val CollectionInfo.isLazyCollection get() = rowCount < 0 || columnCount < 0

private fun CollectionInfo.toAccessibilityCollectionInfo() =
    AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(
        rowCount,
        columnCount,
        false,
        AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_NONE
    )

private fun CollectionItemInfo.toAccessibilityCollectionItemInfo(itemNode: SemanticsNode) =
    AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(
        rowIndex,
        rowSpan,
        columnIndex,
        columnSpan,
        false,
        itemNode.config.getOrElse(SemanticsProperties.Selected) { false }
    )
