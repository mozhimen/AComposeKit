package com.mozhimen.composek.ui.input.rotary

/**
 * @ClassName RotaryScrollEvent
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:59
 * @Version 1.0
 */
/**
 * This event represents a rotary input event.
 *
 * Some Wear OS devices contain a physical rotating side button, or a rotating bezel. When the user
 * turns the button or rotates the bezel, a [RotaryScrollEvent] is sent to the item in focus.
 */
/*actual*/ class RotaryScrollEvent internal constructor(
    /**
     * The amount to scroll (in pixels) in response to a [RotaryScrollEvent] in a container that
     * can scroll vertically.
     */
    /*actual*/ val verticalScrollPixels: Float,

    /**
     * The amount to scroll (in pixels) in response to a [RotaryScrollEvent] in a container that
     * can scroll horizontally.
     */
    /*actual*/ val horizontalScrollPixels: Float,

    /**
     * The time in milliseconds at which this even occurred. The start (`0`) time is
     * platform-dependent.
     */
    /*actual*/ val uptimeMillis: Long,

    /**
     * The id for the input device that this event came from
     */
    val inputDeviceId: Int
) {
    override fun equals(other: Any?): Boolean = other is RotaryScrollEvent &&
            other.verticalScrollPixels == verticalScrollPixels &&
            other.horizontalScrollPixels == horizontalScrollPixels &&
            other.uptimeMillis == uptimeMillis &&
            other.inputDeviceId == inputDeviceId

    override fun hashCode(): Int = 0
        .let { verticalScrollPixels.hashCode() }
        .let { 31 * it + horizontalScrollPixels.hashCode() }
        .let { 31 * it + uptimeMillis.hashCode() }
        .let { 31 * it + inputDeviceId.hashCode() }

    override fun toString(): String = "RotaryScrollEvent(" +
            "verticalScrollPixels=$verticalScrollPixels," +
            "horizontalScrollPixels=$horizontalScrollPixels," +
            "uptimeMillis=$uptimeMillis," +
            "deviceId=$inputDeviceId)"
}
