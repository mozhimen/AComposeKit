package com.mozhimen.composek.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mozhimen.composek.ui.input.NestedScrollState

/**
 * @ClassName TopBar
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/22
 * @Version 1.0
 */
@Composable
private fun BaseTopBar(
    state: NestedScrollState,
    color: Color,
    barHeight: Dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val fraction = remember {
        mutableFloatStateOf(0f)
    }
    val headImgBottom = with(LocalDensity.current) {
        96.dp.toPx()
    }
    LaunchedEffect(state.offset) {
        // 滚动到头像底部即不透明 到头像底部需要滚动16.dp+80.dp
        fraction.floatValue = (-state.offset / headImgBottom).coerceIn(0f, 1f)
    }
    val dynamicColor = remember { derivedStateOf { color.copy(alpha = fraction.floatValue) } }
//    val offsetY by animateFloatAsState(
//        targetValue = if (fraction.floatValue == 1f) 0f else with(LocalDensity.current) { 50.dp.toPx() },
//        label = "SmallHeadImgAnimate"
//    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(dynamicColor.value)
            .clipToBounds(),
        content = content
    )
    //{
//        IconButton(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                .alpha(if (isSearchMode) 0f else 1f),
//            onClick = {
//                clickMenu()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White
//            )
//        }
//        IconButton(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .alpha(if (isSearchMode) 0f else 1f),
//            onClick = {
//                clickShare()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Share, contentDescription = "Share", tint = Color.White
//            )
//        }
//        AsyncImage(
//            model = R.drawable.icon_hanbao,
//            contentDescription = "smallHeadImg",
//            modifier = Modifier
//                .align(Alignment.Center)
//                .graphicsLayer {
//                    translationY = offsetY
//                }
//                .size(24.dp)
//                .clip(RoundedCornerShape(24.dp))
//                .border(1.dp, Color.White, RoundedCornerShape(24.dp))
//        )
    //}
}