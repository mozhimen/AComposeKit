package com.mozhimen.composek.ui.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import com.mozhimen.composek.ui.Modifier

/**
 * @ClassName ModifierLocalConsumer
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/29 23:55
 * @Version 1.0
 */
/**
 * A Modifier that can be used to consume [ModifierLocal]s that were provided by other modifiers to
 * the left of this modifier, or above this modifier in the layout tree.
 */
@Stable
//@JvmDefaultWithCompatibility
interface ModifierLocalConsumer : Modifier.Element {
    /**
     * This function is called whenever one of the consumed values has changed.
     * This could be called in response to the modifier being added, removed or re-ordered.
     */
    fun onModifierLocalsUpdated(scope: ModifierLocalReadScope)
}

/**
 * A Modifier that can be used to consume [ModifierLocal]s that were provided by other modifiers to
 * the left of this modifier, or above this modifier in the layout tree.
 */
@Stable
@ExperimentalComposeUiApi
fun Modifier.modifierLocalConsumer(consumer: ModifierLocalReadScope.() -> Unit): Modifier {
    return this.then(
        ModifierLocalConsumerImpl(
            consumer,
            debugInspectorInfo {
                name = "modifierLocalConsumer"
                properties["consumer"] = consumer
            }
        )
    )
}

@Stable
private class ModifierLocalConsumerImpl(
    val consumer: ModifierLocalReadScope.() -> Unit,
    debugInspectorInfo: InspectorInfo.() -> Unit
) : ModifierLocalConsumer, InspectorValueInfo(debugInspectorInfo) {

    override fun onModifierLocalsUpdated(scope: ModifierLocalReadScope) {
        consumer.invoke(scope)
    }

    override fun equals(other: Any?): Boolean {
        return other is ModifierLocalConsumerImpl && other.consumer == consumer
    }

    override fun hashCode() = consumer.hashCode()
}
