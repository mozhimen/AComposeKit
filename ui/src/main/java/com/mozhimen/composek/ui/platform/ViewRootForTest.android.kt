package com.mozhimen.composek.ui.platform

import android.view.View
import androidx.annotation.VisibleForTesting
import com.mozhimen.composek.ui.node.RootForTest

/**
 * @ClassName ViewRootForTest
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The marker interface to be implemented by the [View] backing the composition.
 * To be used in tests.
 */
@VisibleForTesting
interface ViewRootForTest : RootForTest {

    /**
     * The view backing this Owner.
     */
    val view: View

    /**
     * Returns true when the associated LifecycleOwner is in the resumed state
     */
    val isLifecycleInResumedState: Boolean

    /**
     * Whether the Owner has pending layout work.
     */
    val hasPendingMeasureOrLayout: Boolean

    /**
     * Called to invalidate the Android [View] sub-hierarchy handled by this [View].
     */
    fun invalidateDescendants()

    companion object {
        /**
         * Called after an View implementing [ViewRootForTest] is created. Used by
         * AndroidComposeTestRule to keep track of all attached ComposeViews. Not to be
         * set or used by any other component.
         */
        @VisibleForTesting
        var onViewCreatedCallback: ((ViewRootForTest) -> Unit)? = null
    }
}
