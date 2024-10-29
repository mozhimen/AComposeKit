package com.mozhimen.composek.ui.focus

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusProperties
import androidx.compose.ui.focus.FocusRequester

/**
 * @ClassName FocusProperties
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal class FocusPropertiesImpl : FocusProperties {
    override var canFocus: Boolean = true
    override var next: FocusRequester = FocusRequester.Default
    override var previous: FocusRequester = FocusRequester.Default
    override var up: FocusRequester = FocusRequester.Default
    override var down: FocusRequester = FocusRequester.Default
    override var left: FocusRequester = FocusRequester.Default
    override var right: FocusRequester = FocusRequester.Default
    override var start: FocusRequester = FocusRequester.Default
    override var end: FocusRequester = FocusRequester.Default
    @OptIn(ExperimentalComposeUiApi::class)
    override var enter: (FocusDirection) -> FocusRequester = { FocusRequester.Default }
    @OptIn(ExperimentalComposeUiApi::class)
    override var exit: (FocusDirection) -> FocusRequester = { FocusRequester.Default }
}