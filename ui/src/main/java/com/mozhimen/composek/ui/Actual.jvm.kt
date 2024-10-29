package com.mozhimen.composek.ui

import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.util.fastForEach
import com.mozhimen.composek.ui.node.ModifierNodeElement

/**
 * @ClassName Actual
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal fun areObjectsOfSameType(a: Any, b: Any): Boolean {
    return a::class.java === b::class.java
}

// TODO: For non-JVM platforms, you can revive the kotlin-reflect implementation from
//  https://android-review.googlesource.com/c/platform/frameworks/support/+/2441379
internal fun InspectorInfo.tryPopulateReflectively(
    element: ModifierNodeElement<*>
) {
    element.javaClass.declaredFields
        // Sort by the field name to make the result more well-defined
        .sortedBy { it.name }
        .fastForEach { field ->
            if (!field.declaringClass.isAssignableFrom(ModifierNodeElement::class.java)) {
                try {
                    field.isAccessible = true
                    properties[field.name] = field.get(element)
                } catch (_: SecurityException) {
                    // Do nothing. Just ignore the field and prevent the error from crashing
                    // the application and ending the debugging session.
                } catch (_: IllegalAccessException) {
                    // Do nothing. Just ignore the field and prevent the error from crashing
                    // the application and ending the debugging session.
                }
            }
        }
}
