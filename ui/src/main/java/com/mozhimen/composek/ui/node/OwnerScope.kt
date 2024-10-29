package com.mozhimen.composek.ui.node

/**
 * @ClassName OwnerScope
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Read observation scopes used in layout and drawing must implement this interface to let the
 * snapshot observer know when the scope has been removed and should no longer be observed.
 *
 * /*@see OwnerSnapshotObserver.observeReads*/
 */
internal interface OwnerScope {
    /**
     * `true` when the scope is still in the hierarchy and `false` once it has been removed and
     * observations are no longer necessary.
     */
    val isValidOwnerScope: Boolean
}
