package com.mozhimen.composek.ui.platform

import android.content.Context
import android.os.Build
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.AccessibilityManager

/**
 * @ClassName AndroidAccessibilityManager
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:01
 * @Version 1.0
 */
/**
 * Android implementation for [AccessibilityManager].
 */
internal class AndroidAccessibilityManager(context: Context) : AccessibilityManager {
    private companion object {
        const val FlagContentIcons = 1
        const val FlagContentText = 2
        const val FlagContentControls = 4
    }
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as
            android.view.accessibility.AccessibilityManager

    override fun calculateRecommendedTimeoutMillis(
        originalTimeoutMillis: Long,
        containsIcons: Boolean,
        containsText: Boolean,
        containsControls: Boolean
    ): Long {
        if (originalTimeoutMillis >= Int.MAX_VALUE) {
            return originalTimeoutMillis
        }
        var uiContentFlags = 0
        if (containsIcons) {
            uiContentFlags = uiContentFlags or FlagContentIcons
        }
        if (containsText) {
            uiContentFlags = uiContentFlags or FlagContentText
        }
        if (containsControls) {
            uiContentFlags = uiContentFlags or FlagContentControls
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val recommended = Api29Impl.getRecommendedTimeoutMillis(
                accessibilityManager,
                originalTimeoutMillis.toInt(),
                uiContentFlags
            )
            if (recommended == Int.MAX_VALUE) {
                Long.MAX_VALUE
            } else {
                recommended.toLong()
            }
        } else if (containsControls && accessibilityManager.isTouchExplorationEnabled) {
            Long.MAX_VALUE
        } else {
            originalTimeoutMillis
        }
    }
}

/**
 * This class is here to ensure that the classes that use this API will get verified and can be
 * AOT compiled. It is expected that this class will soft-fail verification, but the classes
 * which use this method will pass.
 */
@RequiresApi(Build.VERSION_CODES.Q)
internal object Api29Impl {
    @DoNotInline
    fun getRecommendedTimeoutMillis(
        accessibilityManager: android.view.accessibility.AccessibilityManager,
        originalTimeout: Int,
        uiContentFlags: Int
    ): Int {
        return accessibilityManager.getRecommendedTimeoutMillis(originalTimeout, uiContentFlags)
    }
}
