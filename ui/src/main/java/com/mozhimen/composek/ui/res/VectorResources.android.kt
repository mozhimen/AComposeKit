package com.mozhimen.composek.ui.res

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.ui.graphics.vector.ImageVector
import java.lang.ref.WeakReference

/**
 * @ClassName VectorResources
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * Object responsible for caching [ImageVector] instances
 * based on the given theme and drawable resource identifier
 */
internal class ImageVectorCache {

    /**
     * Key that binds the corresponding theme with the resource identifier for the vector asset
     */
    data class Key(
        val theme: Resources.Theme,
        val id: Int
    )

    /**
     * Tuple that contains the [ImageVector] as well as the corresponding configuration flags
     * that the [ImageVector] depends on. That is if there is a configuration change that updates
     * the parameters in the flag, this vector should be regenerated from the current configuration
     */
    data class ImageVectorEntry(
        val imageVector: ImageVector,
        val configFlags: Int
    )

    private val map = HashMap<Key, WeakReference<ImageVectorEntry>>()

    operator fun get(key: Key): ImageVectorEntry? = map[key]?.get()

    fun prune(configChanges: Int) {
        val it = map.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val imageVectorEntry = entry.value.get()
            if (imageVectorEntry == null ||
                Configuration.needNewResources(configChanges, imageVectorEntry.configFlags)
            ) {
                it.remove()
            }
        }
    }

    operator fun set(key: Key, imageVectorEntry: ImageVectorEntry) {
        map[key] = WeakReference<ImageVectorEntry>(imageVectorEntry)
    }

    fun clear() {
        map.clear()
    }
}
