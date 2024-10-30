package com.mozhimen.composek.ui.platform

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillTree
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.mozhimen.composek.ui.focus.FocusManager
import com.mozhimen.composek.ui.input.InputModeManager
import com.mozhimen.composek.ui.input.pointer.PointerIconService
import com.mozhimen.composek.ui.node.Owner

/**
 * @ClassName CompositionLocals
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 1:43
 * @Version 1.0
 */
/**
 * The CompositionLocal to provide communication with platform accessibility service.
 */
val LocalAccessibilityManager = staticCompositionLocalOf<AccessibilityManager?> { null }

/**
 * The CompositionLocal that can be used to trigger autofill actions.
 * Eg. [Autofill.requestAutofillForNode].
 */
@Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
val LocalAutofill = staticCompositionLocalOf<Autofill?> { null }

/**
 * The CompositionLocal that can be used to add
 * [AutofillNode][import androidx.compose.ui.autofill.AutofillNode]s to the autofill tree. The
 * [AutofillTree] is a temporary data structure that will be replaced by Autofill Semantics
 * (b/138604305).
 */
@Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
@get:ExperimentalComposeUiApi
@ExperimentalComposeUiApi
val LocalAutofillTree = staticCompositionLocalOf<AutofillTree> {
    noLocalProvidedFor("LocalAutofillTree")
}

/**
 * The CompositionLocal to provide communication with platform clipboard service.
 */
val LocalClipboardManager = staticCompositionLocalOf<ClipboardManager> {
    noLocalProvidedFor("LocalClipboardManager")
}

/**
 * Provides the [Density] to be used to transform between [density-independent pixel
 * units (DP)][androidx.compose.ui.unit.Dp] and pixel units or
 * [scale-independent pixel units (SP)][androidx.compose.ui.unit.TextUnit] and
 * pixel units. This is typically used when a
 * [DP][androidx.compose.ui.unit.Dp] is provided and it must be converted in the body of
 * [Layout] or [DrawModifier].
 */
//val LocalDensity = staticCompositionLocalOf<Density> {
//    noLocalProvidedFor("LocalDensity")
//}

/**
 * The CompositionLocal that can be used to control focus within Compose.
 */
val LocalFocusManager = staticCompositionLocalOf<FocusManager> {
    noLocalProvidedFor("LocalFocusManager")
}

/**
 * The CompositionLocal to provide platform font loading methods.
 */
@Suppress("DEPRECATION")
@Deprecated("LocalFontLoader is replaced with LocalFontFamilyResolver",
    replaceWith = ReplaceWith("LocalFontFamilyResolver")
)
@get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
val LocalFontLoader = staticCompositionLocalOf<Font.ResourceLoader> {
    noLocalProvidedFor("LocalFontLoader")
}

/**
 * The CompositionLocal for compose font resolution from FontFamily.
 */
//val LocalFontFamilyResolver = staticCompositionLocalOf<FontFamily.Resolver> {
//    noLocalProvidedFor("LocalFontFamilyResolver")
//}

/**
 * The CompositionLocal to provide haptic feedback to the user.
 */
val LocalHapticFeedback = staticCompositionLocalOf<HapticFeedback> {
    noLocalProvidedFor("LocalHapticFeedback")
}

/**
 * The CompositionLocal to provide an instance of InputModeManager which controls the current
 * input mode.
 */
val LocalInputModeManager = staticCompositionLocalOf<InputModeManager> {
    noLocalProvidedFor("LocalInputManager")
}

/**
 * The CompositionLocal to provide the layout direction.
 */
//val LocalLayoutDirection = staticCompositionLocalOf<LayoutDirection> {
//    noLocalProvidedFor("LocalLayoutDirection")
//}

/**
 * The CompositionLocal to provide communication with platform text input service.
 */
val LocalTextInputService = staticCompositionLocalOf<TextInputService?> { null }

/**
 * The [CompositionLocal] to provide a [SoftwareKeyboardController] that can control the current
 * software keyboard.
 *
 * Will be null if the software keyboard cannot be controlled.
 */
val LocalSoftwareKeyboardController = staticCompositionLocalOf<SoftwareKeyboardController?> { null }

/**
 * The CompositionLocal to provide text-related toolbar.
 */
val LocalTextToolbar = staticCompositionLocalOf<TextToolbar> {
    noLocalProvidedFor("LocalTextToolbar")
}

/**
 * The CompositionLocal to provide functionality related to URL, e.g. open URI.
 */
val LocalUriHandler = staticCompositionLocalOf<UriHandler> {
    noLocalProvidedFor("LocalUriHandler")
}

/**
 * The CompositionLocal that provides the ViewConfiguration.
 */
//val LocalViewConfiguration = staticCompositionLocalOf<ViewConfiguration> {
//    noLocalProvidedFor("LocalViewConfiguration")
//}

/**
 * The CompositionLocal that provides information about the window that hosts the current [Owner].
 */
val LocalWindowInfo = staticCompositionLocalOf<WindowInfo> {
    noLocalProvidedFor("LocalWindowInfo")
}

internal val LocalPointerIconService = staticCompositionLocalOf<PointerIconService?> {
    null
}

@ExperimentalComposeUiApi
@Composable
internal fun ProvideCommonCompositionLocals(
    owner: Owner,
    uriHandler: UriHandler,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAccessibilityManager provides owner.accessibilityManager,
        LocalAutofill provides owner.autofill,
        LocalAutofillTree provides owner.autofillTree,
        LocalClipboardManager provides owner.clipboardManager,
        LocalDensity provides owner.density,
        LocalFocusManager provides owner.focusOwner,
        @Suppress("DEPRECATION") LocalFontLoader
                providesDefault @Suppress("DEPRECATION") owner.fontLoader,
        LocalFontFamilyResolver providesDefault owner.fontFamilyResolver,
        LocalHapticFeedback provides owner.hapticFeedBack,
        LocalInputModeManager provides owner.inputModeManager,
        LocalLayoutDirection provides owner.layoutDirection,
        LocalTextInputService provides owner.textInputService,
        LocalSoftwareKeyboardController provides owner.softwareKeyboardController,
        LocalTextToolbar provides owner.textToolbar,
        LocalUriHandler provides uriHandler,
        LocalViewConfiguration provides owner.viewConfiguration,
        LocalWindowInfo provides owner.windowInfo,
        LocalPointerIconService provides owner.pointerIconService,
        content = content
    )
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
