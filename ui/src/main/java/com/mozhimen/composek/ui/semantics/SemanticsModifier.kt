package com.mozhimen.composek.ui.semantics

import androidx.compose.ui.platform.AtomicInt

/**
 * @ClassName SemanticsModifier
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */

private var lastIdentifier = AtomicInt(0)
internal fun generateSemanticsId() = lastIdentifier.addAndGet(1)
