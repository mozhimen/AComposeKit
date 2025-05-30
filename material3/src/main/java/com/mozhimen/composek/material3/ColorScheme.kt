package com.mozhimen.composek.material3

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ChipColors
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SliderColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.mozhimen.composek.material3.tokens.ColorSchemeKeyTokens

/**
 * @ClassName ColorScheme
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/16 15:54
 * @Version 1.0
 */
/**
 * A color scheme holds all the named color parameters for a [MaterialTheme].
 *
 * Color schemes are designed to be harmonious, ensure accessible text, and distinguish UI
 * elements and surfaces from one another. There are two built-in baseline schemes,
 * [lightColorScheme] and a [darkColorScheme], that can be used as-is or customized.
 *
 * The Material color system and custom schemes provide default values for color as a starting point
 * for customization.
 *
 * To learn more about colors, see [Material Design colors](https://m3.material.io/styles/color/overview).
 *
 * @property primary The primary color is the color displayed most frequently across your app’s
 * screens and components.
 * @property onPrimary Color used for text and icons displayed on top of the primary color.
 * @property primaryContainer The preferred tonal color of containers.
 * @property onPrimaryContainer The color (and state variants) that should be used for content on
 * top of [primaryContainer].
 * @property inversePrimary Color to be used as a "primary" color in places where the inverse color
 * scheme is needed, such as the button on a SnackBar.
 * @property secondary The secondary color provides more ways to accent and distinguish your
 * product. Secondary colors are best for:
 * - Floating action buttons
 * - Selection controls, like checkboxes and radio buttons
 * - Highlighting selected text
 * - Links and headlines
 * @property onSecondary Color used for text and icons displayed on top of the secondary color.
 * @property secondaryContainer A tonal color to be used in containers.
 * @property onSecondaryContainer The color (and state variants) that should be used for content on
 * top of [secondaryContainer].
 * @property tertiary The tertiary color that can be used to balance primary and secondary
 * colors, or bring heightened attention to an element such as an input field.
 * @property onTertiary Color used for text and icons displayed on top of the tertiary color.
 * @property tertiaryContainer A tonal color to be used in containers.
 * @property onTertiaryContainer The color (and state variants) that should be used for content on
 * top of [tertiaryContainer].
 * @property background The background color that appears behind scrollable content.
 * @property onBackground Color used for text and icons displayed on top of the background color.
 * @property surface The surface color that affect surfaces of components, such as cards, sheets,
 * and menus.
 * @property onSurface Color used for text and icons displayed on top of the surface color.
 * @property surfaceVariant Another option for a color with similar uses of [surface].
 * @property onSurfaceVariant The color (and state variants) that can be used for content on top of
 * [surface].
 * @property surfaceTint This color will be used by components that apply tonal elevation and is
 * applied on top of [surface]. The higher the elevation the more this color is used.
 * @property inverseSurface A color that contrasts sharply with [surface]. Useful for surfaces that
 * sit on top of other surfaces with [surface] color.
 * @property inverseOnSurface A color that contrasts well with [inverseSurface]. Useful for content
 * that sits on top of containers that are [inverseSurface].
 * @property error The error color is used to indicate errors in components, such as invalid text in
 * a text field.
 * @property onError Color used for text and icons displayed on top of the error color.
 * @property errorContainer The preferred tonal color of error containers.
 * @property onErrorContainer The color (and state variants) that should be used for content on
 * top of [errorContainer].
 * @property outline Subtle color used for boundaries. Outline color role adds contrast for
 * accessibility purposes.
 * @property outlineVariant Utility color used for boundaries for decorative elements when strong
 * contrast is not required.
 * @property scrim Color of a scrim that obscures content.
 * @property surfaceBright A [surface] variant that is always brighter than [surface], whether in
 * light or dark mode.
 * @property surfaceDim A [surface] variant that is always dimmer than [surface], whether in light or
 * dark mode.
 * @property surfaceContainer A [surface] variant that affects containers of components, such as
 * cards, sheets, and menus.
 * @property surfaceContainerHigh A [surface] variant for containers with higher emphasis than
 * [surfaceContainer]. Use this role for content which requires more emphasis than [surfaceContainer].
 * @property surfaceContainerHighest A [surface] variant for containers with higher emphasis than
 * [surfaceContainerHigh]. Use this role for content which requires more emphasis than
 * [surfaceContainerHigh].
 * @property surfaceContainerLow A [surface] variant for containers with lower emphasis than
 * [surfaceContainer]. Use this role for content which requires less emphasis than [surfaceContainer].
 * @property surfaceContainerLowest A [surface] variant for containers with lower emphasis than
 * [surfaceContainerLow]. Use this role for content which requires less emphasis than
 * [surfaceContainerLow].
 */
@Immutable
class ColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
) {
    constructor(
        primary: Color,
        onPrimary: Color,
        primaryContainer: Color,
        onPrimaryContainer: Color,
        inversePrimary: Color,
        secondary: Color,
        onSecondary: Color,
        secondaryContainer: Color,
        onSecondaryContainer: Color,
        tertiary: Color,
        onTertiary: Color,
        tertiaryContainer: Color,
        onTertiaryContainer: Color,
        background: Color,
        onBackground: Color,
        surface: Color,
        onSurface: Color,
        surfaceVariant: Color,
        onSurfaceVariant: Color,
        surfaceTint: Color,
        inverseSurface: Color,
        inverseOnSurface: Color,
        error: Color,
        onError: Color,
        errorContainer: Color,
        onErrorContainer: Color,
        outline: Color,
        outlineVariant: Color,
        scrim: Color,
    ) : this(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = Color.Unspecified,
        surfaceDim = Color.Unspecified,
        surfaceContainer = Color.Unspecified,
        surfaceContainerHigh = Color.Unspecified,
        surfaceContainerHighest = Color.Unspecified,
        surfaceContainerLow = Color.Unspecified,
        surfaceContainerLowest = Color.Unspecified,
    )

    /** Returns a copy of this ColorScheme, optionally overriding some of the values. */
    fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        primaryContainer: Color = this.primaryContainer,
        onPrimaryContainer: Color = this.onPrimaryContainer,
        inversePrimary: Color = this.inversePrimary,
        secondary: Color = this.secondary,
        onSecondary: Color = this.onSecondary,
        secondaryContainer: Color = this.secondaryContainer,
        onSecondaryContainer: Color = this.onSecondaryContainer,
        tertiary: Color = this.tertiary,
        onTertiary: Color = this.onTertiary,
        tertiaryContainer: Color = this.tertiaryContainer,
        onTertiaryContainer: Color = this.onTertiaryContainer,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        surfaceVariant: Color = this.surfaceVariant,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        surfaceTint: Color = this.surfaceTint,
        inverseSurface: Color = this.inverseSurface,
        inverseOnSurface: Color = this.inverseOnSurface,
        error: Color = this.error,
        onError: Color = this.onError,
        errorContainer: Color = this.errorContainer,
        onErrorContainer: Color = this.onErrorContainer,
        outline: Color = this.outline,
        outlineVariant: Color = this.outlineVariant,
        scrim: Color = this.scrim,
        surfaceBright: Color = this.surfaceBright,
        surfaceDim: Color = this.surfaceDim,
        surfaceContainer: Color = this.surfaceContainer,
        surfaceContainerHigh: Color = this.surfaceContainerHigh,
        surfaceContainerHighest: Color = this.surfaceContainerHighest,
        surfaceContainerLow: Color = this.surfaceContainerLow,
        surfaceContainerLowest: Color = this.surfaceContainerLowest,
    ): ColorScheme =
        ColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            inversePrimary = inversePrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceTint = surfaceTint,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
            surfaceBright = surfaceBright,
            surfaceDim = surfaceDim,
            surfaceContainer = surfaceContainer,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainerLowest = surfaceContainerLowest,
        )

    @Deprecated(
        message =
        "Maintained for binary compatibility. Use overload with additional surface roles " +
                "instead",
        level = DeprecationLevel.HIDDEN
    )
    fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        primaryContainer: Color = this.primaryContainer,
        onPrimaryContainer: Color = this.onPrimaryContainer,
        inversePrimary: Color = this.inversePrimary,
        secondary: Color = this.secondary,
        onSecondary: Color = this.onSecondary,
        secondaryContainer: Color = this.secondaryContainer,
        onSecondaryContainer: Color = this.onSecondaryContainer,
        tertiary: Color = this.tertiary,
        onTertiary: Color = this.onTertiary,
        tertiaryContainer: Color = this.tertiaryContainer,
        onTertiaryContainer: Color = this.onTertiaryContainer,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        surfaceVariant: Color = this.surfaceVariant,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        surfaceTint: Color = this.surfaceTint,
        inverseSurface: Color = this.inverseSurface,
        inverseOnSurface: Color = this.inverseOnSurface,
        error: Color = this.error,
        onError: Color = this.onError,
        errorContainer: Color = this.errorContainer,
        onErrorContainer: Color = this.onErrorContainer,
        outline: Color = this.outline,
        outlineVariant: Color = this.outlineVariant,
        scrim: Color = this.scrim,
    ): ColorScheme =
        copy(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            inversePrimary = inversePrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceTint = surfaceTint,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
        )

    override fun toString(): String {
        return "ColorScheme(" +
                "primary=$primary" +
                "onPrimary=$onPrimary" +
                "primaryContainer=$primaryContainer" +
                "onPrimaryContainer=$onPrimaryContainer" +
                "inversePrimary=$inversePrimary" +
                "secondary=$secondary" +
                "onSecondary=$onSecondary" +
                "secondaryContainer=$secondaryContainer" +
                "onSecondaryContainer=$onSecondaryContainer" +
                "tertiary=$tertiary" +
                "onTertiary=$onTertiary" +
                "tertiaryContainer=$tertiaryContainer" +
                "onTertiaryContainer=$onTertiaryContainer" +
                "background=$background" +
                "onBackground=$onBackground" +
                "surface=$surface" +
                "onSurface=$onSurface" +
                "surfaceVariant=$surfaceVariant" +
                "onSurfaceVariant=$onSurfaceVariant" +
                "surfaceTint=$surfaceTint" +
                "inverseSurface=$inverseSurface" +
                "inverseOnSurface=$inverseOnSurface" +
                "error=$error" +
                "onError=$onError" +
                "errorContainer=$errorContainer" +
                "onErrorContainer=$onErrorContainer" +
                "outline=$outline" +
                "outlineVariant=$outlineVariant" +
                "scrim=$scrim" +
                "surfaceBright=$surfaceBright" +
                "surfaceDim=$surfaceDim" +
                "surfaceContainer=$surfaceContainer" +
                "surfaceContainerHigh=$surfaceContainerHigh" +
                "surfaceContainerHighest=$surfaceContainerHighest" +
                "surfaceContainerLow=$surfaceContainerLow" +
                "surfaceContainerLowest=$surfaceContainerLowest" +
                ")"
    }

    var defaultButtonColorsCached: ButtonColors? = null
    var defaultElevatedButtonColorsCached: ButtonColors? = null
    var defaultFilledTonalButtonColorsCached: ButtonColors? = null
    var defaultOutlinedButtonColorsCached: ButtonColors? = null
    var defaultTextButtonColorsCached: ButtonColors? = null

    var defaultCardColorsCached: CardColors? = null
    var defaultElevatedCardColorsCached: CardColors? = null
    var defaultOutlinedCardColorsCached: CardColors? = null

    var defaultAssistChipColorsCached: ChipColors? = null
    var defaultElevatedAssistChipColorsCached: ChipColors? = null
    var defaultSuggestionChipColorsCached: ChipColors? = null
    var defaultElevatedSuggestionChipColorsCached: ChipColors? = null
    var defaultFilterChipColorsCached: SelectableChipColors? = null
    var defaultElevatedFilterChipColorsCached: SelectableChipColors? = null
    var defaultInputChipColorsCached: SelectableChipColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultTopAppBarColorsCached: TopAppBarColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultCenterAlignedTopAppBarColorsCached: TopAppBarColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultMediumTopAppBarColorsCached: TopAppBarColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultLargeTopAppBarColorsCached: TopAppBarColors? = null

    var defaultCheckboxColorsCached: CheckboxColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultDatePickerColorsCached: DatePickerColors? = null

    var defaultIconButtonColorsCached: IconButtonColors? = null

    var defaultMenuItemColorsCached: MenuItemColors? = null

    var defaultNavigationBarItemColorsCached: NavigationBarItemColors? = null

    var defaultNavigationRailItemColorsCached: NavigationRailItemColors? = null

    var defaultRadioButtonColorsCached: RadioButtonColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultSegmentedButtonColorsCached: SegmentedButtonColors? = null

    var defaultSliderColorsCached: SliderColors? = null

    var defaultSwitchColorsCached: SwitchColors? = null

    var defaultOutlinedTextFieldColorsCached: TextFieldColors? = null
    var defaultTextFieldColorsCached: TextFieldColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultTimePickerColorsCached: TimePickerColors? = null

    @OptIn(ExperimentalMaterial3Api::class)
    var defaultRichTooltipColorsCached: RichTooltipColors? = null
}

/**
 * CompositionLocal used to pass [ColorSchemeLocal] down the tree.
 *
 * Setting the value here is typically done as part of [MaterialTheme].
 * To retrieve the current value of this CompositionLocal, use
 * [MaterialTheme.colorSchemeLocal].
 */
//val LocalColorScheme = staticCompositionLocalOf { lightColorScheme() }

/**
 * Helper function for component color tokens. Here is an example on how to use component color
 * tokens:
 * ``MaterialTheme.colorScheme.fromToken(ExtendedFabBranded.BrandedContainerColor)``
 */
@Stable
fun ColorScheme.fromToken(value: ColorSchemeKeyTokens): Color {
    return when (value) {
        ColorSchemeKeyTokens.Background -> background
        ColorSchemeKeyTokens.Error -> error
        ColorSchemeKeyTokens.ErrorContainer -> errorContainer
        ColorSchemeKeyTokens.InverseOnSurface -> inverseOnSurface
        ColorSchemeKeyTokens.InversePrimary -> inversePrimary
        ColorSchemeKeyTokens.InverseSurface -> inverseSurface
        ColorSchemeKeyTokens.OnBackground -> onBackground
        ColorSchemeKeyTokens.OnError -> onError
        ColorSchemeKeyTokens.OnErrorContainer -> onErrorContainer
        ColorSchemeKeyTokens.OnPrimary -> onPrimary
        ColorSchemeKeyTokens.OnPrimaryContainer -> onPrimaryContainer
        ColorSchemeKeyTokens.OnSecondary -> onSecondary
        ColorSchemeKeyTokens.OnSecondaryContainer -> onSecondaryContainer
        ColorSchemeKeyTokens.OnSurface -> onSurface
        ColorSchemeKeyTokens.OnSurfaceVariant -> onSurfaceVariant
        ColorSchemeKeyTokens.SurfaceTint -> surfaceTint
        ColorSchemeKeyTokens.OnTertiary -> onTertiary
        ColorSchemeKeyTokens.OnTertiaryContainer -> onTertiaryContainer
        ColorSchemeKeyTokens.Outline -> outline
        ColorSchemeKeyTokens.OutlineVariant -> outlineVariant
        ColorSchemeKeyTokens.Primary -> primary
        ColorSchemeKeyTokens.PrimaryContainer -> primaryContainer
        ColorSchemeKeyTokens.Scrim -> scrim
        ColorSchemeKeyTokens.Secondary -> secondary
        ColorSchemeKeyTokens.SecondaryContainer -> secondaryContainer
        ColorSchemeKeyTokens.Surface -> surface
        ColorSchemeKeyTokens.SurfaceVariant -> surfaceVariant
        ColorSchemeKeyTokens.SurfaceBright -> surfaceBright
        ColorSchemeKeyTokens.SurfaceContainer -> surfaceContainer
        ColorSchemeKeyTokens.SurfaceContainerHigh -> surfaceContainerHigh
        ColorSchemeKeyTokens.SurfaceContainerHighest -> surfaceContainerHighest
        ColorSchemeKeyTokens.SurfaceContainerLow -> surfaceContainerLow
        ColorSchemeKeyTokens.SurfaceContainerLowest -> surfaceContainerLowest
        ColorSchemeKeyTokens.SurfaceDim -> surfaceDim
        ColorSchemeKeyTokens.Tertiary -> tertiary
        ColorSchemeKeyTokens.TertiaryContainer -> tertiaryContainer
        else -> androidx.compose.ui.graphics.Color.Unspecified
    }
}

val ColorSchemeKeyTokens.value: Color
    @ReadOnlyComposable
    @Composable
    get() = MaterialTheme.colorScheme.fromToken(this)

fun androidx.compose.material3.ColorScheme.toThisColorScheme(): ColorScheme {
    return ColorScheme(
        primary,
        onPrimary,
        primaryContainer,
        onPrimaryContainer,
        inversePrimary,
        secondary,
        onSecondary,
        secondaryContainer,
        onSecondaryContainer,
        tertiary,
        onTertiary,
        tertiaryContainer,
        onTertiaryContainer,
        background,
        onBackground,
        surface,
        onSurface,
        surfaceVariant,
        onSurfaceVariant,
        surfaceTint,
        inverseSurface,
        inverseOnSurface,
        error,
        onError,
        errorContainer,
        onErrorContainer,
        outline,
        outlineVariant,
        scrim,
        surfaceBright,
        surfaceDim,
        surfaceContainer,
        surfaceContainerHigh,
        surfaceContainerHighest,
        surfaceContainerLow,
        surfaceContainerLowest
    )
}
