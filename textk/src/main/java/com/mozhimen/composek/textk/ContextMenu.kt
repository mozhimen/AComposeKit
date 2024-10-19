package com.mozhimen.composek.textk

import androidx.compose.runtime.Composable
import com.mozhimen.composek.textk.selection.SelectionManager
import com.mozhimen.composek.textk.selection.TextFieldSelectionManager

/**
 * @ClassName ContextMenu
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */
// TODO (b/269341173) remove inline once these composables are non-trivial

@Composable
internal inline fun ContextMenuArea(
    manager: TextFieldSelectionManager,
    content: @Composable () -> Unit
) {
    content()
}

@Composable
internal inline fun ContextMenuArea(
    manager: SelectionManager,
    content: @Composable () -> Unit
) {
    content()
}
