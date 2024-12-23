package com.mozhimen.composek.foundation.lazy

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * @ClassName LazyListState
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/14
 * @Version 1.0
 */
/**
 * Static field, contains all scroll values
 */
private val SaveMapScroll = mutableMapOf<String, KeyParamsScroll>()

private data class KeyParamsScroll(
    val value: Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param initial see [ScrollState.value]
 */
/**
 * val scrollState = rememberForeverScrollState("history_screen")
 *
 * Column(
 *   modifier = modifier
 *     .fillMaxSize()
 *     .verticalScroll(scrollState)
 * ) {
 *  ...
 * }
 */
@Composable
fun rememberForeverScrollState(
    key: String,
    initial: Int = 0
): ScrollState {
    val scrollState = rememberSaveable(saver = ScrollState.Saver) {
        val scrollValue: Int = SaveMapScroll[key]?.value ?: initial
        SaveMapScroll[key] = KeyParamsScroll(scrollValue)
        return@rememberSaveable ScrollState(scrollValue)
    }
    DisposableEffect(Unit) {
        onDispose {
            SaveMapScroll[key] = KeyParamsScroll(scrollState.value)
        }
    }
    return scrollState
}

/**
 * Static field, contains all scroll values
 */
private val SaveMapLazyListState = mutableMapOf<String, KeyParamsLazyListState>()

private data class KeyParamsLazyListState(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param params arguments for find different between equals screen
 * @param initialFirstVisibleItemIndex see [LazyListState.firstVisibleItemIndex]
 * @param initialFirstVisibleItemScrollOffset see [LazyListState.firstVisibleItemScrollOffset]
 */
@Composable
fun rememberForeverLazyListState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        var savedValue = SaveMapLazyListState[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
        val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
        LazyListState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            SaveMapLazyListState[key] = KeyParamsLazyListState(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}

/**
 * Static field, contains all scroll values
 */
private val SaveMapLazyGridState = mutableMapOf<String, KeyParamsLazyGridState>()

private data class KeyParamsLazyGridState(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param params arguments for find different between equals screen
 * @param initialFirstVisibleItemIndex see [LazyListState.firstVisibleItemIndex]
 * @param initialFirstVisibleItemScrollOffset see [LazyListState.firstVisibleItemScrollOffset]
 */
@Composable
fun rememberForeverLazyGridState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyGridState {
    val scrollState = rememberSaveable(saver = LazyGridState.Saver) {
        var savedValue = SaveMapLazyGridState[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
        val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
        LazyGridState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            SaveMapLazyGridState[key] = KeyParamsLazyGridState(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}