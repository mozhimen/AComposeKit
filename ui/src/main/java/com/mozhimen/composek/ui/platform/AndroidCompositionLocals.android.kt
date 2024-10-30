package com.mozhimen.composek.ui.platform

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.AndroidUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.mozhimen.composek.ui.res.ImageVectorCache

/**
 * @ClassName AndroidCompositionLocals
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The Android [Configuration]. The [Configuration] is useful for determining how to organize the
 * UI.
 */
val LocalConfiguration = compositionLocalOf<Configuration> {
    noLocalProvidedFor("LocalConfiguration")
}

internal val LocalImageVectorCache = staticCompositionLocalOf<ImageVectorCache> {
    noLocalProvidedFor("LocalImageVectorCache")
}

/**
 * The CompositionLocal containing the current [LifecycleOwner].
 */
val LocalLifecycleOwner = staticCompositionLocalOf<LifecycleOwner> {
    noLocalProvidedFor("LocalLifecycleOwner")
}

/**
 * The CompositionLocal containing the current [SavedStateRegistryOwner].
 */
val LocalSavedStateRegistryOwner = staticCompositionLocalOf<SavedStateRegistryOwner> {
    noLocalProvidedFor("LocalSavedStateRegistryOwner")
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun ProvideAndroidCompositionLocals(
    owner: AndroidComposeView,
    content: @Composable () -> Unit
) {
    val view = owner
    val context = view.context
    // Make a deep copy to compare to later, since the same configuration object will be mutated
    // as part of configuration changes
    var configuration by remember {
        mutableStateOf(Configuration(context.resources.configuration))
    }

    owner.configurationChangeObserver = { configuration = Configuration(it) }

    val uriHandler = remember { AndroidUriHandler(context) }
    val viewTreeOwners = owner.viewTreeOwners ?: throw IllegalStateException(
        "Called when the ViewTreeOwnersAvailability is not yet in Available state"
    )

    val saveableStateRegistry = remember {
        DisposableSaveableStateRegistry(view, viewTreeOwners.savedStateRegistryOwner)
    }
    DisposableEffect(Unit) {
        onDispose {
            saveableStateRegistry.dispose()
        }
    }

    val imageVectorCache = obtainImageVectorCache(context, configuration)
    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides context,
        LocalLifecycleOwner provides viewTreeOwners.lifecycleOwner,
        LocalSavedStateRegistryOwner provides viewTreeOwners.savedStateRegistryOwner,
        LocalSaveableStateRegistry provides saveableStateRegistry,
        LocalView provides owner.view,
        LocalImageVectorCache provides imageVectorCache
    ) {
        ProvideCommonCompositionLocals(
            owner = owner,
            uriHandler = uriHandler,
            content = content
        )
    }
}

@Stable
@Composable
private fun obtainImageVectorCache(
    context: Context,
    configuration: Configuration?
): ImageVectorCache {
    val imageVectorCache = remember { ImageVectorCache() }
    val currentConfiguration: Configuration = remember {
        Configuration().apply { configuration?.let { this.setTo(it) } }
    }
    val callbacks = remember {
        object : ComponentCallbacks2 {
            override fun onConfigurationChanged(configuration: Configuration) {
                val changedFlags = currentConfiguration.updateFrom(configuration)
                imageVectorCache.prune(changedFlags)
                currentConfiguration.setTo(configuration)
            }

            override fun onLowMemory() {
                imageVectorCache.clear()
            }

            override fun onTrimMemory(level: Int) {
                imageVectorCache.clear()
            }
        }
    }
    DisposableEffect(imageVectorCache) {
        context.applicationContext.registerComponentCallbacks(callbacks)
        onDispose {
            context.applicationContext.unregisterComponentCallbacks(callbacks)
        }
    }
    return imageVectorCache
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}