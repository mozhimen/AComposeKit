package com.mozhimen.composek.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import com.mozhimen.composek.ui.input.OverScrollState
import kotlin.math.roundToInt

/**
 * @ClassName LayoutFlexible
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/23
 * @Version 1.0
 */
@Composable
fun FlexibleLayout(
    modifier: Modifier = Modifier,
    state: OverScrollState,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.nestedScroll(state),
        content = content,
        measurePolicy = { measurables, constraints ->
            val contentPlaceable = measurables[0].measure(constraints)
            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceable.placeRelative(0, state.offset.roundToInt())
            }
        }
    )
}