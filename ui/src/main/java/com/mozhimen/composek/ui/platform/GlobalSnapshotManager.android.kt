package com.mozhimen.composek.ui.platform

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.platform.AndroidUiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ClassName GlobalSnapshotManager
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Platform-specific mechanism for starting a monitor of global snapshot state writes
 * in order to schedule the periodic dispatch of snapshot apply notifications.
 * This process should remain platform-specific; it is tied to the threading and update model of
 * a particular platform and framework target.
 *
 * Composition bootstrapping mechanisms for a particular platform/framework should call
 * [ensureStarted] during setup to initialize periodic global snapshot notifications.
 * For Android, these notifications are always sent on [AndroidUiDispatcher.Main]. Other platforms
 * may establish different policies for these notifications.
 */
internal object GlobalSnapshotManager {
    private val started = AtomicBoolean(false)
    private val sent = AtomicBoolean(false)

    fun ensureStarted() {
        if (started.compareAndSet(false, true)) {
            val channel = Channel<Unit>(1)
            CoroutineScope(AndroidUiDispatcher.Main).launch {
                channel.consumeEach {
                    sent.set(false)
                    Snapshot.sendApplyNotifications()
                }
            }
            Snapshot.registerGlobalWriteObserver {
                if (sent.compareAndSet(false, true)) {
                    channel.trySend(Unit)
                }
            }
        }
    }
}
