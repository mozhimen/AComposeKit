package com.mozhimen.composek.switchk

import androidx.compose.ui.unit.dp

/**
 * @ClassName SwitchTokens
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/14
 * @Version 1.0
 */
object SwitchTokens {
    val DisabledSelectedHandleColor = ColorSchemeKeyTokens.Surface
    const val DisabledSelectedHandleOpacity = 1.0f
    val DisabledSelectedIconColor = ColorSchemeKeyTokens.OnSurface
    const val DisabledSelectedIconOpacity = 0.38f
    val DisabledSelectedTrackColor = ColorSchemeKeyTokens.OnSurface
    const val DisabledTrackOpacity = 0.12f
    val DisabledUnselectedHandleColor = ColorSchemeKeyTokens.OnSurface
    const val DisabledUnselectedHandleOpacity = 0.38f
    val DisabledUnselectedIconColor = ColorSchemeKeyTokens.SurfaceVariant
    const val DisabledUnselectedIconOpacity = 0.38f
    val DisabledUnselectedTrackColor = ColorSchemeKeyTokens.SurfaceVariant
    val DisabledUnselectedTrackOutlineColor = ColorSchemeKeyTokens.OnSurface
    val HandleShape = ShapeKeyTokens.CornerFull
    val PressedHandleHeight = 28.0.dp
    val PressedHandleWidth = 28.0.dp
    val SelectedFocusHandleColor = ColorSchemeKeyTokens.PrimaryContainer
    val SelectedFocusIconColor = ColorSchemeKeyTokens.OnPrimaryContainer
    val SelectedFocusTrackColor = ColorSchemeKeyTokens.Primary
    val SelectedHandleColor = ColorSchemeKeyTokens.OnPrimary
    val SelectedHandleHeight = 24.0.dp
    val SelectedHandleWidth = 24.0.dp
    val SelectedHoverHandleColor = ColorSchemeKeyTokens.PrimaryContainer
    val SelectedHoverIconColor = ColorSchemeKeyTokens.OnPrimaryContainer
    val SelectedHoverTrackColor = ColorSchemeKeyTokens.Primary
    val SelectedIconColor = ColorSchemeKeyTokens.OnPrimaryContainer
    val SelectedIconSize = 16.0.dp
    val SelectedPressedHandleColor = ColorSchemeKeyTokens.PrimaryContainer
    val SelectedPressedIconColor = ColorSchemeKeyTokens.OnPrimaryContainer
    val SelectedPressedTrackColor = ColorSchemeKeyTokens.Primary
    val SelectedTrackColor = ColorSchemeKeyTokens.Primary
    val StateLayerShape = ShapeKeyTokens.CornerFull
    val StateLayerSize = 40.0.dp
    val TrackHeight = 32.0.dp
    val TrackOutlineWidth = 2.0.dp
    val TrackShape = ShapeKeyTokens.CornerFull
    val TrackWidth = 52.0.dp
    val UnselectedFocusHandleColor = ColorSchemeKeyTokens.OnSurfaceVariant
    val UnselectedFocusIconColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedFocusTrackColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedFocusTrackOutlineColor = ColorSchemeKeyTokens.Outline
    val UnselectedHandleColor = ColorSchemeKeyTokens.Outline
    val UnselectedHandleHeight = 16.0.dp
    val UnselectedHandleWidth = 16.0.dp
    val UnselectedHoverHandleColor = ColorSchemeKeyTokens.OnSurfaceVariant
    val UnselectedHoverIconColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedHoverTrackColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedHoverTrackOutlineColor = ColorSchemeKeyTokens.Outline
    val UnselectedIconColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedIconSize = 16.0.dp
    val UnselectedPressedHandleColor = ColorSchemeKeyTokens.OnSurfaceVariant
    val UnselectedPressedIconColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedPressedTrackColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedPressedTrackOutlineColor = ColorSchemeKeyTokens.Outline
    val UnselectedTrackColor = ColorSchemeKeyTokens.SurfaceVariant
    val UnselectedTrackOutlineColor = ColorSchemeKeyTokens.Outline
    val IconHandleHeight = 24.0.dp
    val IconHandleWidth = 24.0.dp
}

enum class ColorSchemeKeyTokens {
    Background,
    Error,
    ErrorContainer,
    InverseOnSurface,
    InversePrimary,
    InverseSurface,
    OnBackground,
    OnError,
    OnErrorContainer,
    OnPrimary,
    OnPrimaryContainer,
    OnPrimaryFixed,
    OnPrimaryFixedVariant,
    OnSecondary,
    OnSecondaryContainer,
    OnSecondaryFixed,
    OnSecondaryFixedVariant,
    OnSurface,
    OnSurfaceVariant,
    OnTertiary,
    OnTertiaryContainer,
    OnTertiaryFixed,
    OnTertiaryFixedVariant,
    Outline,
    OutlineVariant,
    Primary,
    PrimaryContainer,
    PrimaryFixed,
    PrimaryFixedDim,
    Scrim,
    Secondary,
    SecondaryContainer,
    SecondaryFixed,
    SecondaryFixedDim,
    Surface,
    SurfaceBright,
    SurfaceContainer,
    SurfaceContainerHigh,
    SurfaceContainerHighest,
    SurfaceContainerLow,
    SurfaceContainerLowest,
    SurfaceDim,
    SurfaceTint,
    SurfaceVariant,
    Tertiary,
    TertiaryContainer,
    TertiaryFixed,
    TertiaryFixedDim,
}

enum class ShapeKeyTokens {
    CornerExtraLarge,
    CornerExtraLargeTop,
    CornerExtraSmall,
    CornerExtraSmallTop,
    CornerFull,
    CornerLarge,
    CornerLargeEnd,
    CornerLargeTop,
    CornerMedium,
    CornerNone,
    CornerSmall,
}
