package com.mozhimen.composek.bar.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mozhimen.composek.ui.input.NestedScrollState
import com.mozhimen.composek.ui.input.OverScrollState
import com.mozhimen.composek.ui.input.rememberNestedScrollState
import com.mozhimen.composek.ui.input.rememberOverScrollState
import com.mozhimen.composek.ui.layout.FlexibleLayout
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}

@Composable
fun Greeting() {
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollState = rememberNestedScrollState(coroutineScope)
    CoordinateLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        nestedScrollState
    )
}

@Composable
private fun CoordinateLayout(
    modifier: Modifier = Modifier,
    nestedScrollState: NestedScrollState = rememberNestedScrollState(),
) {
    SubcomposeLayout(
        modifier = modifier,
        measurePolicy = { constraints ->
            val z1TopBar = subcompose(slotId = "topBar", content = {
                TopBar(nestedScrollState, Color.DarkGray)
            }).first().measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity))
            nestedScrollState.topBarHeight = z1TopBar.height
            //
            val z2contentLayout = subcompose(slotId = "contentLayout", content = {
                ContentLayout(z1TopBar.height, nestedScrollState)
            }).first().measure(constraints.copy(minHeight = 0, maxHeight = constraints.maxHeight))
            //
            layout(constraints.maxWidth, constraints.maxHeight) {
                z2contentLayout.placeRelative(0, 0)
                z1TopBar.placeRelative(0, 0)
            }
        }
    )
}

@Composable
private fun TopBar(
    state: NestedScrollState,
    bgColor: Color,
) {
    val fraction = remember {
        mutableFloatStateOf(0f)
    }
    val interval = with(LocalDensity.current) {
        (200.dp - 56.dp).toPx()//abs(state.noPinContentLayoutHeight - state.topBarHeight)/* 56.dp.toPx()*/
    }
    LaunchedEffect(state.offset) {
        // 滚动到头像底部即不透明 到头像底部需要滚动16.dp+80.dp
        fraction.floatValue = (-state.offset / interval).coerceIn(0f, 1f)
    }
    //
    val dynamicColor = remember {
        derivedStateOf { bgColor.copy(alpha = fraction.floatValue) }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(dynamicColor.value)
            .windowInsetsPadding(WindowInsets.statusBars)
            .clipToBounds(),
    )
}

@Composable
private fun ContentLayout(
    topBarHeight: Int,
    nestedScrollState: NestedScrollState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val overScrollState = rememberOverScrollState(coroutineScope)
    FlexibleLayout(
        state = overScrollState,
    ) {
        SubcomposeLayout(
            modifier = Modifier
                .fillMaxSize()
        ) { constraints ->
            val noPinContentLayout = subcompose("noPinContentLayout", content = { NoPinContentLayout(nestedScrollState) }).first().measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity))
            nestedScrollState.noPinContentLayoutHeight = noPinContentLayout.height
            //
            val scrollableContentLayout = subcompose("scrollableContentLayout", content = { ScrollableContentLayout(nestedScrollState) }).first().measure(constraints.copy(minHeight = 0, maxHeight = constraints.maxHeight - topBarHeight))
            layout(constraints.maxWidth, constraints.maxHeight) {
                noPinContentLayout.placeRelative(0, nestedScrollState.offset.toInt())
                scrollableContentLayout.placeRelative(0, noPinContentLayout.height + nestedScrollState.offset.toInt())
            }
        }
    }
}

@Composable
fun NoPinContentLayout(state: NestedScrollState) {
    //            val backgroundImage = subcompose("backgroundImage") {
//                AsyncImage(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .graphicsLayer {
//                            translationY = -overScrollState.indicatorOffset
//                        },
//                    model = R.drawable.pic_vitality,
//                    contentDescription = "background",
//                    contentScale = ContentScale.Crop,
//                )
//            }.first().measure(
//                constraints.copy(
//                    minHeight = (state.onPinContentLayoutHeight + overScrollState.indicatorOffset.coerceAtLeast(0f)).toInt(),
//                    maxHeight = Constraints.Infinity
//                )
//            )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Blue)
            .scrollable(
                state = state.scrollState,
                orientation = Orientation.Vertical
            )
    ) {
        Image(
            painter = painterResource(R.drawable.pic_vitality),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun ScrollableContentLayout(state: NestedScrollState) {
    Text(
        text = "Me 1",
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state.scrollState, Orientation.Vertical),
        textAlign = TextAlign.Center
    )
//    LazyColumn(
//        modifier =
//            Modifier
//                .fillMaxSize()
//                .nestedScroll(state)
//    ) {
//        items(
//            listOf(
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//            )
//        ) {
//            Text(
//                text = it,
//                fontSize = 16.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//            )
//        }
//    }
}

