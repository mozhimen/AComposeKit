package com.mozhimen.composek.ui.input.pointer

import android.view.PointerIcon.TYPE_CROSSHAIR
import android.view.PointerIcon.TYPE_DEFAULT
import android.view.PointerIcon.TYPE_HAND
import android.view.PointerIcon.TYPE_TEXT

/**
 * @ClassName PointerIcon
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:41
 * @Version 1.0
 */
internal class AndroidPointerIconType(val type: Int) :
    PointerIcon {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidPointerIconType

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type
    }

    override fun toString(): String {
        return "AndroidPointerIcon(type=$type)"
    }
}

internal class AndroidPointerIcon(val pointerIcon: android.view.PointerIcon) :
    PointerIcon {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidPointerIcon

        return pointerIcon == other.pointerIcon
    }

    override fun hashCode(): Int {
        return pointerIcon.hashCode()
    }

    override fun toString(): String {
        return "AndroidPointerIcon(pointerIcon=$pointerIcon)"
    }
}

/**
 * Creates [PointerIcon] from [android.view.PointerIcon]
 */
fun PointerIcon(pointerIcon: android.view.PointerIcon): PointerIcon =
    AndroidPointerIcon(pointerIcon)

/**
 * Creates [PointerIcon] from pointer icon type (see [android.view.PointerIcon.getSystemIcon]
 */
fun PointerIcon(pointerIconType: Int): PointerIcon =
    AndroidPointerIconType(pointerIconType)

internal /*actua*/ val pointerIconDefault: PointerIcon = AndroidPointerIconType(TYPE_DEFAULT)
internal /*actua*/ val pointerIconCrosshair: PointerIcon = AndroidPointerIconType(TYPE_CROSSHAIR)
internal /*actua*/ val pointerIconText: PointerIcon = AndroidPointerIconType(TYPE_TEXT)
internal /*actua*/ val pointerIconHand: PointerIcon = AndroidPointerIconType(TYPE_HAND)
