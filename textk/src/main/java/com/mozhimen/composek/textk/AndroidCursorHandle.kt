package com.mozhimen.composek.textk

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.textk.selection.HandleReferencePoint
import com.mozhimen.composek.textk.selection.createHandleImage
import com.mozhimen.composek.textk.selection.HandlePopup
/**
 * @ClassName AndroidCursorHandle
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/18
 * @Version 1.0
 */

private const val Sqrt2 = 1.41421356f
internal val CursorHandleHeight = 25.dp
internal val CursorHandleWidth = CursorHandleHeight * 2f / (1 + Sqrt2)

@Composable
internal fun CursorHandle(
    handlePosition: Offset,
    modifier: Modifier,
    content: @Composable (() -> Unit)?
) {
    HandlePopup(
        positionProvider = { handlePosition },
        handleReferencePoint = HandleReferencePoint.TopMiddle
    ) {
        if (content == null) {
            DefaultCursorHandle(modifier = modifier)
        } else {
            content()
        }
    }
}

@Composable
/*@VisibleForTesting*/
internal fun DefaultCursorHandle(modifier: Modifier) {
    Spacer(modifier.size(CursorHandleWidth, CursorHandleHeight).drawCursorHandle())
}

internal fun Modifier.drawCursorHandle() = composed {
    val handleColor = LocalTextSelectionColors.current.handleColor
    this.then(
        Modifier.drawWithCache {
            // Cursor handle is the same as a SelectionHandle rotated 45 degrees clockwise.
            val radius = size.width / 2f
            val imageBitmap = createHandleImage(radius = radius)
            val colorFilter = ColorFilter.tint(handleColor)
            onDrawWithContent {
                drawContent()
                withTransform({
                    translate(left = radius)
                    rotate(degrees = 45f, pivot = Offset.Zero)
                }) {
                    drawImage(image = imageBitmap, colorFilter = colorFilter)
                }
            }
        }
    )
}
